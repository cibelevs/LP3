package un2.RMI.Exerc;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.print("Insira seu nome: ");
            String nome = scanner.nextLine();

            // Conecta ao registro RMI
            Registry registro = LocateRegistry.getRegistry("localhost");

            // Busca o objeto remoto pelo nome
            Saudacao stub = (Saudacao) registro.lookup("saudacao");

            // Chama o método remoto
            String resposta = stub.saudacao(nome);

            System.out.println("Mensagem recebida: " + resposta);
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}



/* 
public class Cliente {
    private static Scanner scanner= new Scanner(System.in);
    public static void main(String[] args) {
        try {
            System.out.println("Insira seu nome: ");
            String nome = scanner.nextLine();
            Registry registro = LocateRegistry.getRegistry("localhost");
            Saudacao stub = (Saudacao) registro.lookup("saudacao");
            System.out.println("Mensagem: "  + stub.saudacao(nome));
            
        } catch (Exception e) {
        }
    }
    
}
*/