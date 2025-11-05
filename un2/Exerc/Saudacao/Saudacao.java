package un2.RMI.Exerc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface  Saudacao extends Remote{
    public String saudacao (String nome) throws RemoteException;
    
}
