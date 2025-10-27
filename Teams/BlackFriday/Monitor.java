import java.util.concurrent.*;

class Monitor implements Runnable {
    private final BlockingQueue<Pedido> fila;
    private final GerenciadorEstatisticas stats;
    private volatile boolean ativo = true;
    
    public Monitor(BlockingQueue<Pedido> fila, GerenciadorEstatisticas stats) {
        this.fila = fila;
        this.stats = stats;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("[Monitor] Iniciando monitoramento a cada 2 segundos");
            
            while (ativo && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(2000);
                stats.exibirEstatisticas(fila.size());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[Monitor] Encerrado");
    }
    
    public void parar() {
        ativo = false;
    }
}