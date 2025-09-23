
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executa_Single{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            executor.execute(new MeuRunnable());
            executor.execute(new MeuRunnable());
            executor.execute(new MeuRunnable());
            Future<?> future = executor.submit(new MeuRunnable());
            System.out.println(future.isDone());
            executor.shutdown();
            executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);
            System.out.println(future.isDone());
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

   /** * public static class MeuCallabe implements Callable<String> {
        @Override
        public String call() throws Exception { 
            System.out.println("Executando a tarefa em uma thread separada.");
            String nome = Thread.currentThread().getName();
            System.out.println("Nome da thread: " + nome);
            return nome;
        }
    } 
    **/
}

    