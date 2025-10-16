import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Produtor implements Runnable {
    private final BlockingQueue<Pedido> fila;
    private final String fonte; // API, Web, Mobile
    private final int quantidadePedidos;
    private final GerenciadorEstatisticas stats;
    private final Random random = new Random();

    private static final String[] CLIENTES = {"João Silva", "Maria Santos", "Pedro Costa", "Ana Oliveira", "Carlos Souza"};
    private static final String[] PRODUTOS = {"Notebook", "Mouse", "Teclado", "Monitor", "Headset"};
    private static final PrioridadePedido[] PRIORIDADES = PrioridadePedido.values();

    public Produtor(BlockingQueue<Pedido> fila, String fonte, int quantidadePedidos, GerenciadorEstatisticas stats) {
        this.fila = fila;
        this.fonte = fonte;
        this.quantidadePedidos = quantidadePedidos;
        this.stats = stats;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + fonte + "] Iniciando geração de " + quantidadePedidos + " pedidos");

            for (int i = 0; i < quantidadePedidos; i++) {
                Pedido novoPedido = gerarPedidoAleatorio();
                fila.put(novoPedido);
                stats.registrarPedidoGerado();

                System.out.printf("[%s] Gerou: %s%n", fonte, novoPedido.toString());
                Thread.sleep(random.nextInt(151) + 50); // 50–200ms entre pedidos
            }

            System.out.println("[" + fonte + "] Finalizou geração de pedidos");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Pedido gerarPedidoAleatorio() {
        String cliente = CLIENTES[random.nextInt(CLIENTES.length)];
        String produto = PRODUTOS[random.nextInt(PRODUTOS.length)];
        int quantidade = random.nextInt(5) + 1; // 1–5 unidades
        PrioridadePedido prioridade = PRIORIDADES[random.nextInt(PRIORIDADES.length)];

        return new Pedido(cliente, produto, quantidade, prioridade);
    }
}