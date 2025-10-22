package Exerc_Extra.aviao;

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        final int PORTA = 123; // conforme instrução

        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando conexão na porta " + PORTA + "...");
            Aviao aviao = new Aviao();

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                new Thread(() -> {
                    try (
                        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true)
                    ) {
                        saida.println("OK");
                        saida.println("Entre com o número [1,10] da cadeira que deseja reservar:");
                        int numero = Integer.parseInt(entrada.readLine());
                        String resposta = aviao.setCadeiraParaOcupada(numero);
                        saida.println(resposta);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
