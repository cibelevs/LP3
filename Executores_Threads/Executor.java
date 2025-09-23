package Executores_Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new MeuRunnable());
        executor.shutdown();
    }        

    public static class MeuRunnable implements Runnable {
        @Override
        public void run() { 
            System.out.println("Executando a tarefa em uma thread separada.");
            String nome = Thread.currentThread().getName();
            System.out.println("Nome da thread: " + nome);
        }
    }
}
