package un2.RMI.Exerc;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Servidor implements Saudacao {

    @Override
    public String saudacao(String nome) {
      return "Hey " + nome + " lets learn RMI?";
    }


    public static void main(String[] args) {
        try {
            // Cria o objeto que implementa a interface
            Servidor servidor = new Servidor();

            // Exporta o objeto remoto (gera o stub)
            Saudacao stub = (Saudacao) UnicastRemoteObject.exportObject(servidor, 0);

            // Cria o registro RMI na porta 1099
            Registry registro = LocateRegistry.createRegistry(1099);

            // Registra o objeto com o nome "saudacao"
            registro.rebind("saudacao", stub);

            System.out.println("✅ Servidor pronto e aguardando chamadas...");
        } catch (Exception e) {
            System.err.println("❌ Erro no servidor: " + e.toString());
            e.printStackTrace();
        }
    }

    
}
