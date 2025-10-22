import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class FilaReservas {
    private final BlockingQueue<ReservaRequest> fila;
    
    public FilaReservas() {
        this.fila = new LinkedBlockingQueue<>();
    }
    
    public boolean adicionar(ReservaRequest request) {
        try {
            fila.put(request);
            System.out.println("[" + request.getNomeUsuario() + 
                               "] Entrou na fila (Posição: " + fila.size() + 
                               ") - Assento desejado: " + request.getNumeroAssento());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + request.getNomeUsuario() + 
                               "] Erro ao entrar na fila!");
            return false;
        }
    }
    
    public ReservaRequest proximaReserva() {
        try {
            return fila.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    public boolean isEmpty() {
        return fila.isEmpty();
    }
    
    public int tamanho() {
        return fila.size();
    }
}