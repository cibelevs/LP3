package Exerc_4;
/*
 * Crie um servidor TCP que gera um número aleatório e desafia os clientes a adivinhá lo. O
    servidor fornece dicas sobre se o número é maior ou menor do que a tentativa do cliente.     
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Servidor {
    public static void main(String[] args) {
        final int PORTA = 1234;

        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor ligado na porta " + PORTA + "...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostName());

                // Fluxos de entrada e saída
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream()));
                PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);

                // Gera número aleatório entre 1 e 100
                int numeroSecreto = new Random().nextInt(100) + 1;
                System.out.println("Número secreto: " + numeroSecreto);

                saida.println("Tente adivinhar o número entre 1 e 100!");

                while (true) {
                    String tentativaStr = entrada.readLine();
                    if (tentativaStr == null) break;

                    try {
                        int tentativa = Integer.parseInt(tentativaStr);

                        if (tentativa > numeroSecreto) {
                            saida.println("O número é menor!");
                        } else if (tentativa < numeroSecreto) {
                            saida.println("O número é maior!");
                        } else {
                            saida.println("Parabéns! Você acertou!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        saida.println("Digite um número válido!");
                    }
                }

                cliente.close();
                System.out.println("Conexão encerrada com o cliente.\n");
            }

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}
