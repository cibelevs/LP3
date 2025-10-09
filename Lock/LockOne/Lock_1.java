package Lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//em sychronized vc precisa bloquear um metodo completo 
// lock apresenta vantagens 

public class Lock_1 {
    private static int i = -1;
    private static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable r1 = () -> {
            lock.lock();
            //boolean conseguiu = lock.tryLock();
            //boolean conseguiu = lock.tryLock(1, TimeUnit.SECONDS);
            String name = Thread.currentThread().getName();
            i++;
            System.out.println(name + " lendo o incremento " + i);
            lock.unlock();
        };
        for (int i = 0; i < 6; i++) {
            executor.execute(r1);
        }
        executor.shutdown();
    }


}