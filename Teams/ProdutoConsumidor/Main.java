package ProdutoConsumidor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.*;


public class Main {

    public static void main(String[] args) throws Exception{
        BlockingQueue<Pedido> fila = new LinkedBlockingQueue<>();
        Semaphore semaforo = new Semaphore(2);
        Estatisticas stats = new Estatisticas();

        Produtor web = new Produtor("Web",fila, stats);
        Produtor api = new Produtor("Api",fila, stats);
        Produtor mobile = new Produtor("Mobile",fila, stats);
                


        Thread tweb = new Thread(web);
        Thread tmobile = new Thread(mobile);
        Thread tapi = new Thread(api);

        Thread c1 = new Thread(new Consumidor(fila, semaforo, stats), "Consumidor 1");
        Thread c2 = new Thread(new Consumidor(fila, semaforo, stats), "Consumidor 2");

        tmobile.start();
        tapi.start();
        tweb.start();
        c1.start();
        c2.start();

        Thread.sleep(10000);
        web.parar();
        api.parar();
        mobile.parar();
        Thread.sleep(4000);

        c1.interrupt();
        c2.interrupt();
        stats.imprimeStats();
        System.out.println("fim");
    }
}