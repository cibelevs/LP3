package Semaforos;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Semáforo (Semaphore)

    O que é: Controla quantas threads podem acessar um 
    recurso ao mesmo tempo.

    Quando usar: Se só N threads podem usar um recurso 
    simultaneamente (ex: impressora, banco de dados).
 */

public class Semaphore1 {
    
    private static final Semaphore semaphore = new Semaphore(3);
    private static AtomicInteger qtd = new AtomicInteger(0);
    public static void main(String[] args) {

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(501);
        Runnable r1 = () -> {
            String name = Thread.currentThread().getName();
            int usuario = new Random().nextInt(10000);
            boolean conseguiu = false;
            qtd.incrementAndGet();
            while(!conseguiu){
            conseguiu = tryAcquire();
            }
            qtd.decrementAndGet();
            System.out.println("Usuário " + usuario + " matriculou-se em LP-III! " + " Usando a thread " + name);
            sleep();
            semaphore.release();
        };

        Runnable r2 = () -> {
            System.out.println(qtd.get());
        };
        for (int i = 0; i < 500; i++) {
            executor.execute(r1);
        }
            executor.scheduleAtFixedRate(r2, 0, 100, TimeUnit.MILLISECONDS);
    }


    public static void sleep(){
        try {
            int tempoEspera = new Random().nextInt(6);
            tempoEspera++;
            Thread.sleep(1000 * tempoEspera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            e.printStackTrace();
        }
    }
        

    public static boolean tryAcquire(){
        try {
            return semaphore.tryAcquire(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
           return false;
        }
    }
        
    
}


   
    
    