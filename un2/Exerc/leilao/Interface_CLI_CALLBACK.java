

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map; // Para Map<String, Integer>

public interface Interface_CLI_CALLBACK extends Remote {
    
    // Método para notificar um novo maior lance
    void notificarNovoLance(String codigoProduto, int novoLance) throws RemoteException;
    
    // Método para notificar o encerramento do leilão e o resultado final
    void notificarEncerramento(Map<String, Integer> lancesFinais) throws RemoteException;
}