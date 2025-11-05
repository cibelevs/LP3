
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Integer> historico;
    public static void main(String[] args) {
        try {
            boasVindas();
            Registry reg = LocateRegistry.getRegistry("localhost",1090);
            Sorteio sorteio = (Sorteio) reg.lookup("sorteio");
            System.out.println("RESULTADO DO SORTEIO: " + sorteio.sortear());       
            try {
                historico = sorteio.getHistorico();
                listaHistorico();
            } catch (Exception e) {
                e.printStackTrace();
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }        

    }
    

    public static void boasVindas(){
        System.out.println("Bem vindo(a) ao sorteio!");
        System.out.print("Insira seu nome: ");
        String nome = scanner.nextLine();
        System.out.println("Aguarde um momento " + nome + ", o sorteio irá começar em instantes...");
    }

    public static void listaHistorico(){
        System.out.println("--------LISTANDO HISTORICO--------");
        System.out.println("HISTÓRICO (últimos " + historico.size() + "): " + historico);
    }
}
