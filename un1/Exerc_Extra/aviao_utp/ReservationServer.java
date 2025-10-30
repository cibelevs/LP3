package Exerc_Extra.aviao_utp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Desenvolva um sistema cliente-servidor em Java que simule a reserva de assentos em um avião,
 * utilizando comunicação por sockets TCP e tratamento de múltiplos clientes com threads.
 * 
 * O servidor deve:
 *  - Escutar conexões na porta 123.
 *  - Manter o controle de 10 cadeiras (de 1 a 10), indicando se estão livres ou ocupadas.
 *  - Receber do cliente o número da cadeira a ser reservada.
 *  - Verificar se a cadeira está livre:
 *      → Se estiver livre, marcar como ocupada e confirmar a reserva.
 *      → Se já estiver ocupada, informar que a cadeira está indisponível.
 *  - Encerrar a conexão caso o cliente envie o comando "quit".
 * 
 * O cliente deve:
 *  - Conectar-se ao servidor e exibir as mensagens enviadas por ele.
 *  - Permitir que o usuário digite o número da cadeira desejada (1 a 10) ou "quit" para sair.
 *  - Exibir a resposta do servidor com o status da reserva.
 * 
 * Utilize threads no servidor para permitir o atendimento simultâneo de vários clientes.
 */


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
