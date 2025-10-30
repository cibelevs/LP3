package Exerc_Extra.entrega;

import java.util.Random;

// ------------------------------------------------------------
// CLASSE GeradorPedidos (PRODUTOR)
// Cria entregas aleatórias e adiciona à fila.
// Representa uma "fonte" (App, E-commerce, Corporativo).
// ------------------------------------------------------------
class GeradorPedidos implements Runnable {
    private final String nomeFonte;
    private final FilaEntregas fila;
    private final int total;
    private static int contador = 1;
    private final Random random = new Random();

    public GeradorPedidos(String nomeFonte, FilaEntregas fila, int total) {
        this.nomeFonte = nomeFonte;
        this.fila = fila;
        this.total = total;
    }

    @Override
    public void run() {
        System.out.println("[" + nomeFonte + "] Iniciando geração de " + total + " entregas...");
        try {
            for (int i = 0; i < total; i++) {
                // Cria entrega com dados aleatórios
                Entrega e = new Entrega(
                        contador++,
                        gerarCliente(),
                        "Rua " + (char) ('A' + random.nextInt(5)),
                        random.nextInt(20) + 1,
                        Prioridade.values()[random.nextInt(Prioridade.values().length)]
                );

                fila.adicionarEntrega(e);
                System.out.println("[" + nomeFonte + "] Gerou: " + e);

                // Simula atraso entre as gerações
                Thread.sleep(random.nextInt(150) + 100);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[" + nomeFonte + "] Finalizou geração de entregas.");
    }

    // Gera nomes de clientes aleatórios
    private String gerarCliente() {
        String[] nomes = {"Ana", "Carlos", "Pedro", "João", "Maria"};
        return nomes[random.nextInt(nomes.length)];
    }
}
