package Lista.Exerc_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class CliEco {
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("localhost",1234);
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(cliente.getInputStream())
            );

            Scanner teclado = new Scanner(System.in);

            while (true) {
                System.out.print("Digite uma mensagem (ou 'sair' para encerrar): ");
                String msg = teclado.nextLine();

                // Verifica se o usuário quer sair
                if (msg.equalsIgnoreCase("sair")) {
                    ps.println(msg); // Envia "sair" para o servidor
                    System.out.println("Encerrando conexão...");
                    break;
                }

                // Verifica se a mensagem está vazia
                if (msg.trim().isEmpty()) {
                    System.out.println("Mensagem vazia, digite algo ou 'sair' para encerrar.");
                    continue;
                }

                // Envia mensagem válida para o servidor
                ps.println(msg);

                String resposta = in.readLine();
                if (resposta == null) break; 

                System.out.println("Eco do servidor: " + resposta);
            }

            cliente.close();
            teclado.close();
            
        } catch(Exception e){
            System.out.println("Ocorreu um erro de conexão");
            e.printStackTrace();
        }
        finally {
            System.out.println("Operação Finalizada");
        }
    }
}