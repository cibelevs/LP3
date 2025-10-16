import java.util.concurrent.atomic.AtomicInteger;

class GerenciadorEstatisticas {
    private final AtomicInteger pedidosGerados = new AtomicInteger(0);
    private final AtomicInteger pedidosProcessados = new AtomicInteger(0);
    private final AtomicInteger pedidosRejeitados = new AtomicInteger(0);
    private final long tempoInicio;
    
    public GerenciadorEstatisticas() {
        this.tempoInicio = System.currentTimeMillis();
    }
    
    public void registrarPedidoGerado() {
        pedidosGerados.incrementAndGet();
    }
    
    public void registrarPedidoProcessado() {
        pedidosProcessados.incrementAndGet();
    }
    
    public void registrarPedidoRejeitado() {
        pedidosRejeitados.incrementAndGet();
    }
    
    // Métodos para compatibilidade com código existente
    public void incrementarGerados() {
        registrarPedidoGerado();
    }
    
    public void incrementarProcessados() {
        registrarPedidoProcessado();
    }
    
    public void incrementarRejeitados() {
        registrarPedidoRejeitado();
    }
    
    public void exibirEstatisticas(int tamanhoFila) {
        long tempoDecorrido = (System.currentTimeMillis() - tempoInicio) / 1000;
        double taxaProcessamento = tempoDecorrido > 0 ? 
            (pedidosProcessados.get() + pedidosRejeitados.get()) / (double)tempoDecorrido : 0.0;
            
        System.out.println("\nMONITORAMENTO EM TEMPO REAL");
        System.out.println("Fila atual    : " + tamanhoFila + " pedidos");
        System.out.println("Pedidos gerados : " + pedidosGerados.get());
        System.out.println("Processados    : " + (pedidosProcessados.get() + pedidosRejeitados.get()));
        System.out.println("Rejeitados    : " + pedidosRejeitados.get());
        System.out.printf("Taxa processamento: %.2f ped/s\n", taxaProcessamento);
        System.out.println("Tempo decorrido  : " + tempoDecorrido + "s");
    }
    
    public void exibirRelatorioFinal() {
        long tempoTotal = System.currentTimeMillis() - tempoInicio;
        double taxaMedia = tempoTotal > 0 ? 
            (pedidosProcessados.get() + pedidosRejeitados.get()) / (tempoTotal / 1000.0) : 0.0;
        double taxaSucesso = pedidosGerados.get() > 0 ? 
            (pedidosProcessados.get() * 100.0) / pedidosGerados.get() : 0.0;
            
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          RELATÓRIO FINAL               ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Total gerado          : %3d pedidos%n", pedidosGerados.get());
        System.out.printf("║ Processados com sucesso: %3d pedidos%n", pedidosProcessados.get());
        System.out.printf("║ Rejeitados (sem estoque): %3d pedidos%n", pedidosRejeitados.get());
        System.out.printf("║ Total tratado         : %3d pedidos%n", 
            pedidosProcessados.get() + pedidosRejeitados.get());
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Taxa de sucesso       : %.1f%%%n", taxaSucesso);
        System.out.printf("║ Taxa média           : %.2f ped/s%n", taxaMedia);
        System.out.printf("║ Tempo total execução : %.1fs%n", tempoTotal / 1000.0);
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    // Getters para o sistema principal
    public int getPedidosGerados() { return pedidosGerados.get(); }
    public int getPedidosProcessados() { return pedidosProcessados.get(); }
    public int getPedidosRejeitados() { return pedidosRejeitados.get(); }
}