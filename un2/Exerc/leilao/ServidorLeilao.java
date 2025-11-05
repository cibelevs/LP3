import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class ServidorLeilao implements ServicoLeilao{

    private final ArrayList<ObjetoLeilao> objetos = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    
    public static void main(String[] args) {
        try {
            ServidorLeilao serv = new ServidorLeilao();
            serv.objetos.add(new ObjetoLeilao("Computador Gamer"));
            serv.objetos.add(new ObjetoLeilao("Celular Samsung"));
            serv.objetos.add(new ObjetoLeilao("Fone Bluetooth"));
            ServicoLeilao servico = (ServicoLeilao) UnicastRemoteObject.exportObject((Remote) serv, 0);
            Registry reg = LocateRegistry.createRegistry(2000);
            reg.rebind("leilao", servico);

        } catch (Exception e) {
            System.out.println("Erro de conexao");
            e.printStackTrace();
        }
    }


    @Override
    public float  consultarMaiorLance() {
        float maior = 0;
        for(Cliente c : clientes){
            if(c.getMaiorLance() > maior){
                maior = c.getMaiorLance();
            }
        }
        return maior;
    }

    @Override
    public String ofertarLances() {
        Random r = new Random();
        int n = r.nextInt(objetos.size());
        return objetos.get(n).getNome();
    }


    @Override
    public void listarObjetos(){
        System.out.println("Objetos em leilão:");
        for(ObjetoLeilao objs: objetos){
            objs.toString();
        }
    }

    @Override
    public String receberNotificaçãoLancesMaiores() throws RemoteException {
        return "abc";
    }
    
}
