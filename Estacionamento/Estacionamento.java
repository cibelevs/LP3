import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simulação de estacionamento com vagas regulares e prioritárias.
 *
 * Requisitos implementados:
 * - 5 vagas regulares
 * - 2 vagas prioritárias
 * - 1 portão de entrada (semaphore)
 * - 1 portão de saída (semaphore)
 * - Preferência para veículos prioritários (tenta vaga prioritária primeiro)
 * - 20 veículos chegam (30% prioritários, 70% normais)
 * - FixedThreadPool com 4 threads para processar chegadas
 * - Cada veículo tenta entrar, estaciona por 1-5s se conseguir vaga, sai.
 * - Logs + estatísticas (entraram, desistiram, tempos min/max/média, ocupação máxima)
 */
public class Estacionamento {

    // Semaphores conforme requisitos
    private static final Semaphore vagaNorm = new Semaphore(5);
    private static final Semaphore vagaPri  = new Semaphore(2);
    private static final Semaphore entrada   = new Semaphore(1);
    private static final Semaphore saida     = new Semaphore(1);

    // Contadores thread-safe
    private static final AtomicInteger contadorSemVaga = new AtomicInteger(0);
    private static final AtomicInteger totalEntraram  = new AtomicInteger(0);
    private static final AtomicInteger currentOcupacao = new AtomicInteger(0);
    private static final AtomicInteger maxOcupacao     = new AtomicInteger(0);

    // Lista sincronizada para armazenar tempos de permanência (ms)
    private static final List<Long> temposPermanencia = Collections.synchronizedList(new ArrayList<>());

    // Lista de logs (opcional, sincronizada)
    private static final List<String> logs = Collections.synchronizedList(new ArrayList<>());

    // Constantes
    private static final int TOTAL_VEICULOS = 20;
    private static final int POOL_THREADS = 4;
    private static final int CAPACIDADE_TOTAL = 5 + 2; // 7

