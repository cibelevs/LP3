package Lista.Exerc_3;



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
        // Define endereço e porta do servidor
        final String SERVIDOR = "localhost";
        final int PORTA = 12345;

        // Try-with-resources garante fechamento automático dos recursos
        try (Socket socket = new Socket(SERVIDOR, PORTA);             // Cria socket e conecta ao servidor
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));  // Lê entrada do teclado
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);  // Envia dados para servidor (auto-flush)
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {  // Recebe dados do servidor

            // Solicita palavra ao usuário
            System.out.print("Digite uma palavra: ");
            String palavra = teclado.readLine();  // Lê palavra do teclado

            saida.println(palavra); // Envia a palavra ao servidor
            String resposta = entrada.readLine(); // Lê a resposta do servidor (bloqueante)

            // Exibe o significado recebido
            System.out.println("Significado: " + resposta);

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
        // Os recursos são fechados automaticamente pelo try-with-resources
    }
}