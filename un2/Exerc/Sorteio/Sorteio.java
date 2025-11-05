
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/*
* Implemente um servidor RMI que retorna 
* um número aleatório de 1 a 100 quando o
* cliente chama int sortear().
*/
public interface Sorteio extends Remote{
    public int sortear() throws RemoteException; // TEM QUE ACRESCENTAR 'THROWS REMOTE EXCEPTION'
    public List<Integer> getHistorico() throws RemoteException;
}