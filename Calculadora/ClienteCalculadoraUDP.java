package Calculadora;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteCalculadoraUDP {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final int BUFFER_SIZE = 1024;
    
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {
            
            InetAddress enderecoServidor = InetAddress.getByName(SERVER_ADDRESS);
            
            System.out.println("Cliente calculadora UDP");
            System.out.println("Digite 'sair' para encerrar");
            System.out.println("Formato: operacao:num1:num2");
            System.out.println("Operações: +, -, *, /");
            
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("sair")) {
                    break;
                }
                
                // Envia a requisição
                byte[] dadosEnvio = input.getBytes();
                DatagramPacket packetEnvio = new DatagramPacket(
                    dadosEnvio, dadosEnvio.length, enderecoServidor, SERVER_PORT);
                socket.send(packetEnvio);
                
                // Aguarda a resposta
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packetResposta = new DatagramPacket(buffer, buffer.length);
                socket.receive(packetResposta);
                
                // Exibe a resposta
                String resposta = new String(packetResposta.getData(), 0, packetResposta.getLength());
                System.out.println("Resposta: " + resposta);
            }
            
        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
