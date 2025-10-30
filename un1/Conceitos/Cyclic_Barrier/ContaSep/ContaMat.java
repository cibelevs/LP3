package Cyclic_Barrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContaMat {

    public static void main(String[] args) {
        CyclicBarrier cycleBarrier = new CyclicBarrier(3);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        ///432*3 + 3^14 + 45*127/12 = ?
        
        Runnable r1 = ()  -> {
            int a = 432 * 3;
            System.out.println("432*3 = " + a);
            await(cycleBarrier);
            System.out.println(" Thread" + Thread.currentThread().getName() +" terminou execução");
        };

        Runnable r2 = ()  -> {
            int b = (int) Math.pow(3, 14);
            System.out.println("3^14 = " + b);
            await(cycleBarrier);
            System.out.println(" Thread" + Thread.currentThread().getName() +" terminou execução");
        };

        Runnable r3 = ()  -> {
            int c = 45 * 127 / 12;
            System.out.println("45*127/12 = " + c);
            await(cycleBarrier);
            System.out.println(" Thread" + Thread.currentThread().getName() +" terminou execução");
        };

        executor.execute(r1);
        executor.execute(r2);
        executor.execute(r3);
        executor.shutdown();
        while (!executor.isTerminated()) {
            // espera todas as threads terminarem
        }
        System.out.println("Todas as threads terminaram");

    }

    private static void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   


    
}