    public static void main(String[] args) {
        System.out.println("Iniciando simulação do estacionamento...\n");

        ExecutorService executor = Executors.newFixedThreadPool(POOL_THREADS);
        CountDownLatch latch = new CountDownLatch(TOTAL_VEICULOS); // para aguardar todas as tarefas

        for (int i = 1; i <= TOTAL_VEICULOS; i++) {
            final int id = i;
            final boolean isPrioritario = ThreadLocalRandom.current().nextDouble() < 0.3; // 30%

            executor.submit(() -> {
                Veiculo v = new Veiculo(id, isPrioritario);

                // Espera para usar o portão de entrada (1 por vez)
                try {
                    entrada.acquire();
                    logsAdd(String.format("Veículo %d (prioritário=%s) chegou e está acessando o portão de entrada.",
                            v.getId(), v.isPrioritario()));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logsAdd("Thread interrompida ao adquirir portão de entrada para veículo " + v.getId());
                    latch.countDown();
                    entrada.release();
                    return;
                }

                // Após adquirir o portão, tentar conseguir vaga conforme prioridade
                boolean conseguiuVaga = false;
                VagaTipo vagaAlocada = VagaTipo.NENHUMA;

                if (v.isPrioritario()) {
                    // tenta vaga prioritária primeiro
                    if (vagaPri.tryAcquire()) {
                        conseguiuVaga = true;
                        vagaAlocada = VagaTipo.PRIORITARIA;
                    } else if (vagaNorm.tryAcquire()) {
                        conseguiuVaga = true;
                        vagaAlocada = VagaTipo.NORMAL;
                    }
                } else {
                    // veículo normal tenta vaga normal
                    if (vagaNorm.tryAcquire()) {
                        conseguiuVaga = true;
                        vagaAlocada = VagaTipo.NORMAL;
                    }
                }

                // libera o portão de entrada depois da tentativa
                entrada.release();

                if (!conseguiuVaga) {
                    contadorSemVaga.incrementAndGet();
                    logsAdd(String.format("Veículo %d não conseguiu vaga e saiu (sem vaga).", v.getId()));
                    latch.countDown();
                    return;
                }

                // se chegou aqui, ocupou uma vaga
                totalEntraram.incrementAndGet();
                v.setVaga(vagaAlocada);
                v.setEstacionado(true);

                // Atualiza ocupação atual e máximo
                int ocup = currentOcupacao.incrementAndGet();
                updateMaxOcupacao(ocup);

                logsAdd(String.format("Veículo %d ocupou vaga %s. Ocupação atual: %d/%d",
                        v.getId(), vagaAlocada, ocup, CAPACIDADE_TOTAL));

                // Simula tempo de permanência (1 a 5 segundos)
                long permanenciaMs = 0L;
                try {
                    int tempoSec = 1 + ThreadLocalRandom.current().nextInt(5); // 1..5
                    permanenciaMs = tempoSec * 1000L;
                    Thread.sleep(permanenciaMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logsAdd("Veículo " + v.getId() + " interrompido durante permanência.");
                }

                // Registra tempo
                temposPermanencia.add(permanenciaMs);

                // Saída: precisa adquirir portão de saída (1 por vez)
                try {
                    saida.acquire();
                    logsAdd(String.format("Veículo %d aguardando portão de saída e iniciando saída.", v.getId()));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logsAdd("Thread interrompida ao adquirir portão de saída para veículo " + v.getId());
                    saida.release();
                    latch.countDown();
                    return;
                }

                // libera a vaga correspondente
                if (v.getVaga() == VagaTipo.PRIORITARIA) {
                    vagaPri.release();
                    logsAdd(String.format("Veículo %d liberou vaga PRIORITÁRIA.", v.getId()));
                } else if (v.getVaga() == VagaTipo.NORMAL) {
                    vagaNorm.release();
                    logsAdd(String.format("Veículo %d liberou vaga NORMAL.", v.getId()));
                }

                // atualiza ocupação
                int atual = currentOcupacao.decrementAndGet();
                logsAdd(String.format("Veículo %d saiu. Ocupação atual: %d/%d", v.getId(), atual, CAPACIDADE_TOTAL));

                // libera portão de saída
                saida.release();

                // marca veículo como não estacionado
                v.setEstacionado(false);
                v.setVaga(VagaTipo.NENHUMA);

                latch.countDown();
            });
        }

        // aguardar conclusão
        executor.shutdown();
        try {
            // aguarda todas as tarefas terminarem (com um timeout razoável)
            if (!latch.await(120, TimeUnit.SECONDS)) {
                System.out.println("Timeout aguardando finalização das tarefas.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // garante que o executor encerre
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Imprime logs (opcional) e estatísticas finais
        imprimirRelatorioFinal();
    }

    // Atualiza o valor máximo de ocupação (thread-safe)
    private static void updateMaxOcupacao(int ocupAtual) {
        maxOcupacao.getAndUpdate(prev -> Math.max(prev, ocupAtual));
    }

    private static void logsAdd(String s) {
        String entry = String.format("[%s] %s", java.time.LocalTime.now().withNano(0), s);
        logs.add(entry);
        // Também imprime imediatamente (útil pra acompanhar)
        System.out.println(entry);
    }

    private static void imprimirRelatorioFinal() {
        System.out.println("\n========== RELATÓRIO FINAL ==========");
        System.out.printf("Total de veículos gerados: %d%n", TOTAL_VEICULOS);
        System.out.printf("Entraram e estacionaram: %d%n", totalEntraram.get());
        System.out.printf("Não conseguiram vaga (desistências): %d%n", contadorSemVaga.get());

        // tempos
        long soma = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        synchronized (temposPermanencia) {
            for (Long t : temposPermanencia) {
                soma += t;
                min = Math.min(min, t);
                max = Math.max(max, t);
            }
        }

        int registrados = temposPermanencia.size();
        double media = registrados > 0 ? (soma / (double) registrados) / 1000.0 : 0.0; // em segundos
        double minSec = (min == Long.MAX_VALUE) ? 0.0 : min / 1000.0;
        double maxSec = (max == Long.MIN_VALUE) ? 0.0 : max / 1000.0;

        System.out.printf("Veículos com tempo registrado: %d%n", registrados);
        System.out.printf("Tempo médio de permanência: %.2f s%n", media);
        System.out.printf("Tempo mínimo de permanência: %.2f s%n", minSec);
        System.out.printf("Tempo máximo de permanência: %.2f s%n", maxSec);

        // ocupação
        System.out.printf("Capacidade total: %d vagas (regulares %d + prioritárias %d)%n", CAPACIDADE_TOTAL, 5, 2);
        System.out.printf("Ocupação máxima observada: %d%n", maxOcupacao.get());
        double taxaOcupacao = (maxOcupacao.get() / (double) CAPACIDADE_TOTAL) * 100.0;
        System.out.printf("Taxa máxima de ocupação: %.2f%%%n", taxaOcupacao);

        double taxaSucesso = (TOTAL_VEICULOS > 0) ? (totalEntraram.get() / (double) TOTAL_VEICULOS) * 100.0 : 0.0;
        System.out.printf("Taxa de sucesso (entraram/total): %.2f%%%n", taxaSucesso);

        System.out.println("\nLogs (últimas entradas):");
        synchronized (logs) {
            int start = Math.max(0, logs.size() - 50); // mostra até 50 últimos registros
            for (int i = start; i < logs.size(); i++) {
                System.out.println(logs.get(i));
            }
        }

        System.out.println("\nSIMULAÇÃO FINALIZADA.");
    }

    // Enum para o tipo de vaga ocupada pelo veículo
    private enum VagaTipo {
        NENHUMA, NORMAL, PRIORITARIA;

        @Override
        public String toString() {
            switch(this) {
                case NORMAL: return "NORMAL";
                case PRIORITARIA: return "PRIORITÁRIA";
                default: return "NENHUMA";
            }
        }
    }

    // Classe Veiculo simplificada
    private static class Veiculo {
        private final int id;
        private final boolean prioritario;
        private volatile boolean estacionado;
        private volatile VagaTipo vaga;

        public Veiculo(int id, boolean prioritario) {
            this.id = id;
            this.prioritario = prioritario;
            this.estacionado = false;
            this.vaga = VagaTipo.NENHUMA;
        }

        public int getId() { return id; }
        public boolean isPrioritario() { return prioritario; }
        public boolean isEstacionado() { return estacionado; }
        public void setEstacionado(boolean b) { this.estacionado = b; }
        public VagaTipo getVaga() { return vaga; }
        public void setVaga(VagaTipo v) { this.vaga = v; }
    }
}



