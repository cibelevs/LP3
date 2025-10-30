
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class Recepcao {

    public static void main(String[] args) throws InterruptedException {

        SalaCinema primeiraSala = new SalaCinema();

        ExecutorService executor = Executors.newFixedThreadPool(30);


        for (int i = 0; i < 10; i++) {
            executor.execute(new Cliente("Jtinho" + i, primeiraSala));
            
        }
        executor.shutdown();

        if(!executor.awaitTermination(50, TimeUnit.SECONDS)){
            executor.shutdownNow(); 
        }

}
    
}
