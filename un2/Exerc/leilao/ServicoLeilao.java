
import java.rmi.Remote;
import java.rmi.RemoteException;




public interface ServicoLeilao extends Remote {

    
    public String receberNotificaçãoLancesMaiores() throws RemoteException;
    public float consultarMaiorLance()  throws RemoteException;
    public String ofertarLances()  throws RemoteException;
    public void listarObjetos()  throws RemoteException;

}
