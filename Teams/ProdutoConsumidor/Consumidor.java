package ProdutoConsumidor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Consumidor implements Runnable{
    private BlockingQueue<Pedido> filaP;
    private Semaphore semaforo;
    private Estatisticas estats;


    public Consumidor(BlockingQueue filaP, Semaphore sem1, Estatisticas estats) {
        this.filaP = filaP;
        this.semaforo = sem1;
        this.estats = estats;
    }

    @Override
    public void run(){
        try {
            while (true) { 
                semaforo.acquire();
                Pedido pedido = filaP.take();


                System.out.println(Thread.currentThread().getName() + " processando ");
                Thread.sleep(2000); // simula tempo de processamento
                estats.registraGeral();

                System.out.println(Thread.currentThread().getName() + " finalizou o pedido ");
                semaforo.release(); // libera o slot
            }
        } catch (Exception e) {
            System.err.println("Thread interrompida " + Thread.currentThread().getName());
        }
    }
    
}
