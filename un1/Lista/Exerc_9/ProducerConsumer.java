package Lista.Exerc_9;

import java.util.concurrent.*;

/**
 * ProducerConsumer.java
 *
 * Implementa o padrão produtor-consumidor usando um buffer com capacidade limitada.
 * - Produtores colocam itens no buffer.
 * - Consumidores retiram itens do buffer.
 *
 * Usamos BlockingQueue (ArrayBlockingQueue) para garantir sincronização.
 */
public class ProducerConsumer {

    public static void main(String[] args) throws InterruptedException {
        final int BUFFER_CAPACITY = 5;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 3;

        BlockingQueue<String> buffer = new ArrayBlockingQueue<>(BUFFER_CAPACITY);

        ExecutorService exec = Executors.newFixedThreadPool(NUM_PRODUCERS + NUM_CONSUMERS);

        // produtores
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            final int pid = i + 1;
            exec.submit(() -> {
                for (int j = 0; j < 10; j++) {
                    String item = "P" + pid + "-item" + j;
                    try {
                        buffer.put(item); // bloqueia se estiver cheio
                        System.out.println("Producer " + pid + " produced: " + item);
                        Thread.sleep(200); // simula tempo de produção
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("Producer " + pid + " finished.");
            });
        }

        // consumidores
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            final int cid = i + 1;
            exec.submit(() -> {
                try {
                    while (true) {
                        String item = buffer.poll(3, TimeUnit.SECONDS); // espera item ou retorna null
                        if (item == null) {
                            // assume que não haverá mais produção após timeout
                            System.out.println("Consumer " + cid + " timeout, exiting.");
                            break;
                        }
                        System.out.println("Consumer " + cid + " consumed: " + item);
                        Thread.sleep(300); // simula tempo de consumo
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        exec.shutdown();
        exec.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("All done.");
    }
}

