package Executores_Threads.Multi;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/*
 * Executores (Executor)

    O que é: Uma forma de gerenciar threads sem criar manualmente 
    Thread t = new Thread(...).

    Quando usar: Quando precisa rodar várias tarefas em paralelo de 
    forma organizada, especialmente se for criar muitas threads.
    Ex: ExecutorService executor = Executors.newFixedThreadPool(5);""
 */

public class MultiThreads {
    public static void main(String[] args) {
        ExecutorService executor = null;

        
        System.out.println("Implementação de múltiplas threads.");
        
        try{
            executor = Executors.newSingleThreadExecutor();
            // se nao quer definir a qtd de threads, use o newCachedThreadPool()
            // executor = Executors.newCachedThreadPool();
            // se quer definir a qtd de threads, use o newFixedThreadPool(qtd)
            // executor = Executors.newFixedThreadPool(3);
            Future<String> f1 = executor.submit(new Tarefa());
            Future<String> f2 = executor.submit(new Tarefa());
            Future<String> f3 = executor.submit(new Tarefa());
            Future<String> f4 = executor.submit(new Tarefa());
            Future<String> f5 = executor.submit(new Tarefa());
            Future<String> f6 = executor.submit(new Tarefa());

            System.out.println(f1.get());
            System.out.println(f2.get());
            System.out.println(f3.get());
            System.out.println(f4.get());
            System.out.println(f5.get());
            System.out.println(f6.get());
            List<Future<String>> futures = List.of(f1, f2, f3, f4, f5, f6);
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
            executor.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    public static class Tarefa implements java.util.concurrent.Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(2000);
            return "Tarefa concluída pela thread: " + Thread.currentThread().getName();
        }
    }










}
