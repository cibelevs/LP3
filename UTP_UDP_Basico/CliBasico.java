package UTP_UDP_Basico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class CliBasico {
    public static void main(String[] args) {
        try (Socket cliente = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             Scanner teclado = new Scanner(System.in)) {
            
            System.out.println("Conexão estabelecida com o servidor!");
            
            while (true) {
                System.out.println("Insira uma mensagem (ou 'sair' para encerrar): ");
                String msg = teclado.nextLine();
                
                if ("sair".equalsIgnoreCase(msg)) {
                    break;
                }
                
                out.println(msg);
                
                String resposta = in.readLine();
                if (resposta == null) {
                    System.out.println("Servidor desconectado");
                    break;
                }
                
                JOptionPane.showMessageDialog(null, "Resposta do servidor: " + resposta);
            }
            
        } catch (Exception e) {
            System.out.println("Ocorreu um problema de conexão: " + e.getMessage());
        }
    }
}