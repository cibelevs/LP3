package Exerc_Extra.Cyber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Classe principal que orquestra todo o sistema
public class SistemaProcessamentoPagamentos {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== INICIANDO SISTEMA DE PROCESSAMENTO DE PAGAMENTOS ===\n");
        
        long inicio = System.currentTimeMillis();
        
        // Cria a fila de prioridades
        PriorityBlockingQueue<Pagamento> fila = new PriorityBlockingQueue<>(50);
        
        // Contadores atômicos para estatísticas
        AtomicInteger contadorId = new AtomicInteger(0);
        AtomicInteger processadosComSucesso = new AtomicInteger(0);
        AtomicInteger rejeitados = new AtomicInteger(0);
        
        // Gerenciador de contas
        GerenciadorContas gerenciador = new GerenciadorContas();
        
        // Pool de produtores
        ExecutorService produtores = Executors.newFixedThreadPool(3);
        String[] fontes = {"App", "Site", "ParceiroAPI"};
        
        // Inicia os produtores
        for (String fonte : fontes) {
            produtores.execute(new GeradorPagamentos(fila, fonte, contadorId));
        }
        
        // Pool de consumidores
        ExecutorService consumidores = Executors.newFixedThreadPool(5);
        ProcessadorPagamentos[] processadores = new ProcessadorPagamentos[5];
        
        // Inicia os consumidores
        for (int i = 0; i < 5; i++) {
            processadores[i] = new ProcessadorPagamentos(fila, gerenciador, processadosComSucesso, rejeitados);
            consumidores.execute(processadores[i]);
        }
        
        // Inicia o monitor
        Monitor monitor = new Monitor(fila, processadosComSucesso, rejeitados, inicio);
        Thread threadMonitor = new Thread(monitor);
        threadMonitor.start();
        
        // Aguarda os produtores terminarem
        produtores.shutdown();
        if (!produtores.awaitTermination(2, TimeUnit.MINUTES)) {
            System.out.println("Timeout nos produtores - forçando shutdown");
            produtores.shutdownNow();
        }
        
        System.out.println("\n=== TODOS OS PAGAMENTOS FORAM GERADOS ===\n");
        
        // Aguarda a fila esvaziar
        int tentativas = 0;
        while (!fila.isEmpty() && tentativas < 30) {
            Thread.sleep(1000);
            tentativas++;
            System.out.printf("Aguardando fila esvaziar... (%d/30 segundos) - Fila: %d\n", tentativas, fila.size());
        }
        
        // Para os consumidores e o monitor
        System.out.println("Parando consumidores...");
        for (ProcessadorPagamentos processador : processadores) {
            processador.parar();
        }
        
        System.out.println("Parando monitor...");
        monitor.parar();
        
        consumidores.shutdown();
        if (!consumidores.awaitTermination(5, TimeUnit.SECONDS)) {
            consumidores.shutdownNow();
        }
        
        threadMonitor.interrupt();
        threadMonitor.join(2000);
        
        long fim = System.currentTimeMillis();
        double tempoTotal = (fim - inicio) / 1000.0;
        
        // Exibe relatório final
        exibirRelatorioFinal(contadorId.get(), processadosComSucesso.get(), 
                           rejeitados.get(), tempoTotal, gerenciador);
    }
    
    private static void exibirRelatorioFinal(int totalGerados, int processadosSucesso, 
                                           int rejeitados, double tempoTotal, GerenciadorContas gerenciador) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("=== RELATÓRIO FINAL DO SISTEMA ===");
        System.out.println("=".repeat(60));
        System.out.printf("Total de pagamentos gerados: %d\n", totalGerados);
        System.out.printf("Total processados com sucesso: %d\n", processadosSucesso);
        System.out.printf("Total rejeitados: %d\n", rejeitados);
        System.out.printf("Tempo total de execução: %.2f segundos\n", tempoTotal);
        
        if (tempoTotal > 0) {
            System.out.printf("Taxa média: %.2f pagamentos/segundo\n", totalGerados / tempoTotal);
        }
        
        gerenciador.exibirSaldosFinais();
        
        System.out.println("\n=== SISTEMA FINALIZADO ===");
    }
}