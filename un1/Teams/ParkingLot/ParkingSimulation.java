package Teams.ParkingLot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ParkingSimulation.java
 *
 * Simulação de um estacionamento com:
 * - 5 vagas normais
 * - 2 vagas prioritárias (idosos/PCD)
 * - 1 portão de entrada (apenas 1 veículo por vez)
 * - 1 portão de saída (apenas 1 veículo por vez)
 *
 * Concorrência controlada via Semaphores.
 * Chegada gerenciada por um ExecutorService (FixedThreadPool com 4 threads).
 *
 * Cada veículo:
 *  - espera para passar pelo portão de entrada (acquire entrada)
 *  - tenta conseguir vaga (prioritário tenta vaga prioritária primeiro)
 *  - permanece estacionado por tempo aleatório (1-5s)
 *  - sai adquirindo o portão de saída e liberando a vaga
 *
 * Estatísticas e logs são coletados e exibidos ao final.
 */
public class ParkingSimulation {

    // Capacidade
    private static final int VAGAS_REGULARES = 5;
    private static final int VAGAS_PRIORITARIAS = 2;

    // GATES
    private static final int PORTAO_ENTRADA_CAP = 1;
    private static final int PORTAO_SAIDA_CAP = 1;

    // Quantidade de veículos a serem gerados
    private static final int TOTAL_VEICULOS = 20;

    // Percentual de prioritários (30%)
    private static final double PCT_PRIORITARIO = 0.30;

    // Executor com pool fixo de 4 threads
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    // Semaphores que controlam recursos compartilhados
    private final Semaphore vagasRegulares = new Semaphore(VAGAS_REGULARES, true);
    private final Semaphore vagasPrioritarias = new Semaphore(VAGAS_PRIORITARIAS, true);
    private final Semaphore portaoEntrada = new Semaphore(PORTAO_ENTRADA_CAP, true);
    private final Semaphore portaoSaida = new Semaphore(PORTAO_SAIDA_CAP, true);

    // Estatísticas e logs (thread-safe / sincronizados)
    private final List<String> logs = Collections.synchronizedList(new ArrayList<>());
    private final List<Long> temposPermanencia = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger totalGerados = new AtomicInteger(0);
    private final AtomicInteger totalEntraram = new AtomicInteger(0);
    private final AtomicInteger totalDesistencias = new AtomicInteger(0);

    private final Random random = new Random();

    // Runnable que representa um veículo
    private class Veiculo implements Runnable {
        private final int id;
        private final boolean prioritario;

        Veiculo(int id, boolean prioritario) {
            this.id = id;
            this.prioritario = prioritario;
        }

        @Override
        public void run() {
            String tipo = prioritario ? "PRIORITARIO" : "NORMAL";
            log(String.format("Veículo #%d (%s) gerado e a caminho...", id, tipo));

            // Simular pequeno tempo entre geração e chegada ao portão (opcional)
            // Aqui colocamos um pequeno delay aleatório (0-400ms) para "espalhar" chegadas
            sleepMillis(random.nextInt(400));

            log(String.format("Veículo #%d (%s) chegou ao portão de entrada", id, tipo));

            // 1) Aguardar para usar o portão de entrada (apenas 1 veículo por vez)
            try {
                portaoEntrada.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log(String.format("Veículo #%d (%s) interrompido ao aguardar portão de entrada", id, tipo));
                return;
            }

            log(String.format("Veículo #%d (%s) está no portão de entrada", id, tipo));

            // 2) Tentar conseguir uma vaga (com prioridade para os prioritários)
            boolean conseguiuVaga = false;
            long entradaHora = System.currentTimeMillis();

            if (prioritario) {
                // Prioritário tenta primeiro vaga prioritária, se não, vaga regular
                if (vagasPrioritarias.tryAcquire()) {
                    conseguiuVaga = true;
                    log(String.format("Veículo #%d (%s) conseguiu vaga PRIORITÁRIA", id, tipo));
                } else if (vagasRegulares.tryAcquire()) {
                    conseguiuVaga = true;
                    log(String.format("Veículo #%d (%s) conseguiu vaga REGULAR", id, tipo));
                }
            } else {
                // Normal só tenta vaga regular (prioritárias são reservadas)
                if (vagasRegulares.tryAcquire()) {
                    conseguiuVaga = true;
                    log(String.format("Veículo #%d (%s) conseguiu vaga REGULAR", id, tipo));
                }
            }

            // Liberar o portão de entrada (independente de ter conseguido vaga ou não)
            portaoEntrada.release();

            if (!conseguiuVaga) {
                // Não conseguiu vaga — registra desistência
                totalDesistencias.incrementAndGet();
                log(String.format("Veículo #%d (%s) desistiu por falta de vagas", id, tipo));
                return; // termina a execução do veículo
            }

            // Se chegou aqui, entrou no estacionamento
            totalEntraram.incrementAndGet();

            // Permanência aleatória (1 a 5 segundos)
            int permanenciaSegs = 1 + random.nextInt(5);
            log(String.format("Veículo #%d (%s) está estacionado por %d segundos", id, tipo, permanenciaSegs));

            long inicioPermanencia = System.currentTimeMillis();
            sleepSeconds(permanenciaSegs);
            long tempoPermMs = System.currentTimeMillis() - inicioPermanencia;
            temposPermanencia.add(tempoPermMs);

            // Saída: adquirir portão de saída (apenas 1 veículo por vez)
            log(String.format("Veículo #%d (%s) está saindo do estacionamento, aguardando portão de saída...", id, tipo));
            try {
                portaoSaida.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log(String.format("Veículo #%d (%s) interrompido ao aguardar portão de saída", id, tipo));
                // mesmo interrompido, tentamos liberar vaga para não travar o sistema
                liberarVagaConformeTipo(prioritario);
                return;
            }

            // Liberar a vaga ao sair
            liberarVagaConformeTipo(prioritario);
            portaoSaida.release();

            log(String.format("Veículo #%d (%s) saiu com sucesso!", id, tipo));
        }

