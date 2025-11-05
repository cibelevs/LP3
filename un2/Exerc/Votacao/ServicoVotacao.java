package un2.Exerc.Votacao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServicoVotacao extends Remote {
    public String autenticar(String cpf, String senha, boolean admin) throws RemoteException;
    public boolean jaVotou(String token) throws RemoteException;
    public String obterStatusEleicao() throws RemoteException;
    public boolean registrarVoto(String token, int idCandidato) throws RemoteException;
    public boolean validarToken(String token) throws RemoteException;
    public List<Candidato> listarCandidatos() throws RemoteException;
    public Map<Integer, Integer> obterResultadoParcial(String tokenAdmin) throws RemoteException;
    public Map<Integer, Integer> obterResultadoFinal(String tokenAdmin) throws RemoteException;
    public boolean iniciarEleicao(String tokenAdmin) throws RemoteException;
    public boolean encerrarEleicao(String tokenAdmin) throws RemoteException;
    
}
