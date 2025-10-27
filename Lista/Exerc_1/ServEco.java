
package Lista.Exerc_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServEco{
    public static void main(String[] args) {
        
    try {
        ServerSocket servidor = new ServerSocket(1234);
        System.out.println("Servidor criado utilizando a porta 1234...");

        while(true){
            Socket cliente = servidor.accept();
            
            System.out.println("Cliente conectado no endereco " + cliente.getInetAddress().getHostName());
            
            InputStreamReader fluxoDado = new InputStreamReader(cliente.getInputStream());
            BufferedReader dado = new BufferedReader(fluxoDado);
            PrintStream saida = new PrintStream(cliente.getOutputStream());

            String txt = null;
            while ((txt = dado.readLine()) != null){
                // Verifica se o cliente quer sair
                if (txt.equalsIgnoreCase("sair")) {
                    System.out.println("Cliente solicitou encerramento da conexão.");
                    break;
                }

                // Ignora mensagens vazias
                if (txt.trim().isEmpty()) {
                    continue;
                }

                System.out.println("Mensagem recebida: " + txt);
                saida.println(txt);
            }

            cliente.close();
            System.out.println("Cliente desconectado.");

        }

        
    } catch (Exception e) {
        System.out.println("Ocorreu um erro na conexão");
        e.printStackTrace();

    } finally{
        System.out.println("Operacao finalizada");
    }

    }
}