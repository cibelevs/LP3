package Exerc_Extra.entrega;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class FilaEntregas {
    private PriorityBlockingQueue<Entrega> fila;

    public FilaEntregas() {
        fila = new PriorityBlockingQueue<>(30, new ComparadorEntregas());
    }

    public void adicionarEntrega(Entrega e) throws InterruptedException {
        fila.put(e);
    }

    public Entrega retirarEntrega() throws InterruptedException {
        return fila.take();
    }

    public int tamanho() {
        return fila.size();
    }

    public PriorityBlockingQueue<Entrega> getFila() {
        return fila;
    }
   
    private static class ComparadorEntregas implements Comparator<Entrega> {
        @Override
        public int compare(Entrega e1, Entrega e2) {
            int compPrioridade = Integer.compare(e1.getPrioridade().getNivel(), e2.getPrioridade().getNivel());
            if (compPrioridade != 0) return compPrioridade;
            return e1.getTempoCriação().compareTo(e2.getTempoCriação());
        }
    }
}
