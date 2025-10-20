package ProdutoConsumidor;

public class Main {

    public static void main(String[] args) throws Exception{
        BlockingQueue<Pedido> fila = new LinkedBlockingQueue<>();
        Semaphore semaforo = new Semaphore(2);
        Estatistica stats = new Estatistica();

        Produtor web = new Produtor(fila, "Web", stats);
        Produtor mobile = new Produtor(fila, "Mobile", stats);
        Produtor api = new Produtor(fila, "Api", stats);


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
        stats.gerarEstatisticas();
        System.out.println("fim");
    }
}