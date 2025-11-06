// CallbackCliente.java (NOVO ARQUIVO)

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

// Deve estender UnicastRemoteObject para ser um objeto RMI
public class CallbackCliente extends UnicastRemoteObject implements Interface_CLI_CALLBACK {
    
    public CallbackCliente() throws RemoteException {
        super();
    }

    @Override
    public void notificarNovoLance(String codigoProduto, int novoLance) throws RemoteException {
        System.out.println("\n*** NOTIFICAÇÃO (MAIOR LANCE) ***");
        System.out.println("  O produto " + codigoProduto + " recebeu um novo lance de: " + novoLance);
        // Implemente a lógica para o usuário ver isso (ex: atualizar a tela)
    }
    
    @Override
    public void notificarEncerramento(Map<String, Integer> lancesFinais) throws RemoteException {
        System.out.println("\n*** NOTIFICAÇÃO (LEILÃO ENCERRADO) ***");
        System.out.println("  Lances Finais Recebidos: " + lancesFinais);
        // Implemente a lógica para o usuário ver isso
    }
}