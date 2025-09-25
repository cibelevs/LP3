package Cyclic_Barrier.ResultRestart;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ResRestLoop {
    private static BlockingQueue<Double> resultados = new LinkedBlockingQueue<>();
    private static ExecutorService executor = null;
    private static Runnable r1 = null;
    private static Runnable r2 = null;
    private static Runnable r3 = null;
    private static double resultadoFinal = 0;
    //432*3 + 3^14 + 45*127/12 = ?
    public static void main(String[] args) {
        Runnable sumarizacao = () -> {
            System.out.println("Somando tudo.");
            resultadoFinal += resultados.poll();
            resultadoFinal += resultados.poll();
            resultadoFinal += resultados.poll();
            System.out.println("Processamento finalizado. O resultado final é : " + resultadoFinal);
            System.out.println("--------------------------------");
            // restart();
        };
        CyclicBarrier cycleBarrier = new CyclicBarrier(3, sumarizacao);
        executor = Executors.newFixedThreadPool(3);

        r1 = () ->{
            while (true) {
                resultados.add(432d*3d);
                await(cycleBarrier);
                sleep();
            }
        };
        r2 = () ->{
            while (true) {
                resultados.add(Math.pow(3, 14));
                await(cycleBarrier);
                sleep();
            }
        };
        r3 = () ->{
            while (true) {
                resultados.add(45d*127d/12d);
                await(cycleBarrier);
                sleep();
            }
        };

        restart();
    }

    


    private static void await(CyclicBarrier cycleBarrier) {
        try {
            cycleBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private static void restart(){
        sleep();
        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);
    
    }
    
    private static void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}