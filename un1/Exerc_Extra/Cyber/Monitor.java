package Exerc_Extra.Cyber;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

// Classe para monitoramento do sistema
class Monitor implements Runnable {
    private final PriorityBlockingQueue<Pagamento> fila;
    private final AtomicInteger processadosComSucesso;
    private final AtomicInteger rejeitados;
    private final long inicio;
    private volatile boolean executando = true;
    private int ultimoTotalProcessados = 0;
    private long ultimoTempo = System.currentTimeMillis();
    
    public Monitor(PriorityBlockingQueue<Pagamento> fila, AtomicInteger processadosComSucesso, 
                   AtomicInteger rejeitados, long inicio) {
        this.fila = fila;
        this.processadosComSucesso = processadosComSucesso;
        this.rejeitados = rejeitados;
        this.inicio = inicio;
    }
    
    public void parar() {
        executando = false;
    }
    
    @Override
    public void run() {
        try {
            while (executando) {
                exibirStatus();
                Thread.sleep(2000); // Atualiza a cada 2 segundos
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Monitor] Interrompido");
        }
    }
    
    private void exibirStatus() {
        int tamanhoFila = fila.size();
        int totalSucesso = processadosComSucesso.get();
        int totalRejeitados = rejeitados.get();
        int totalProcessados = totalSucesso + totalRejeitados;
        long tempoAtual = System.currentTimeMillis();
        
        // Calcula taxa de processamento (pagamentos/segundo)
        int processadosDesdeUltimaVerificacao = totalProcessados - ultimoTotalProcessados;
        long tempoDesdeUltimaVerificacao = tempoAtual - ultimoTempo;
        double taxaProcessamento = tempoDesdeUltimaVerificacao > 0 ? 
            (processadosDesdeUltimaVerificacao * 1000.0) / tempoDesdeUltimaVerificacao : 0;
        
        System.out.printf("\n=== MONITORAMENTO ===\n");
        System.out.printf("Tamanho da fila: %d\n", tamanhoFila);
        System.out.printf("Processados com sucesso: %d\n", totalSucesso);
        System.out.printf("Rejeitados: %d\n", totalRejeitados);
        System.out.printf("Taxa de processamento: %.2f pagamentos/segundo\n", taxaProcessamento);
        System.out.printf("Tempo de execução: %.2f segundos\n", (tempoAtual - inicio) / 1000.0);
        
        ultimoTotalProcessados = totalProcessados;
        ultimoTempo = tempoAtual;
    }
}


