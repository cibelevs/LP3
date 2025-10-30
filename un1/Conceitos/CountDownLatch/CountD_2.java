package CountDownLatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CountD_2 {
            private static volatile int i = 0;
            private static CountDownLatch latch = new CountDownLatch(3);
        public static void main(String[] args) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
            Runnable r1 = () -> {
                int j = new Random().nextInt();
                int x = i * j;
                System.out.println(i + " x " + j + " = " + x);
                latch.countDown();
            };
            Runnable r2 = () -> {
                await();
                i = new Random().nextInt(100);
            };
            Runnable r3 = () -> {
                await();
                latch = new CountDownLatch(3);
            };
    
            Runnable r4 = () -> {
                await();
                System.out.println("Terminei a execuçã o! Vamos Começar Novamente");
            };

            executor.scheduleAtFixedRate(r1, 0, 1, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(r2, 0, 1, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(r3, 0, 1, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(r4, 0, 1, TimeUnit.SECONDS);
        }

        public static void await(){
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
                


