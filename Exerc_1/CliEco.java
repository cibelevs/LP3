

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class CliEco {
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("localhost",1234);
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(cliente.getInputStream())
            );

            Scanner teclado = new Scanner(System.in);

            while (true) {
                System.out.print("Digite uma mensagem: ");
                String msg = teclado.nextLine();

                ps.println(msg);

                String resposta = in.readLine();
                if (resposta == null) break; 

                System.out.println("Eco do servidor: " + resposta);
            }

            cliente.close();
            teclado.close();
            
        } catch(Exception e){
            System.out.println("Ocorreu um erro de conexão");
            e.printStackTrace();
        }
        finally {
            System.out.println("Operação Finalizada");
        }
    }
}