        // Helper: libera a vaga correta
        private void liberarVagaConformeTipo(boolean prioritario) {
            // Preferimos liberar a vaga do tipo que foi ocupada.
            // Como armazenamos só nos semaphores, a liberação é direta.
            // Observação: se prioritário ocupou uma vaga regular (quando prioritarias estavam cheias),
            // a liberação foi feita em vagasRegulares; aqui assumimos a simetria.
            // Para ser robusto, poderia guardar em cada Veículo qual semáforo foi adquirido.
            // Aqui simplificamos liberando preferencialmente prioritária se disponível para liberar (não ideal),
            // portanto para robustez armazenamos qual semáforo usou: (melhoria abaixo).
        }
    }

    // Melhor versão: Veículo guarda qual tipo de vaga ocupou (regular/prioritária) para liberar corretamente.
    // Para isso, implementamos outra classe VeiculoCorrect que salva o tipo de vaga ocupado.
    private class VeiculoCorrect implements Runnable {
        private final int id;
        private final boolean prioritario;
        // guarda qual semáforo foi realmente adquirido: "REGULAR" ou "PRIORITARIA"
        private String vagaOcupada = null;

        VeiculoCorrect(int id, boolean prioritario) {
            this.id = id;
            this.prioritario = prioritario;
        }

        @Override
        public void run() {
            String tipo = prioritario ? "PRIORITARIO" : "NORMAL";
            log(String.format("Gerado: Veículo #%d (%s)", id, tipo));
            totalGerados.incrementAndGet();

            // Pequeno delay de chegada para espalhar eventos
            sleepMillis(random.nextInt(400));

            log(String.format("Veículo #%d (%s) chegou ao portão de entrada", id, tipo));

            try {
                portaoEntrada.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log("Interrompido ao aguardar portão de entrada: " + id);
                return;
            }

            log(String.format("Veículo #%d (%s) está no portão de entrada", id, tipo));

            boolean entrou = false;

            if (prioritario) {
                // tenta prioritária
                if (vagasPrioritarias.tryAcquire()) {
                    vagaOcupada = "PRIORITARIA";
                    entrou = true;
                    log(String.format("Veículo #%d (%s) conseguiu vaga PRIORITÁRIA", id, tipo));
                } else if (vagasRegulares.tryAcquire()) {
                    vagaOcupada = "REGULAR";
                    entrou = true;
                    log(String.format("Veículo #%d (%s) conseguiu vaga REGULAR", id, tipo));
                }
            } else {
                // normal tenta vaga regular apenas
                if (vagasRegulares.tryAcquire()) {
                    vagaOcupada = "REGULAR";
                    entrou = true;
                    log(String.format("Veículo #%d (%s) conseguiu vaga REGULAR", id, tipo));
                }
            }

            // libera portão de entrada sempre
            portaoEntrada.release();

            if (!entrou) {
                totalDesistencias.incrementAndGet();
                log(String.format("Veículo #%d (%s) não conseguiu vaga e desistiu", id, tipo));
                return;
            }

            totalEntraram.incrementAndGet();

            // Permanência aleatória (1 a 5s)
            int permanencia = 1 + random.nextInt(5);
            log(String.format("Veículo #%d (%s) está estacionado por %d segundos (vaga: %s)", id, tipo, permanencia, vagaOcupada));

            long inicio = System.currentTimeMillis();
            sleepSeconds(permanencia);
            long duracaoMs = System.currentTimeMillis() - inicio;
            temposPermanencia.add(duracaoMs);

            // Saída - aguarda portão de saída
            log(String.format("Veículo #%d (%s) indo para saída...", id, tipo));
            try {
                portaoSaida.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log("Interrompido ao aguardar portão de saída: " + id);
                // garante liberação da vaga mesmo se interrompido
                liberarVagaPorTipo(vagaOcupada);
                return;
            }

            // libera vaga correta
            liberarVagaPorTipo(vagaOcupada);
            portaoSaida.release();

            log(String.format("Veículo #%d (%s) saiu com sucesso!", id, tipo));
        }

