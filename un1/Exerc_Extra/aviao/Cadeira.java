package Exerc_Extra.aviao;

public class Cadeira {
    private int numero;
    private boolean fumante;
    private boolean ocupada;

    public Cadeira(int numero, boolean fumante) {
        this.numero = numero;
        this.fumante = fumante;
        this.ocupada = false;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isFumante() {
        return fumante;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    @Override
    public String toString() {
        return "Numero=" + numero + 
               " fumante=" + fumante + 
               " Ocupada=" + ocupada;
    }
}
