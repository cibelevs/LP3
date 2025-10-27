package Exerc_Extra.entrega;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SistemaEntregas {
    public static void main(String[] args) throws InterruptedException {
        FilaEntregas fila = new FilaEntregas();
        GerenciadorVeiculos veiculos = new GerenciadorVeiculos();
        AtomicInteger realizadas = new AtomicInteger(0);
        AtomicInteger pendentes = new AtomicInteger(0);

        ExecutorService produtores = Executors.newFixedThreadPool(3);
        ExecutorService consumidores = Executors.newFixedThreadPool(4);

        // Monitor
        Thread monitor = new Thread(new Monitoramento(fila, realizadas, pendentes));
        monitor.start();

        // Inicia produtores
        produtores.submit(new GeradorPedidos("App", fila, 15));
        produtores.submit(new GeradorPedidos("E-commerce", fila, 15));
        produtores.submit(new GeradorPedidos("Corporativo", fila, 15));
        produtores.shutdown();
        produtores.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nTodos os produtores finalizaram!");

        // Inicia consumidores
        for (int i = 1; i <= 4; i++) {
            consumidores.submit(new Despachante("Despachante-" + i, fila, veiculos, realizadas, pendentes));
        }
        consumidores.shutdown();
        consumidores.awaitTermination(10, TimeUnit.SECONDS);

        monitor.interrupt();
        veiculos.exibirEstado();

        System.out.println("\n========= RELATÓRIO FINAL =========");
        System.out.println("Total gerado      : 45");
        System.out.println("Entregas feitas   : " + realizadas.get());
        System.out.println("Pendentes         : " + pendentes.get());
        System.out.println("===================================");
    }
}

