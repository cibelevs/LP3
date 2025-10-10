package Exerc_4;
/*
 * Crie um servidor TCP que gera um número aleatório e desafia os clientes a adivinhá lo. O
    servidor fornece dicas sobre se o número é maior ou menor do que a tentativa do cliente.     
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORTA = 1234;

        try (Socket socket = new Socket(HOST, PORTA);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             Scanner teclado = new Scanner(System.in)) {

            System.out.println(entrada.readLine()); // Mensagem inicial do servidor

            while (true) {
                System.out.print("Digite sua tentativa: ");
                String tentativa = teclado.nextLine();

                saida.println(tentativa);
                String resposta = entrada.readLine();
                if (resposta == null) break;

                System.out.println(resposta);

                if (resposta.contains("Parabéns")) break;
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
