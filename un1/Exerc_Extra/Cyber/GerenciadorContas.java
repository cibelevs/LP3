package Exerc_Extra.Cyber;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Classe para gerenciar as contas dos clientes com thread safety
class GerenciadorContas {
    private final ConcurrentHashMap<String, Double> contas;
    private final ReentrantReadWriteLock lock;
    
    public GerenciadorContas() {
        this.contas = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        // Inicializa alguns clientes com saldos aleatórios
        inicializarContas();
    }
    
    private void inicializarContas() {
        contas.put("Cliente1", 5000.0);
        contas.put("Cliente2", 3000.0);
        contas.put("Cliente3", 8000.0);
        contas.put("Cliente4", 2000.0);
        contas.put("Cliente5", 6000.0);
    }
    
    public double consultarSaldo(String cliente) {
        lock.readLock().lock();
        try {
            return contas.getOrDefault(cliente, 0.0);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean debitar(String cliente, double valor) {
        lock.writeLock().lock();
        try {
            double saldoAtual = contas.getOrDefault(cliente, 0.0);
            if (saldoAtual >= valor) {
                contas.put(cliente, saldoAtual - valor);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void creditar(String cliente, double valor) {
        lock.writeLock().lock();
        try {
            double saldoAtual = contas.getOrDefault(cliente, 0.0);
            contas.put(cliente, saldoAtual + valor);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void exibirSaldosFinais() {
        System.out.println("\n=== SALDOS FINAIS DOS CLIENTES ===");
        contas.forEach((cliente, saldo) -> 
            System.out.printf("Cliente: %s | Saldo: R$ %.2f\n", cliente, saldo));
    }
}