package Exerc_Extra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationServer {
    private static final int PORT = 123;
    private final boolean[] cadeiras = new boolean[10]; // false = livre, true = ocupada
    private ServerSocket servidor;
    private ExecutorService pool = Executors.newCachedThreadPool();

    public ReservationServer() throws IOException {
        servidor = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta " + PORT);
    }

    public void start() throws IOException {
        while (true) {
            Socket cliente = servidor.accept();
            pool.submit(new ClientHandler(cliente));
        }
    }

    // Método para localizar cadeira (situação I)
    private synchronized boolean localizaCadeira(int num) {
        if (num < 1 || num > 10) return false; 
        return !cadeiras[num - 1]; // true se livre
    }

    // Método para marcar cadeira como ocupada (situação II)
    private synchronized void setCadeiraParaOcupada(int num) {
        cadeiras[num - 1] = true;
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("Bem-vindo! Digite um número de 1 a 10 para reservar ou 'quit' para sair.");

                String linha;
                while ((linha = in.readLine()) != null) {
                    if (linha.equalsIgnoreCase("quit")) break;

                    try {
                        int numero = Integer.parseInt(linha);

                        if (localizaCadeira(numero)) {
                            setCadeiraParaOcupada(numero);
                            out.println("Cadeira " + numero + " reservada com sucesso!");
                        } else {
                            out.println("Cadeira " + numero + " já está ocupada!");
                        }

                    } catch (NumberFormatException e) {
                        out.println("Entrada inválida. Digite um número entre 1 e 10.");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ReservationServer servidor = new ReservationServer();
        servidor.start();
    }
}
