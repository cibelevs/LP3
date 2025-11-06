import java.rmi.*;
import java.util.Map;
public interface Interface_CLI_SERV extends Remote{
     
    boolean registrarParaNotificacao(Interface_CLI_CALLBACK clienteCallback) throws RemoteException;

    int maiorlance_desse_produto(String codigo)throws RemoteException;

    void EncerrarLeilao() throws RemoteException;

    int maiorlance_de_todos_produtos( )throws RemoteException;

    boolean IniciarLeilao(int duracaoSegundos) throws RemoteException;

    int TempoLeilao (  int tempo )throws RemoteException;

   
    boolean  OfertarLance(String codigoOfertado, int valorOfertado )throws RemoteException;

    Map<String, Integer> listarLances() throws RemoteException;

    // pensando em colocar o codigo do produto no primeiro interger e o valor no segundo 
    // uma lista de lances do usuario e depois verificar o maior no laço no arraylist



    // inicializar metodos 
}