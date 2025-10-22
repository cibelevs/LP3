class ProcessadorReservas {
    private final GerenciadorAssentos gerenciadorAssentos;
    private final FilaReservas filaReservas;
    
    public ProcessadorReservas(GerenciadorAssentos gerenciadorAssentos, 
                               FilaReservas filaReservas) {
        this.gerenciadorAssentos = gerenciadorAssentos;
        this.filaReservas = filaReservas;
    }
    
    public void processar() {
        System.out.println("\nIniciando processamento da fila de reservas...\n");
        
        while (!filaReservas.isEmpty()) {
            ReservaRequest request = filaReservas.proximaReserva();
            
            if (request != null) {
                System.out.println("Processando reserva de [" + 
                                   request.getNomeUsuario() + "]...");
                processarReserva(request);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Processamento interrompido!");
                    break;
                }
            }
        }
        
        System.out.println("\nFila processada completamente!\n");
    }
    
    private void processarReserva(ReservaRequest request) {
        String nome = request.getNomeUsuario();
        int assento = request.getNumeroAssento();
        
        if (gerenciadorAssentos.reservar(assento, nome)) {
            System.out.println("[" + nome + "] Assento " + assento + 
                               " reservado com sucesso!");
        } else {
            System.out.println("[" + nome + "] Assento " + assento + 
                               " já está ocupado! Tentando sugerir alternativa...");
            
            Integer alternativa = gerenciadorAssentos.sugerirAssentoProximo(assento);
            if (alternativa != null && gerenciadorAssentos.reservar(alternativa, nome)) {
                System.out.println("[" + nome + "] Assento " + alternativa + 
                                   " reservado como alternativa!");
            } else {
                System.out.println("[" + nome + "] Nenhum assento disponível!");
            }
        }
    }
}