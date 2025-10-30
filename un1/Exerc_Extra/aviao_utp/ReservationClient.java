package Exerc_Extra.aviao_utp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
 * Desenvolva um sistema cliente-servidor em Java que simule a reserva de assentos em um avião,
 * utilizando comunicação por sockets TCP e tratamento de múltiplos clientes com threads.
 * 
 * O servidor deve:
 *  - Escutar conexões na porta 123.
 *  - Manter o controle de 10 cadeiras (de 1 a 10), indicando se estão livres ou ocupadas.
 *  - Receber do cliente o número da cadeira a ser reservada.
 *  - Verificar se a cadeira está livre:
 *      → Se estiver livre, marcar como ocupada e confirmar a reserva.
 *      → Se já estiver ocupada, informar que a cadeira está indisponível.
 *  - Encerrar a conexão caso o cliente envie o comando "quit".
 * 
 * O cliente deve:
 *  - Conectar-se ao servidor e exibir as mensagens enviadas por ele.
 *  - Permitir que o usuário digite o número da cadeira desejada (1 a 10) ou "quit" para sair.
 *  - Exibir a resposta do servidor com o status da reserva.
 * 
 * Utilize threads no servidor para permitir o atendimento simultâneo de vários clientes.
 */


public class ReservationClient {
 public static void main(String[] args) {
 String host = "localhost";
 int port = 123;
    try (
    Socket socket = new Socket(host, port);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    Scanner sc = new Scanner(System.in)
    ) {
        System.out.println("Conectado ao servidor.");
        System.out.println("Servidor: " + in.readLine());
         while (true) {
            System.out.print("Digite um número [1..10] ou 'quit': ");
            String msg = sc.nextLine();
            out.println(msg);
            if (msg.equalsIgnoreCase("quit")) break;
            String resposta = in.readLine();
            System.out.println("Servidor: " + resposta);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
 }
}