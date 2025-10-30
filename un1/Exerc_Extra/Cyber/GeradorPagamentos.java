package Exerc_Extra.Cyber;

import java.sql.Timestamp;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

// Classe produtora que gera pagamentos
class GeradorPagamentos implements Runnable {
    private final PriorityBlockingQueue<Pagamento> fila;
    private final String fonte;
    private final AtomicInteger contadorId;
    private final String[] clientes = {"Cliente1", "Cliente2", "Cliente3", "Cliente4", "Cliente5"};
    private final Pagamento.TipoPagamento[] tipos = Pagamento.TipoPagamento.values();
    
    public GeradorPagamentos(PriorityBlockingQueue<Pagamento> fila, String fonte, AtomicInteger contadorId) {
        this.fila = fila;
        this.fonte = fonte;
        this.contadorId = contadorId;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < 20; i++) {
                // Gera pagamento aleatório
                int id = contadorId.incrementAndGet();
                String cliente = clientes[ThreadLocalRandom.current().nextInt(clientes.length)];
                double valor = ThreadLocalRandom.current().nextDouble(50, 5000);
                Pagamento.TipoPagamento tipo = tipos[ThreadLocalRandom.current().nextInt(tipos.length)];
                Timestamp tempo = new Timestamp(System.currentTimeMillis());
                
                Pagamento pagamento = new Pagamento(id, cliente, valor, tipo, tempo);
                
                // Insere na fila
                fila.put(pagamento);
                System.out.printf("[%s] Gerou pagamento ID: %d | Cliente: %s | Valor: R$ %.2f | Prioridade: %s\n",
                                 fonte, id, cliente, valor, pagamento.getPrioridade());
                
                // Simula atraso entre 100-300ms
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 301));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("[%s] Interrompido\n", fonte);
        }
    }
}
