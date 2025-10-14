import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

class Pedido implements Comparable<Pedido> {
    private static final AtomicInteger contadorId = new AtomicInteger(0);
    
    private final int id;
    private final String cliente;
    private final String produto;
    private final int quantidade;
    private final PrioridadePedido prioridade;
    private final LocalDateTime timestamp;
    
    public Pedido(String cliente, String produto, int quantidade, PrioridadePedido prioridade) {
        this.id = contadorId.incrementAndGet();
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.prioridade = prioridade;
        this.timestamp = LocalDateTime.now();
    }
    
    // TODO: Implementar compareTo para ordenar por prioridade e timestamp
    @Override
    public int compareTo(Pedido outro) {
        // Implementar comparação
        int compara = Integer.compare(this.prioridade.getValor(), outro.prioridade.getValor());
        if(compara != 0) return 0;
        return this.timestamp.compareTo(outro.getTimestamp());
    }
    
    // Getters
    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public String getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public PrioridadePedido getPrioridade() { return prioridade; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("Pedido#%d [%s] %s - %s x%d", 
            id, prioridade, cliente, produto, quantidade);
    }
}