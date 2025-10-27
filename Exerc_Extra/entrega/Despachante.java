package Exerc_Extra.entrega;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Despachante implements Runnable {
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
                Entrega entrega = fila.getFila().poll();
                if (entrega == null) break;

                System.out.println("[" + nome + "] Processando: " + entrega);
                if (veiculos.reservarVeiculo()) {
                    Thread.sleep(random.nextInt(250) + 150);
                    realizadas.incrementAndGet();
                    System.out.println("[" + nome + "] Concluída: " + entrega);
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

