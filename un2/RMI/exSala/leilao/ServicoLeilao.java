
import java.rmi.Remote;
import java.rmi.RemoteException;




public interface ServicoLeilao extends Remote{

    public String receberNotificaçãoLancesMaiores(int idCadastro) throws RemoteException;
    public Double consultarMaiorLance() throws RemoteException;
    public String ofertarLances() throws RemoteException;;
    
    

}
