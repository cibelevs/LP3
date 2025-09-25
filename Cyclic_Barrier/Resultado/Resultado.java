package Cyclic_Barrier.Resultado.java;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Resultado {
        private static BlockingQueue<Double> resultados = new LinkedBlockingQueue<>();
    //432*3 + 3^14 + 45*127/12 = ?
    public static void main(String[] args) {

        //resultado é uma quarta thread que so executa quando as outras tres terminarem
        Runnable finalizacao = () -> {
            System.out.println("Somando tudo.");
            double resultadoFinal = 0;
            resultadoFinal += resultados.poll(); //"pool" remove e retorna o head da fila, ou seja, o primeiro elemento
            resultadoFinal += resultados.poll();  
            resultadoFinal += resultados.poll();
            System.out.println("Processamento finalizado. O resultado final é : " + resultadoFinal);
        };

        //barreira que espera 3 threads + a thread de finalizacao
        CyclicBarrier cycleBarrier = new CyclicBarrier(3, finalizacao);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable r1 = () ->{
            resultados.add(432d*3d);
            await(cycleBarrier); //isso coloca na fila? 
            //sim, pq a thread so vai passar da barreira quando todas as outras 3 chegarem la
        };

        Runnable r2 = () ->{
            resultados.add(Math.pow(3, 14));
            await(cycleBarrier);
        };

        Runnable r3 = () ->{
            resultados.add(45d*127d/12d);
            await(cycleBarrier);
        };

        executor.submit(r1);
        executor.submit(r2);
        executor.submit(r3);
        executor.shutdown();
    }
        


    private static void await(CyclicBarrier cycleBarrier) {
            try {
                cycleBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
    }

        
}


