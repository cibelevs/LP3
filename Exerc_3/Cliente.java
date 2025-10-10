package Exerc_3;


/*
 * Implemente um servidor TCP que mantém um dicionário de palavras e seus significados. 
 * O cliente pode enviar uma palavra para o servidor e receber seu significado como resposta.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        final String SERVIDOR = "localhost";
        final int PORTA = 12345;

        try (Socket socket = new Socket(SERVIDOR, PORTA);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.print("Digite uma palavra: ");
            String palavra = teclado.readLine();

            saida.println(palavra); // Envia a palavra ao servidor
            String resposta = entrada.readLine(); // Lê a resposta

            System.out.println("Significado: " + resposta);

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
