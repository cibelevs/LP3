package Exerc_Extra.entrega;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// ------------------------------------------------------------
// CLASSE GerenciadorVeiculos
// Gerencia os veículos disponíveis (Moto, Carro, Van)
// usando ConcurrentHashMap + ReentrantReadWriteLock.
// ------------------------------------------------------------
class GerenciadorVeiculos {
    private final ConcurrentHashMap<String, Boolean> veiculos = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public GerenciadorVeiculos() {
        // Inicializa com 1 de cada tipo
        veiculos.put("Moto", true);
        veiculos.put("Carro", true);
        veiculos.put("Van", true);
    }

    // Tenta reservar um veículo disponível (thread-safe)
    public boolean reservarVeiculo() {
        lock.writeLock().lock();
        try {
            for (String v : veiculos.keySet()) {
                if (veiculos.get(v)) {
                    veiculos.put(v, false);
                    return true;
                }
            }
            return false; // nenhum veículo disponível
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Libera um veículo após o uso
    public void liberarVeiculo(String tipo) {
        lock.writeLock().lock();
        try {
            veiculos.put(tipo, true);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Mostra a situação final dos veículos
    public void exibirEstado() {
        lock.readLock().lock();
        try {
            System.out.println("\n--- ESTADO FINAL DOS VEÍCULOS ---");
            veiculos.forEach((k, v) -> System.out.println(k + ": " + (v ? "Disponível" : "Em uso")));
        } finally {
            lock.readLock().unlock();
        }
    }
}