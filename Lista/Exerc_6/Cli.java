package Lista.Exerc_6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cli {
    private static final String SERVIDOR = "localhost";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        System.out.println("=== CLIENTE DO SISTEMA DE VOTAÇÃO ===");
        
        try (
            Socket socket = new Socket(SERVIDOR, PORTA);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado ao servidor de votação...");
            
            // Lê e exibe a mensagem de boas-vindas do servidor
            String mensagemServidor;
            while ((mensagemServidor = entrada.readLine()) != null) {
                System.out.println(mensagemServidor);
                
                // Quando recebe a linha de separação, significa que as instruções terminaram
                if (mensagemServidor.contains("===")) {
                    break;
                }
            }

            // Loop principal de interação com o usuário
            while (true) {
                System.out.print("\nDigite seu comando: ");
                String comando = scanner.nextLine();
                
                // Envia o comando para o servidor
                saida.println(comando);
                
                // Se o comando for SAIR, encerra o cliente
                if (comando.equalsIgnoreCase("SAIR")) {
                    System.out.println("Encerrando cliente...");
                    break;
                }
                
                // Lê e exibe a resposta do servidor
                String resposta = entrada.readLine();
                System.out.println("Servidor: " + resposta);
            }

        } catch (UnknownHostException e) {
            System.err.println("Erro: Servidor não encontrado. Verifique se o servidor está rodando em " + SERVIDOR + ":" + PORTA);
        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível conectar ao servidor. Verifique se o servidor está rodando.");
        } catch (IOException e) {
            System.err.println("Erro de comunicação com o servidor: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Cliente encerrado.");
    }
}
