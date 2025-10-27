package Exerc_Extra.Cyber;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Classe consumidora que processa pagamentos
class ProcessadorPagamentos implements Runnable {
    private final PriorityBlockingQueue<Pagamento> fila;
    private final GerenciadorContas gerenciador;
    private final AtomicInteger processadosComSucesso;
    private final AtomicInteger rejeitados;
    private volatile boolean executando = true;
    
    public ProcessadorPagamentos(PriorityBlockingQueue<Pagamento> fila, GerenciadorContas gerenciador,
                                AtomicInteger processadosComSucesso, AtomicInteger rejeitados) {
        this.fila = fila;
        this.gerenciador = gerenciador;
        this.processadosComSucesso = processadosComSucesso;
        this.rejeitados = rejeitados;
    }
    
    public void parar() {
        executando = false;
    }
    
    @Override
    public void run() {
        try {
            while (executando || !fila.isEmpty()) {
                // Tenta remover da fila com timeout para verificar se ainda está executando
                Pagamento pagamento = fila.poll(1, TimeUnit.SECONDS);
                
                if (pagamento != null) {
                    processarPagamento(pagamento);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Processador] Interrompido");
        }
    }
    
    private void processarPagamento(Pagamento pagamento) throws InterruptedException {
        // Simula tempo de processamento (150-350ms)
        Thread.sleep(ThreadLocalRandom.current().nextInt(150, 351));
        
        String cliente = pagamento.getNome();
        double valor = pagamento.getValor();
        double saldoAtual = gerenciador.consultarSaldo(cliente);
        
        // Verifica saldo e processa pagamento
        if (saldoAtual >= valor) {
            if (gerenciador.debitar(cliente, valor)) {
                processadosComSucesso.incrementAndGet();
                System.out.printf("[PROCESSADO] ID: %d | Cliente: %s | Valor: R$ %.2f | Tipo: %s | Saldo anterior: R$ %.2f\n",
                                 pagamento.getId(), cliente, valor, pagamento.getTipoPagamento(), saldoAtual);
            }
        } else {
            rejeitados.incrementAndGet();
            System.out.printf("[REJEITADO] ID: %d | Cliente: %s | Valor: R$ %.2f | Saldo insuficiente: R$ %.2f\n",
                             pagamento.getId(), cliente, valor, saldoAtual);
        }
    }
}
