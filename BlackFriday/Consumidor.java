import java.util.Random;
import java.util.concurrent.*;

class Consumidor implements Runnable {
    private final BlockingQueue<Pedido> fila;
    private final GerenciadorEstoque estoque;
    private final GerenciadorEstatisticas stats;
    private final int id;
    private final Random random = new Random();
    
    public Consumidor(int id, BlockingQueue<Pedido> fila, GerenciadorEstoque estoque,
                     GerenciadorEstatisticas stats) {
        this.id = id;
        this.fila = fila;
        this.estoque = estoque;
        this.stats = stats;
    }
    
    // Construtor alternativo para compatibilidade
    public Consumidor(BlockingQueue<Pedido> fila, GerenciadorEstoque estoque, 
                     GerenciadorEstatisticas stats, int id) {
        this(id, fila, estoque, stats);
    }
    
    @Override
    public void run() {
        try {
            System.out.println("[Consumidor-" + id + "] Pronto para processar pedidos");
            
            while (true) {
                // Poll com timeout de 5 segundos para detectar fim do processamento
                Pedido pedido = fila.poll(5, TimeUnit.SECONDS);
                
                if (pedido == null) {
                    // Timeout - não há mais pedidos chegando
                    break;
                }
                
                processarPedido(pedido);
            }
            
            System.out.println("[Consumidor-" + id + "] Finalizou processamento");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Consumidor-" + id + "] Interrompido");
        }
    }
    
    private void processarPedido(Pedido pedido) throws InterruptedException {
        System.out.println("[Consumidor-" + id + "] Processando: " + pedido);
        
        // Verificar e reservar estoque
        boolean estoqueDisponivel = estoque.reservarEstoque(pedido.getProduto(), pedido.getQuantidade());
        
        if (estoqueDisponivel) {
            // Simular tempo de processamento (100-300ms)
            Thread.sleep(random.nextInt(201) + 100);
            
            // Registrar sucesso
            stats.registrarPedidoProcessado();
            System.out.println("[Consumidor-" + id + "] Processado com sucesso: " + pedido);
        } else {
            // Devolver ao estoque (se alguma reserva parcial foi feita)
            // Registrar rejeição
            stats.registrarPedidoRejeitado();
            System.out.println("[Consumidor-" + id + "] REJEITADO (sem estoque): " + 
                pedido.getProduto() + " x" + pedido.getQuantidade());
        }
    }
}