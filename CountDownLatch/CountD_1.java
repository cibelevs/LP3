package CountDownLatch;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CountD_1 {
    private static volatile int i = 0;
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        Runnable r1 = () -> {
            int j = new Random().nextInt();
            int x = i * j;
            System.out.println(i + " x " + j + " = " + x);
        };

        executor.scheduleAtFixedRate(r1, 0, 1, TimeUnit.SECONDS);
        while (true) {
            sleep(); 
            //atualiza o valor de i a cada segundo
            i = new Random().nextInt();
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
