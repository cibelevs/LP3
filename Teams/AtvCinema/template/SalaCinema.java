class SalaCinema {
    private final GerenciadorAssentos gerenciadorAssentos;
    private final FilaReservas filaReservas;
    private final ProcessadorReservas processadorReservas;
    
    public SalaCinema(int totalAssentos) {
        this.gerenciadorAssentos = new GerenciadorAssentos(totalAssentos);
        this.filaReservas = new FilaReservas();
        this.processadorReservas = new ProcessadorReservas(gerenciadorAssentos, filaReservas);
    }
    
    public void solicitarReserva(String nomeUsuario, int assentoDesejado) {
        ReservaRequest request = new ReservaRequest(nomeUsuario, assentoDesejado);
        filaReservas.adicionar(request);
    }

    public void processarReservas() {
        processadorReservas.processar();
    }
    
    public int tamanhoFila() {
        return filaReservas.tamanho();
    }

    public void exibirStatus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STATUS DA SALA DE CINEMA");
        System.out.println("=".repeat(60));
        System.out.println("Total de assentos: " + gerenciadorAssentos.getTotalAssentos());
        System.out.println("Assentos reservados: " + gerenciadorAssentos.getAssentosReservados());
        System.out.println("Assentos disponíveis: " + gerenciadorAssentos.getAssentosDisponiveis());
        System.out.println("Pessoas na fila: " + filaReservas.tamanho());
        System.out.println("\nMapa de assentos: " + gerenciadorAssentos.getMapaAssentos());
        System.out.println("[ ] = Livre  [X] = Ocupado");
        System.out.println("=".repeat(60) + "\n");
    }
}