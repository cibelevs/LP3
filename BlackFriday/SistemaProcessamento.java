import java.util.concurrent.*;

public class SistemaProcessamento {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE PROCESSAMENTO DE PEDIDOS  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // --- Fila de pedidos com prioridade ---
        BlockingQueue<Pedido> fila = new PriorityBlockingQueue<>(50, new ComparadorPedidos());

        // --- Estoque inicial ---
        GerenciadorEstoque estoque = new GerenciadorEstoque();
        
        // --- Exibir estoque inicial ---
        System.out.println("ESTOQUE INICIAL");
        estoque.exibirEstoque();

        // --- Estatísticas ---
        GerenciadorEstatisticas stats = new GerenciadorEstatisticas();

        // --- Pool de Produtores (3 fontes) ---
        ExecutorService produtores = Executors.newFixedThreadPool(3);
        produtores.execute(new Produtor(fila, "API", 20, stats));
        produtores.execute(new Produtor(fila, "Web", 20, stats));
        produtores.execute(new Produtor(fila, "Mobile", 20, stats));
        produtores.shutdown();

        // --- Pool de Consumidores (5 consumidores) ---
        ExecutorService consumidores = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            consumidores.execute(new Consumidor(fila, estoque, stats, i));
        }

        // --- Thread de monitoramento ---
        Thread monitor = new Thread(new Monitor(fila, stats));
        monitor.start();

        // --- Aguarda término dos produtores ---
        try {
            produtores.awaitTermination(30, TimeUnit.SECONDS);
            System.out.println("\nTodos os produtores finalizaram!\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // --- Aguarda término do processamento ---
        consumidores.shutdown();
        try {
            consumidores.awaitTermination(30, TimeUnit.SECONDS);
            System.out.println("\nTodos os consumidores finalizaram!\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // --- Encerra monitoramento ---
        monitor.interrupt();
        try {
            monitor.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // --- Relatório final ---
        stats.exibirRelatorioFinal();

        System.out.println("\nESTOQUE FINAL");
        estoque.exibirEstoqueFinal();

        // Validação
        int totalProcessado = stats.getPedidosProcessados() + stats.getPedidosRejeitados();
        if (totalProcessado == stats.getPedidosGerados()) {
            System.out.println("VALIDAÇÃO: Todos os pedidos foram processados corretamente!");
        } else {
            System.out.println("AVISO: Alguns pedidos podem não ter sido processados!");
        }

        System.out.println("\nSistema finalizado com sucesso!");
    }
}