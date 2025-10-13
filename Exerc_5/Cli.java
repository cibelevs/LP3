package Exerc_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cli {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread para ouvir mensagens do servidor
            new Thread(() -> {
                try {
                    String resposta;
                    while ((resposta = in.readLine()) != null) {
                        System.out.println(resposta);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada.");
                }
            }).start();

            // Enviar mensagens (nome e lances)
            String msg;
            while ((msg = teclado.readLine()) != null) {
                out.println(msg);
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}

