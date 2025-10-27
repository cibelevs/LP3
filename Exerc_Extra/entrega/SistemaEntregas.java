package Exerc_Extra.entrega;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// ------------------------------------------------------------
// CLASSE PRINCIPAL SistemaEntregas
// Coordena todas as threads, fila, veículos e estatísticas.
// ------------------------------------------------------------
public class SistemaEntregas {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=======================================");
        System.out.println("     SISTEMA DE ENTREGAS CONCORRENTE   ");
        System.out.println("=======================================\n");

        // Criação dos componentes principais
        FilaEntregas fila = new FilaEntregas();
        GerenciadorVeiculos veiculos = new GerenciadorVeiculos();
        AtomicInteger realizadas = new AtomicInteger(0);
        AtomicInteger pendentes = new AtomicInteger(0);

        // Criação dos pools de threads
        ExecutorService produtores = Executors.newFixedThreadPool(3);
        ExecutorService consumidores = Executors.newFixedThreadPool(4);

        // Inicia thread de monitoramento
        Thread monitor = new Thread(new Monitoramento(fila, realizadas, pendentes));
        monitor.start();

        // Inicia os produtores (3 fontes diferentes)
        produtores.submit(new GeradorPedidos("App", fila, 15));
        produtores.submit(new GeradorPedidos("E-commerce", fila, 15));
        produtores.submit(new GeradorPedidos("Corporativo", fila, 15));
        produtores.shutdown(); // não aceita novas tarefas
        produtores.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nTodos os produtores finalizaram!\n");

        // Inicia consumidores (4 despachantes)
        for (int i = 1; i <= 4; i++) {
            consumidores.submit(new Despachante("Despachante-" + i, fila, veiculos, realizadas, pendentes));
        }
        consumidores.shutdown();
        consumidores.awaitTermination(15, TimeUnit.SECONDS);

        // Encerra o monitoramento
        monitor.interrupt();

        // Mostra relatório final
        veiculos.exibirEstado();
        System.out.println("\n========= RELATÓRIO FINAL =========");
        System.out.println("Total gerado      : 45");
        System.out.println("Entregas feitas   : " + realizadas.get());
        System.out.println("Pendentes         : " + pendentes.get());
        System.out.println("Taxa de sucesso   : " +
                (realizadas.get() * 100 / 45.0) + "%");
        System.out.println("===================================");
    }
}