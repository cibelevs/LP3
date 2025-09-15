package UTP_UDP_Basico;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

//package UTP_UDP_Basico;

public class ServBasico {
    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(1234);
            System.out.println("Servidor buscando conexão na porta 1234..");

            while (true) { 
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado " + cliente.getInetAddress().getHostName());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(new Date());
                cliente.close();
                saida.close();
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um problema de conexao");
            e.printStackTrace();
        }
    }
}
