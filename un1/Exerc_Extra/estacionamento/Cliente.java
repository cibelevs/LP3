package Exerc_Extra.estacionamento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORTA = 2025;

        try (
            Socket socket = new Socket(HOST, PORTA);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println(entrada.readLine()); // Bem-vindo...
            System.out.println(entrada.readLine()); // Digite o número...

            System.out.print("→ ");
            String numero = teclado.nextLine();
            saida.println(numero);

            String resposta = entrada.readLine();
            System.out.println("Servidor: " + resposta);

        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}

