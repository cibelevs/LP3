/*
 * Simulação de reservas de um cinema com 10 lugares.
 * 
 * Cada cliente (thread) tenta reservar um assento. O sistema deve impedir
 * que dois clientes reservem o mesmo lugar ao mesmo tempo.
 * 
 * Utilize sincronização para garantir a consistência.
 */

import java.util.Random;

class Cinema {
    private final boolean[] lugares = new boolean[10]; // false = livre, true = ocupado

    // Método sincronizado garante exclusão mútua
    public synchronized boolean reservarLugar(int clienteId) {
        Random rand = new Random();
        int tentativa = rand.nextInt(10); // cliente tenta um lugar aleatório

        if (!lugares[tentativa]) {
            lugares[tentativa] = true;
            System.out.println("Cliente " + clienteId + " reservou o assento " + (tentativa + 1));
            return true;
        } else {
            System.out.println("Cliente " + clienteId + " tentou o assento " + (tentativa + 1) + " mas já está ocupado.");
            return false;
        }
    }

    public void exibirLugares() {
        System.out.println("\n--- Estado final dos assentos ---");
        for (int i = 0; i < lugares.length; i++) {
            System.out.println("Assento " + (i + 1) + ": " + (lugares[i] ? "OCUPADO" : "LIVRE"));
        }
    }
}

