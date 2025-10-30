import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProdutoConsumidor {

    private static final int TAMANHO_MAXIMO = 5;
    private static final Queue<Integer> fila = new LinkedList<>();

    public static void main(String[] args) {
        Thread produtor = new Thread(new Produtor(), "Produtor");
        Thread consumidor = new Thread(new Consumidor(), "Consumidor");

        produtor.start();
        consumidor.start();
    }

    static class Produtor implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                synchronized (fila) {
                    while (fila.size() == TAMANHO_MAXIMO) {
                        try {
                            System.out.println("Fila cheia. Produtor aguardando...");
                            fila.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    int valor = random.nextInt(1000);
                    fila.add(valor);
                    System.out.println("Produziu: " + valor + " | Tamanho da fila: " + fila.size());
                    fila.notifyAll();
                }
                simulaProcessamento();
            }
        }
    }

    static class Consumidor implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (fila) {
                    while (fila.isEmpty()) {
                        try {
                            System.out.println("Fila vazia. Consumidor aguardando...");
                            fila.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    int valor = fila.poll();
                    System.out.println("Consumiu: " + valor + " | Tamanho da fila: " + fila.size());
                    fila.notifyAll();
                }
                simulaProcessamento();
            }
        }
    }

    private static void simulaProcessamento() {
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
