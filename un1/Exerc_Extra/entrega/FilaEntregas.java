package Exerc_Extra.entrega;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

// ------------------------------------------------------------
// CLASSE FilaEntregas
// Controla a fila de entregas usando PriorityBlockingQueue.
// As entregas são ordenadas por prioridade e tempo de criação.
// ------------------------------------------------------------
class FilaEntregas {
    private final PriorityBlockingQueue<Entrega> fila;

    public FilaEntregas() {
        // Cria a fila com capacidade inicial de 30 e um comparador customizado
        fila = new PriorityBlockingQueue<>(30, new ComparadorEntregas());
    }

    public void adicionarEntrega(Entrega e) throws InterruptedException {
        fila.put(e); // adiciona e bloqueia se estiver cheia
    }

    public Entrega retirarEntrega() throws InterruptedException {
        return fila.take(); // retira e bloqueia se estiver vazia
    }

    public int tamanho() {
        return fila.size();
    }

    public PriorityBlockingQueue<Entrega> getFila() {
        return fila;
    }

    // Classe interna: define como comparar duas entregas
    private static class ComparadorEntregas implements Comparator<Entrega> {
        @Override
        public int compare(Entrega e1, Entrega e2) {
            // Menor nível = maior prioridade
            int compPrioridade = Integer.compare(e1.getPrioridade().getNivel(), e2.getPrioridade().getNivel());
            if (compPrioridade != 0) return compPrioridade;
            // Se tiver mesma prioridade, a mais antiga vem primeiro
            return e1.getTimestamp().compareTo(e2.getTimestamp());
        }
    }
}