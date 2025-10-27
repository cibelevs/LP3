package Exerc_Extra.entrega;

public class Monitoramento implements Runnable {
    private final FilaEntregas fila;
    private final AtomicInteger realizadas;
    private final AtomicInteger pendentes;

    public Monitoramento(FilaEntregas fila, AtomicInteger realizadas, AtomicInteger pendentes) {
        this.fila = fila;
        this.realizadas = realizadas;
        this.pendentes = pendentes;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.printf(
                    "%n[MONITOR] Fila: %d | Realizadas: %d | Pendentes: %d%n",
                    fila.tamanho(), realizadas.get(), pendentes.get()
                );
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            System.out.println("[MONITOR] Encerrado.");
        }
    }
}

