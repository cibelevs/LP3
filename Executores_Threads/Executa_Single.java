
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executa_Single{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            executor.execute(new MeuRunnable());
            executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        }      
         catch (Exception e) {
            throw e;
        } finally{
            if (executor != null) {
                executor.shutdown();
            }
        }
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

    