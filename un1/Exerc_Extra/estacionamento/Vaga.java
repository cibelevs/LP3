package Exerc_Extra.estacionamento;

public class Vaga{

    private int numero;
    private boolean ocupada;


    public Vaga(int numero, boolean ocupada) {
        this.numero = numero;
        this.ocupada = ocupada;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    @Override
    public String toString() {
        return "Vaga [numero=" + numero + ", ocupada=" + ocupada + "]";
    }


}