        private void liberarVagaPorTipo(String tipoVaga) {
            if ("PRIORITARIA".equals(tipoVaga)) {
                vagasPrioritarias.release();
            } else if ("REGULAR".equals(tipoVaga)) {
                vagasRegulares.release();
            } else {
                // fallback (não deveria acontecer)
                vagasRegulares.release();
            }
        }
    }

    // Construtor
    public ParkingSimulation() {
    }

    // Utility: sleep em ms ignorando InterruptedException
    private static void sleepMillis(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Utility: sleep em segundos
    private static void sleepSeconds(long s) {
        sleepMillis(s * 1000);
    }

    // Log thread-safe (armazenamos no list e imprimimos)
    private void log(String mensagem) {
        String text = mensagem;
        logs.add(text);
        // também printamos no console para acompanhar execução
        System.out.println(text);
    }

    // Método que inicia a simulação
    public void executarSimulacao() {
        log("=== INICIANDO SIMULAÇÃO ===");

        // Gerar veículos e submeter ao executor.
        // Usamos VeiculoCorrect para rastrear corretamente qual vaga foi ocupada.
        for (int i = 1; i <= TOTAL_VEICULOS; i++) {
            boolean prioritario = random.nextDouble() < PCT_PRIORITARIO;
            VeiculoCorrect v = new VeiculoCorrect(i, prioritario);
            executor.submit(v);

            // Opcional: espaçar a geração para mais realismo (pequeno delay)
            sleepMillis(100 + random.nextInt(200)); // 100-300ms entre envios
        }

        // Após submeter todos, desligamos o executor e aguardamos término
        executor.shutdown();
        try {
            // espera até 2 minutos para terminar (ajuste se necessário)
            if (!executor.awaitTermination(2, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Ao fim, mostra relatório
        gerarRelatorio();
    }

    // Gera resumo final e imprime
    private void gerarRelatorio() {
        log("");
        log("=== STATUS DO ESTACIONAMENTO ===");
        log(String.format("Vagas regulares disponíveis: %d/%d", vagasRegulares.availablePermits(), VAGAS_REGULARES));
        log(String.format("Vagas prioritárias disponíveis: %d/%d", vagasPrioritarias.availablePermits(), VAGAS_PRIORITARIAS));
        log(String.format("Total de entradas (conseguiram estacionar): %d", totalEntraram.get()));
        log(String.format("Total de desistências (não conseguiram vaga): %d", totalDesistencias.get()));

        // Estatísticas finais sobre tempos de permanência
        log("");
        log("=== ESTATÍSTICAS FINAIS ===");
        int totalVeiculos = TOTAL_VEICULOS;
        int conseguiramEntrar = totalEntraram.get();
        int desistiram = totalDesistencias.get();
        double taxaSucesso = (totalVeiculos == 0) ? 0.0 : (100.0 * conseguiramEntrar / totalVeiculos);

        log(String.format("Total de veículos gerados: %d", totalVeiculos));
        log(String.format("Conseguiram entrar: %d", conseguiramEntrar));
        log(String.format("Desistiram: %d", desistiram));
        log(String.format("Taxa de sucesso: %.1f %%", taxaSucesso));

        if (!temposPermanencia.isEmpty()) {
            long soma = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (Long t : temposPermanencia) {
                soma += t;
                if (t < min) min = t;
                if (t > max) max = t;
            }
            double mediaMs = (double) soma / temposPermanencia.size();
            log(String.format("Tempo médio de permanência: %.2f ms (%.2f s)", mediaMs, mediaMs / 1000.0));
            log(String.format("Tempo mínimo: %d ms (%.2f s)", min, min / 1000.0));
            log(String.format("Tempo máximo: %d ms (%.2f s)", max, max / 1000.0));
        } else {
            log("Nenhum tempo de permanência registrado.");
        }

        // Taxa de ocupação (simples): proporção de veículos que entraram dentro do total gerado
        double taxaOcupacao = (totalVeiculos == 0) ? 0.0 : (100.0 * conseguiramEntrar / totalVeiculos);
        log(String.format("Taxa de ocupação (entraram / gerados): %.1f %%", taxaOcupacao));

        log("");
        log("=== Simulação Concluída ===");
    }

    // Main para rodar
    public static void main(String[] args) {
        ParkingSimulation sim = new ParkingSimulation();
        sim.executarSimulacao();
    }
}
