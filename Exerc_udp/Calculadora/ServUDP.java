package Exerc_udp.Calculadora;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServUDP {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;
    private Calculadora calculadora;
    
    public ServUDP() {
        this.calculadora = new Calculadora();
    }
    
    public void iniciar() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Servidor calculadora UDP iniciado na porta " + PORT);
            System.out.println("Aguardando requisições...");
            
            while (true) {
                // Prepara para receber dados
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                // Recebe o pacote
                socket.receive(packet);
                
                // Processa a requisição
                String dadosRecebidos = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Requisição recebida: " + dadosRecebidos);
                
                // Processa a requisição e prepara a resposta
                String resposta = processarRequisicao(dadosRecebidos);
                
                // Obtém endereço e porta do cliente
                InetAddress enderecoCliente = packet.getAddress();
                int portaCliente = packet.getPort();
                
                // Envia a resposta
                byte[] dadosResposta = resposta.getBytes();
                DatagramPacket packetResposta = new DatagramPacket(
                    dadosResposta, dadosResposta.length, enderecoCliente, portaCliente);
                socket.send(packetResposta);
                
                System.out.println("Resposta enviada: " + resposta);
            }
        } catch (SocketException e) {
            System.err.println("Erro ao criar socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O: " + e.getMessage());
        }
    }
    
    private String processarRequisicao(String dados) {
        try {
            // Formato esperado: "operacao:num1:num2"
            String[] partes = dados.split(":");
            
            if (partes.length != 3) {
                return "ERRO: Formato inválido. Use: operacao:num1:num2";
            }
            
            String operacao = partes[0];
            double num1 = Double.parseDouble(partes[1]);
            double num2 = Double.parseDouble(partes[2]);
            
            double resultado = calculadora.calcular(operacao, num1, num2);
            return "SUCESSO: " + resultado;
            
        } catch (NumberFormatException e) {
            return "ERRO: Números inválidos";
        } catch (IllegalArgumentException e) {
            return "ERRO: " + e.getMessage();
        } catch (ArithmeticException e) {
            return "ERRO: " + e.getMessage();
        } catch (Exception e) {
            return "ERRO: " + e.getMessage();
        }
    }
    
    public static void main(String[] args) {
        ServUDP servidor = new ServUDP();
        servidor.iniciar();
    }
}