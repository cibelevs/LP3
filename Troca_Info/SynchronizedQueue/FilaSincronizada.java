package Troca_Info.SynchronizedQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class FilaSincronizada {
    private static final SynchronousQueue<String> fila = new SynchronousQueue<>();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable r1 = () -> {
            put();
            System.out.println("Escreveu na fila!  " + getNameThread());
        };
        Runnable r2 = () -> {
            String msg = take();
            System.out.println("Pegou da fila! " + msg + "   " + getNameThread());
        };
        /*esse metodo só para a execução se alguem for ler na fila
         * trocando informação entre threads 
         */
        executor.execute(r1);
        executor.execute(r2);
        executor.shutdown();
    }
    private static String take() {
        try {
            return fila.take();
            // return fila.poll(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return "Exceção!";
        }
    }

    private static void put() {
        try {
            fila.put("LP-III");
        // fila.offer(e, timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
        
    public static String getNameThread(){
        return Thread.currentThread().getName();
    }
}
