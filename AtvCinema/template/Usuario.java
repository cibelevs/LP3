import java.util.Random;

class Usuario implements Runnable {
    private final String nome;
    private final SalaCinema sala;
    private final int assentoDesejado;
    
    public Usuario(String nome, SalaCinema sala, int assentoDesejado) {
        this.nome = nome;
        this.sala = sala;
        this.assentoDesejado = assentoDesejado;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sala.solicitarReserva(nome, assentoDesejado);
    }
}
