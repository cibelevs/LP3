package Lista.Exerc_3;

/*
 * Implemente um servidor TCP que mantém um dicionário de palavras e seus significados. 
 * O cliente pode enviar uma palavra para o servidor e receber seu significado como resposta.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Serv {
    public static void main(String[] args) {
        final int PORTA = 12345;

        // Dicionário de palavras e significados
        Map<String, String> dicionario = new HashMap<>();
        // Preenche o dicionário com palavras e seus significados
        dicionario.put("java", "Linguagem de programação orientada a objetos.");
        dicionario.put("tcp", "Protocolo de controle de transmissão usado para comunicação confiável.");
        dicionario.put("socket", "Ponto de extremidade para comunicação entre duas máquinas.");
        dicionario.put("servidor", "Programa que fornece serviços a outros programas (clientes).");
        dicionario.put("cliente", "Programa que solicita serviços de um servidor.");

        try (ServerSocket servidor = new ServerSocket(PORTA)) {  // Cria socket servidor na porta especificada
            System.out.println("Servidor iniciado na porta " + PORTA + "...");

            // Loop infinito para aceitar múltiplos clientes (sequencialmente)
            while (true) {
                // Aguarda conexão de cliente (método bloqueante)
                Socket conexao = servidor.accept();
                System.out.println("Cliente conectado: " + conexao.getInetAddress().getHostAddress());

                // Prepara streams de entrada e saída para comunicação com o cliente
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(conexao.getInputStream()));
                PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);

                String palavra = entrada.readLine(); // Lê a palavra do cliente (bloqueante)
                System.out.println("Palavra recebida: " + palavra);

                // Busca significado no dicionário (converte para minúsculas para case-insensitive)
                // Se palavra não existe, retorna mensagem padrão
                String significado = dicionario.getOrDefault(
                        palavra.toLowerCase(),  // Converte para minúsculas para busca case-insensitive
                        "Palavra não encontrada no dicionário.");  // Mensagem padrão para palavras não encontradas

                saida.println(significado); // Envia o significado de volta ao cliente
                System.out.println("Resposta enviada ao cliente.");

                conexao.close();  // Fecha conexão com este cliente
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}