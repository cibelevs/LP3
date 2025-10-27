
import java.util.concurrent.atomic.AtomicBoolean;

public class Ingresso {

    private int numeroLugar;
    private AtomicBoolean disponivel;
   

    public Ingresso(int numeroLugar){

        this.numeroLugar = numeroLugar;
        this.disponivel = new AtomicBoolean(true);
    }

    public int getNumeroLugar() {
        return numeroLugar;
    }

    public AtomicBoolean getDisponivel() {
        return disponivel;
    }
    
}
