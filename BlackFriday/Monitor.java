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
            while (ativo) {
                // TODO: A cada 2 segundos, exibir estatísticas
                Thread.sleep(2000);
                stats.exibirEstatisticas(fila.size());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void parar() {
        ativo = false;
    }
}