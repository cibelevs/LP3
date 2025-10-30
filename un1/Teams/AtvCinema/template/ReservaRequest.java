class ReservaRequest {
    private final String nomeUsuario;
    private final int numeroAssento;
    private final long timestamp;
    
    public ReservaRequest(String nomeUsuario, int numeroAssento) {
        this.nomeUsuario = nomeUsuario;
        this.numeroAssento = numeroAssento;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public int getNumeroAssento() {
        return numeroAssento;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "Reserva{usuario='" + nomeUsuario + "', assento=" + numeroAssento + "}";
    }
}