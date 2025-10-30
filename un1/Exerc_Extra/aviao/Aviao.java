package Exerc_Extra.aviao;

public class Aviao {
    private Cadeira[] cadeiras;

    public Aviao() {
        cadeiras = new Cadeira[10];
        for (int i = 0; i < 10; i++) {
            cadeiras[i] = new Cadeira(i + 1, false);
        }
    }

    public Cadeira getCadeira(int numero) {
        if (numero < 1 || numero > 10) return null;
        return cadeiras[numero - 1];
    }

    public Cadeira localizaCadeira(int numero) {
        return getCadeira(numero);
    }

    public String setCadeiraParaOcupada(int numero) {
        Cadeira c = localizaCadeira(numero);
        if (c == null) return "Cadeira inválida.";
        if (c.isOcupada()) return "Cadeira já reservada.";
        c.setOcupada(true);
        return c.toString();
    }
}
