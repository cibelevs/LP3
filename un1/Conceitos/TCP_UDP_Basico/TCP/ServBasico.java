package TCP_UDP_Basico.TCP;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

//package UTP_UDP_Basico;
/*
 * Explicando oque o codigo faz:
 * 1. Importa as bibliotecas necessárias para manipulação de sockets e fluxo de objetos
 * 2. Define a classe principal ServBasico.
 * 3. No método main, tenta criar um servidor na porta 1234.
 * 4. Entra em um loop infinito onde:
 *    a. Aguarda a conexão de um cliente.
 *    b. Quando um cliente se conecta, imprime o endereço do cliente.
 *    c. Cria um fluxo de saída de objetos para enviar dados ao cliente.
 *    d. Envia a data e hora atual para o cliente.
 *    e. Fecha a conexão com o cliente.
 * 5. Se ocorrer algum problema de conexão, uma mensagem de erro é exibida.
 */

 //Uso de Socket TCP
public class ServBasico {
    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(1234);
            System.out.println("Servidor buscando conexão na porta 1234..");

            while (true) { 
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado " + cliente.getInetAddress().getHostName());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(new Date());
                cliente.close();
                saida.close();
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um problema de conexao");
            e.printStackTrace();
        }
    }
}
