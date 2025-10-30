package Exerc_Extra.estacionamento;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        final int PORTA = 2025;
        Estacionamento estacionamento = new Estacionamento();

        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor de estacionamento iniciado na porta " + PORTA);

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Novo cliente conectado: " + cliente.getInetAddress());

                Thread t = new Thread(() -> {
                    try (
                        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true)
                    ) {
                        saida.println("Bem-vindo ao sistema de estacionamento.");
                        saida.println("Digite o número da vaga [1-10]:");

                        String msg = entrada.readLine();

                        if (msg != null) {
                            msg = msg.trim(); // remove espaços/quebras
                            try {
                                int numero = Integer.parseInt(msg);
                                String resposta = estacionamento.setVagaOcupada(numero);
                                saida.println(resposta);
                                estacionamento.listar();
                            } catch (NumberFormatException e) {
                                saida.println("Entrada inválida. Digite um número entre 1 e 10.");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            cliente.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
