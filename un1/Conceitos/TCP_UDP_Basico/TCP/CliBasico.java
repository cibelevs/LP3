package TCP_UDP_Basico.TCP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane; 

/*Explicando oque o codigo faz:
 * 1. Importa as bibliotecas necessárias para manipulação de sockets, entrada e saída de dados, e interface gráfica.
 * 2. Define a classe principal CliBasico.
 * 3. No método main, tenta estabelecer uma conexão com o servidor na porta 123
 * 4. Cria fluxos de entrada e saída para comunicação com o servidor.
 * 5. Usa um Scanner para ler a entrada do usuário.
 * 6. Entra em um loop onde:
 *    a. Solicita ao usuário que insira uma mensagem.
 *    b. Se o usuário digitar "sair", o loop é encerrado.
 *    c. Envia a mensagem ao servidor.
 *    d. Aguarda a resposta do servidor.
 *    e. Exibe a resposta em uma janela de diálogo.
 * 7. Se ocorrer algum problema de conexão, uma mensagem de erro é exibida.
*  8. Todos os recursos são fechados automaticamente graças ao try-with-resources.
 */

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