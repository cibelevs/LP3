import java.util.concurrent.*;

public class SistemaProcessamento {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE PROCESSAMENTO DE PEDIDOS  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // TODO: Criar BlockingQueue (PriorityBlockingQueue com capacidade 50)
        BlockingQueue<Pedido> fila = null;
        
        // TODO: Criar GerenciadorEstoque
        GerenciadorEstoque estoque = null;
        
        // TODO: Criar GerenciadorEstatisticas
        GerenciadorEstatisticas stats = null;
        
        // TODO: Criar e iniciar Monitor
        Monitor mon = new Monitor(fila, stats);
        
        // TODO: Criar ExecutorService para produtores (3 threads)
        ExecutorService produtores = null;
        produtores = new Executors.newFixedThreadPool(3);
        // TODO: Criar 3 produtores (API, Web, Mobile) - cada um gera 20 pedidos
        Produtor p1 = new Produtor(fila, "API", 20, stats);
        Produtor p2 = new Produtor(fila, "WEB", 20, stats);
        Produtor p3 = new Produtor(fila, "MOBILE", 20, stats);

        // TODO: Criar ExecutorService para consumidores (5 threads)
        ExecutorService consumidores = null;
        consumidores = new Executors.newFixedThreadPool(5);
        // TODO: Criar 5 consumidores
        Consumidor c1 = new Consumidor(1, fila, estoque, stats);
        Consumidor c2 = new Consumidor(2, fila, estoque, stats);
        Consumidor c3 = new Consumidor(3, fila, estoque, stats);
        Consumidor c4 = new Consumidor(4, fila, estoque, stats);
        Consumidor c5 = new Consumidor(5, fila, estoque, stats);

        // TODO: Aguardar produtores finalizarem
        p1.wait();
        p2.wait();
        p3.wait();

        // TODO: Aguardar consumidores finalizarem

        c1.wait();
        c2.wait();
        c3.wait();
        c4.wait();
        c5.wait();


        // TODO: Parar monitor
        mon.parar();
        System.out.println("Monitor parado");
        
        // TODO: Exibir relatório final
        stats.exibirRelatorioFinal();
        
        // TODO: Exibir estoque final
        stats.exibirEstatisticas(fila.size());
        
        System.out.println("\nSistema finalizado com sucesso!");
    }
}