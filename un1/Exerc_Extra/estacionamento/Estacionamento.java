package Exerc_Extra.estacionamento;

import java.util.ArrayList;

public class Estacionamento {
    private ArrayList<Vaga> vagas;


    public Estacionamento() {
        vagas = new ArrayList<>();
        // cria 10 vagas numeradas de 1 a 10
        for (int i = 1; i <= 10; i++) {
            vagas.add(new Vaga(i, false));
        }
    }

    public Vaga localizaVaga(int numero) {
        if (numero < 1 || numero > vagas.size()) return null;
        return vagas.get(numero - 1);
    }

    public synchronized String setVagaOcupada(int numero) {
        Vaga v = localizaVaga(numero);
        if (v == null) {
            return "Vaga inexistente.";
        }
        if (v.isOcupada()) {
            return "Vaga [" + numero + "] já está ocupada.";
        }
        v.setOcupada(true);
        return "Vaga [" + numero + "] reservada com sucesso.";
    }


    public void listar(){
        for(Vaga v : vagas){
            System.out.println("Vaga [" + v.getNumero() + "] " +
             "Ocupada: " + v.isOcupada());
        }
    }


}
