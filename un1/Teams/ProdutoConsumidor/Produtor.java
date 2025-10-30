package ProdutoConsumidor;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Produtor implements Runnable {
    private String[] fonte = {"Web", "API", "Mobile"};
    private String origem;
    private String[] clientes = {"Alexander", "Eliza", "Burr", "Sabrina"};
    private BlockingQueue fila;
    private boolean ativo = true;
    private Estatisticas stats;
    private Random random = new Random();




    public Produtor(String fonte, BlockingQueue fila, Estatisticas stats) {
        this.origem = fonte;
        this.fila = fila;
        this.stats = stats;
    }

    @Override
    public void run(){
        try {
            while(ativo){
                String org = fonte[random.nextInt(3)];
                int id = random.nextInt(50);
                String cliente = clientes[random.nextInt(4)];
                Pedido p = new Pedido(id, cliente, 0, org);
                fila.add(p);
                stats.registraGeral();
                stats.verificaTipo(org);
                System.out.println("[" + org +"] gerou um pedido");
                Thread.sleep(1000+ random.nextInt(3000));
            }
            
        } catch (Exception e) {
            System.err.println("Thread interrompida " + Thread.currentThread().getName());
        }
    }

    public void parar(){
        ativo = false;
    }


}
