
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * Implemente um servidor RMI que retorna 
 * um número aleatório de 1 a 100 quando o
 * cliente chama int sortear().
    Faça o servidor guardar um histórico dos últimos 
    5 números sorteados e permita que o cliente chame List<Integer> getHistorico().
 */

public class Servidor implements Sorteio {
    private Random random = new Random();
    private Deque<Integer> historico = new LinkedList<>();

    public static void main(String[] args) {
        try {
            Servidor sort = new Servidor();
            Sorteio stub = (Sorteio) UnicastRemoteObject.exportObject((Remote) sort, 1090);
            Registry registro = LocateRegistry.createRegistry(1090);
            registro.rebind("sorteio", stub);
            System.out.println("Aguardando clientes...");
        } catch (Exception e) {
            System.err.println("Erro no serv" + e.getMessage());
            
        }
    }

    @Override
    public int sortear() throws RemoteException{
        int valor = 1 + random.nextInt(100);
        historico.addLast(valor);
        if(historico.size() > 5){
            historico.removeFirst();
        }
        return valor;
    }

    @Override
    public List<Integer> getHistorico() throws RemoteException{
        return new ArrayList<>(historico);
    }
}
