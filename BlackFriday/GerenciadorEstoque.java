import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class GerenciadorEstoque {
    private final ConcurrentHashMap<String, Integer> estoque;
    private final ReadWriteLock lock;
    
    public GerenciadorEstoque() {
        this.estoque = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        inicializarEstoque();
    }
    
    private void inicializarEstoque() {
        estoque.put("Notebook", 10);
        estoque.put("Mouse", 50);
        estoque.put("Teclado", 30);
        estoque.put("Monitor", 15);
        estoque.put("Headset", 25);
    }
    
    // Método para compatibilidade
    public void adicionarProduto(String produto, int quantidade) {
        estoque.put(produto, quantidade);
    }
    
    public int consultarEstoque(String produto) {
        lock.readLock().lock();
        try {
            return estoque.getOrDefault(produto, 0);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean reservarEstoque(String produto, int quantidade) {
        lock.writeLock().lock();
        try {
            int estoqueAtual = estoque.getOrDefault(produto, 0);
            if (estoqueAtual >= quantidade) {
                estoque.put(produto, estoqueAtual - quantidade);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void devolverEstoque(String produto, int quantidade) {
        lock.writeLock().lock();
        try {
            int estoqueAtual = estoque.getOrDefault(produto, 0);
            estoque.put(produto, estoqueAtual + quantidade);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void exibirEstoque() {
        lock.readLock().lock();
        try {
            System.out.println("\n=== ESTOQUE ATUAL ===");
            estoque.forEach((produto, qtd) -> 
                System.out.printf("%-10s: %3d unidades\n", produto, qtd));
            System.out.println("====================\n");
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void exibirEstoqueFinal() {
        exibirEstoque();
    }
}