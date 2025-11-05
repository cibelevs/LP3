import java.awt.Menu;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

public class ClienteLeilao {
     
    private static Random random = new Random();
    private static Cliente cliente;

    public static void main(String[] args) {
        try {
            Registry registro = LocateRegistry.getRegistry("localhost",2000);
            ServicoLeilao leilao = (ServicoLeilao) registro.lookup("leilao");
            Scanner scanner = new Scanner(System.in);
            msgBoasVindas();
            System.err.println("Insira seu nome: ");
            String name = scanner.nextLine();
            cliente = new Cliente(random.nextInt(100), 0, name);
            msgCadastro();
            menu();
            int n = scanner.nextInt();
            if(n == 1){
                float a = leilao.consultarMaiorLance();
                System.out.println("Maior lance até o momento: " + a);
            } else {
                String nome = leilao.ofertarLances();
                System.out.println("Objeto leiloado: " + nome);
            }
        } catch (Exception e) {
            System.out.println("Erro de conexão!!!! " + e.getMessage());
        }
    }

    

    public static void menu(){
        System.out.println("Insira 1 para começar o leilao...");
        System.out.println("Insira 2 para saber o objeto leiloado");
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