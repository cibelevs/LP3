package Lista.Exerc_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*      Desenvolva um servidor TCP para um sistema de leilão onde os clientes podem fazer
    lances em itens. O servidor deve coordenar os lances e notificar todos os clientes sobre o
    status do leilão.
 */

public class Serv {

    // Lista compartilhada com todos os clientes conectados
    private static List<PrintWriter> clientWriters = Collections.synchronizedList(new ArrayList<>());

    // Controle do leilão
    private static double maiorLance = 0.0;
    private static String maiorLicitante = "Ninguém";
    private static final Object lock = new Object(); // usado para sincronizar os lances

    public static void main(String[] args) {
        final int PORTA = 1234;
        System.out.println("🟢 Servidor de Leilão iniciado na porta " + PORTA);

        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            while (true) {
                Socket clienteSocket = servidor.accept();
                System.out.println("Novo cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());

                PrintWriter writer = new PrintWriter(clienteSocket.getOutputStream(), true);
                clientWriters.add(writer);

                // Cria uma thread para tratar cada cliente individualmente
                Thread t = new Thread(new ClientHandler(clienteSocket, writer));
                t.start();
            }
        } catch (IOException e) {
            System.err.println("❌ Erro no servidor: " + e.getMessage());
        }
    }

    /** Envia uma mensagem para todos os clientes conectados */
    private static void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }

    /** Classe interna que trata cada cliente em uma thread separada */
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String nomeCliente;

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.out = writer;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("Bem-vindo ao Leilão! Digite seu nome:");
                nomeCliente = in.readLine();

                broadcast("🔔 " + nomeCliente + " entrou no leilão!");
                out.println("O maior lance atual é R$" + maiorLance + " por " + maiorLicitante);

                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    try {
                        double lance = Double.parseDouble(mensagem);
                        synchronized (lock) {
                            if (lance > maiorLance) {
                                maiorLance = lance;
                                maiorLicitante = nomeCliente;
                                broadcast("✅ Novo lance: R$" + maiorLance + " por " + maiorLicitante);
                            } else {
                                out.println("❌ Lance muito baixo! O maior é R$" + maiorLance);
                            }
                        }
                    } catch (NumberFormatException e) {
                        out.println("⚠️ Envie apenas números (valor do lance).");
                    }
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + nomeCliente);
            } finally {
                try {
                    socket.close();
                    clientWriters.remove(out);
                    broadcast("🚪 " + nomeCliente + " saiu do leilão.");
                } catch (IOException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}
