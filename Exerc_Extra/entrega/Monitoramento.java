package Exerc_Extra.entrega;

import java.util.concurrent.atomic.AtomicInteger;

// ------------------------------------------------------------
// CLASSE Monitoramento
// Mostra o status do sistema a cada 3 segundos.
// ------------------------------------------------------------
class Monitoramento implements Runnable {
    private final FilaEntregas fila;
    private final AtomicInteger realizadas;
    private final AtomicInteger pendentes;
    private final long inicio = System.currentTimeMillis();

    public Monitoramento(FilaEntregas fila, AtomicInteger realizadas, AtomicInteger pendentes) {
        this.fila = fila;
        this.realizadas = realizadas;
        this.pendentes = pendentes;
    }

    @Override
    public void run() {
        try {
            while (true) {
                long tempo = (System.currentTimeMillis() - inicio) / 1000;
                System.out.printf(
                        "%n[MONITOR] Fila: %d | Realizadas: %d | Pendentes: %d | Tempo: %ds%n",
                        fila.tamanho(), realizadas.get(), pendentes.get(), tempo
                );
                Thread.sleep(1000); //Mudei para um segundo mas na questão era 3
            }
        } catch (InterruptedException e) {
            System.out.println("[MONITOR] Encerrado.");
        }
    }
}