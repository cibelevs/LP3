import java.util.Random;
import java.util.Scanner;

public class ClienteLeilao {
     
    private static Random random = new Random();
    private static Cliente cliente;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            msgBoasVindas();
            System.err.println("Insira seu nome: ");
            String name = scanner.nextLine();
            cliente = new Cliente(random.nextInt(100), name);
            msgCadastro();

        } catch (Exception e) {
            System.out.println("Erro de conexão!!!! " + e.getMessage());
        } finally {
        }
    }

    public void sairLeilao(){
        System.out.println("Saindo do leilao....");
    }

    public static void msgBoasVindas(){
        System.out.println("-------------LEILÃO LP3------------------");
        System.out.println("!!!!!!!!!!Bem vindo ao leilão!!!!!!!!!!!!");
    }
    
    public static void msgCadastro(){
        System.out.println("Cadastrando [" + cliente.getNome() + "] com o id " + cliente.getId() + "...");
    }
    
}