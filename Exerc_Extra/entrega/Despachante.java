package Exerc_Extra.entrega;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// ------------------------------------------------------------
// CLASSE Despachante (CONSUMIDOR)
// Retira entregas da fila, verifica veículos e simula despacho.
// ------------------------------------------------------------
class Despachante implements Runnable {
    private final String nome;
    private final FilaEntregas fila;
    private final GerenciadorVeiculos veiculos;
    private final AtomicInteger realizadas;
    private final AtomicInteger pendentes;
    private final Random random = new Random();

    public Despachante(String nome, FilaEntregas fila, GerenciadorVeiculos veiculos,
                       AtomicInteger realizadas, AtomicInteger pendentes) {
        this.nome = nome;
        this.fila = fila;
        this.veiculos = veiculos;
        this.realizadas = realizadas;
        this.pendentes = pendentes;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Usa poll() com timeout para detectar fim da fila
                Entrega entrega = fila.getFila().poll(2, TimeUnit.SECONDS);
                if (entrega == null) break;

                System.out.println("[" + nome + "] Processando: " + entrega);

                // Se houver veículo disponível
                if (veiculos.reservarVeiculo()) {
                    Thread.sleep(random.nextInt(250) + 150); // simula tempo de entrega
                    realizadas.incrementAndGet();
                    System.out.println("[" + nome + "] CONCLUÍDA: " + entrega);
                    veiculos.liberarVeiculo("Moto");
                } else {
                    System.out.println("[" + nome + "] SEM VEÍCULO: " + entrega);
                    pendentes.incrementAndGet();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[" + nome + "] Finalizou processamento.");
    }
}