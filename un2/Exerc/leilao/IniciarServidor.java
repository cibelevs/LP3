import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;





/****
 * 🏢 O servidor pode ser comparado a uma empresa que contrata funcionários (os objetos remotos), 
 * define os cargos e funções de cada um (os métodos remotos) e divulga suas informações 
 * de contato em uma rede social profissional (o registry) para que os clientes (programas remotos) 
 * possam encontrá-la e solicitar seus serviços.
 * ****/
public class IniciarServidor  {

  

   // implementar metodos
    /**
     * @param args
     */
    public static void main ( String args[]){

     try{
      

      Servidor obj = new Servidor(); 
       // 🔹 Cria um objeto local do  objeto remoto.
      Registry registry = LocateRegistry.createRegistry(5000);
      //cria o registro e cria uma ponte para acessa-los atraveis da variavel

      registry.rebind("Interface_CLI_SERV",obj );
          // 🔹 "Rebind" significa registrar (ou substituir) um objeto remoto com esse nome.
        // O cliente vai usar esse nome para localizar o objeto.
      System.out.println("Server ready");

     }catch(final Exception e ) {

        e.printStackTrace();
        
     }

    }

    

    // public  tipo e nome do metodo que quer implementar(){}
}