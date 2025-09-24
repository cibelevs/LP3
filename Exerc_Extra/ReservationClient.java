package Exerc_Extra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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