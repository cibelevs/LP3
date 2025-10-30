package Exerc_Extra.aviao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 123);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("Pressione Enter para iniciar...");
            teclado.nextLine();
            System.out.println(entrada.readLine()); // "OK"
            System.out.println(entrada.readLine()); // "Entre com o número..."

            int numero = teclado.nextInt();
            saida.println(numero);

            System.out.println("Servidor: " + entrada.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

