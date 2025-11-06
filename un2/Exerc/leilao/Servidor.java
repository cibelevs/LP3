import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Servidor extends UnicastRemoteObject implements Interface_CLI_SERV {
private static final long serialVersionUID = 1L;
private long tempoFimLeilao = 0; // Armazena o timestamp de encerramento
private final long DURACAO_MAXIMA_MS = 5 * 60 * 1000; // 5 minutos em milissegundos
private volatile boolean leilaoAtivo = false;
private Map<String, Integer> lances = new ConcurrentHashMap<>();
private List<Interface_CLI_CALLBACK> clientesConectados = new CopyOnWriteArrayList<>();
private ScheduledExecutorService executorTimeout = Executors.newSingleThreadScheduledExecutor();
// newSingleThreadScheduledExecutor é suficiente para agendar o encerramento do leilão


    public Servidor  (  ) throws RemoteException {
     super(); // importante para UnicastRemoteObject
    InicializarLances();
        

    }

   public  void  InicializarLances(){

       lances.put( "Cl34" , 5000);
       lances.put( "AS21" , 3450);
       lances.put( "NB67" , 8000);
       lances.put( "VF54" , 7000);
       lances.put( "HJ31" , 5700);
       lances.put( "GD54" , 5000);
       lances.put( "DS45" , 5000);
       lances.put( "AD29" , 10000);


    }


// ===================== Métodos de Leilão/ lances  =====================    
    @Override
     public  boolean  OfertarLance(String codigoOfertado, int valorOfertado )throws RemoteException{
        if (codigoOfertado == null) return false;
            if (!this.leilaoAtivo) {
        System.out.println("Lance recusado: Leilão não está ativo.");
        return false;
            }

        String chave = codigoOfertado.trim().toUpperCase();; 
        // armazena o codigo ofertado em uma variavel 

        Integer valorAtual = lances.get(chave);
        //O Java vai procurar dentro do Map lances pela chave "CL34".
        //Como ela existe (foi criada no inicializarLances()),
        //ele encontra o valor por ex 5000 e guarda na variável:
        
         
          if(valorAtual == null){
             System.out.println("Código inválido: " + chave);
            return false;
         }
         int lanceatual = valorAtual + valorAtual*5/100;
         System.out.println("Lance mínimo para " + chave + " é: " + lanceatual);
         if (valorOfertado>lanceatual){
            lances.put(chave,valorOfertado);
            notificarTodos(chave, valorOfertado);
            System.out.println("Lance atualizado! " + chave + " = " + valorOfertado);
            return true;
         } else {
            System.out.println("Lance recusado. Valor menor que o atual (" + valorAtual + ")");
            return false;
        }

     }


    @Override
public Map<String, Integer> listarLances (){
    // Retorna o Map completo de códigos e lances
    return this.lances;
}





// ===================== Métodos de Controle de Tempo do Leilão =====================

    // Este método é apenas para iniciar e configurar o tempo
public boolean IniciarLeilao(int duracaoSegundos)throws RemoteException {
    if (leilaoAtivo) {
        System.out.println("O leilão já está ativo.");
        return false;
    }

    long duracaoMs = duracaoSegundos * 1000L;
    // O requisito diz: "deve encerra no máximo 5 minutos"
    if (duracaoMs <= 0 || duracaoMs > DURACAO_MAXIMA_MS) {
        System.out.println("Duração do leilao finalizada . Máximo de 5 minutos.");
        return false;
    }

    // Configura o tempo de fim e marca como ativo
    this.tempoFimLeilao = System.currentTimeMillis() + duracaoMs;
    this.leilaoAtivo = true;
    System.out.println("Leilão iniciado. Fim em: " + new Date(tempoFimLeilao));

    executorTimeout.schedule(() -> {
    try {
        EncerrarLeilao();
    } catch (RemoteException e) {
        System.err.println("Erro ao encerrar: " + e.getMessage());
    }
}, duracaoSegundos, TimeUnit.SECONDS);

    return true;
}

// Método para ser chamado quando o tempo acabar
@Override 
public void EncerrarLeilao() throws RemoteException { 

    if (!leilaoAtivo) return;
    leilaoAtivo = false;
    System.out.println("\n*** LEILÃO ENCERRADO PELO TEMPO! ***");

    // NOTIFICAÇÃO 
    for (Interface_CLI_CALLBACK cliente : clientesConectados) {
        try {
            // Chama o método notificarEncerramento no cliente, passando os resultados
            cliente.notificarEncerramento(this.lances); 
        } catch (RemoteException e) {
            // Não é crítico, mas é bom para logging. O cliente já foi removido no notificarTodos se caiu antes.
            System.err.println("Falha ao notificar cliente sobre o encerramento. Cliente pode ter caído: " + e.getMessage());
        }
    }
}







// ===================== Métodos de Consulta de Maior Lance =====================



    @Override
public int maiorlance_desse_produto(String codigo)throws RemoteException{
    // Retorna o Map completo de códigos e lances
    int maiorlance = 0;
   
    for (Map.Entry<String, Integer> lanceAtual : lances.entrySet()) {
        
        if(maiorlance == 0 || lanceAtual .getValue() > maiorlance && lanceAtual.getKey().equals(codigo) ){ 
            maiorlance = lanceAtual.getValue();
        }
    }
    if(maiorlance > 0){
        System.out.println("Maior lance atual:" + maiorlance);
        System.out.println(  " Lance: " + maiorlance);
    }else{
        System.out.println("Nenhum lance encontrado.");
    }
    return  maiorlance;
}
public int maiorlance_de_todos_produtos( )throws RemoteException{
    // Retorna o Map completo de códigos e lances
    int maiorlance = 0;
   
    for (Map.Entry<String, Integer> lanceAtual : lances.entrySet()) {
        
        if(maiorlance == 0 || lanceAtual .getValue() > maiorlance  ){ 
            maiorlance = lanceAtual.getValue();
        }
    }
    if(maiorlance > 0){
        System.out.println("Maior lance atual:" + maiorlance);
        System.out.println(  " Lance: " + maiorlance);
    }else{
        System.out.println("Nenhum lance encontrado.");
    }
    return  maiorlance;
}
//Map.Entry é um item do mapa, que contém uma chave (key) e um valor (value).
//Quando você quer percorrer todos os itens de um Map, você usa o método .entrySet():
//Map.Entry<String, Integer> é o tipo do item (um par chave/valor).
//entrada.getKey() → devolve a chave.
//entrada.getValue() → devolve o valor.





// ===================== Notificações via Callback =====================


@Override
    public boolean registrarParaNotificacao(Interface_CLI_CALLBACK clienteCallback) throws RemoteException {
        if (!clientesConectados.contains(clienteCallback)) {
            clientesConectados.add(clienteCallback);
            System.out.println("Novo cliente registrado para notificações.");
            return true;
        }
        return false;
    }

    // NOVO MÉTODO: Auxiliar para iterar e notificar (Lida com o multithreading do loop)
    private void notificarTodos(String codigoProduto, int novoLance) {
        // Itera sobre a lista thread-safe
        for (Interface_CLI_CALLBACK cliente : clientesConectados) {
            try {
                // Chama o método remoto no cliente
                cliente.notificarNovoLance(codigoProduto, novoLance);
            } catch (RemoteException e) {
                // Se a chamada falhar (cliente desconectado), remove o cliente
                System.out.println("Cliente falhou na notificação (desconectado). Removendo da lista.");
                clientesConectados.remove(cliente);
            }
        }
    }

  


    // ===================== Getters e Setters =====================


    public Map<String, Integer> getValorlance(String codigo) {
        return lances;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public long getTempoFimLeilao() {
        return tempoFimLeilao;
    }

    public void setTempoFimLeilao(long tempoFimLeilao) {
        this.tempoFimLeilao = tempoFimLeilao;
    }

    public long getDURACAO_MAXIMA_MS() {
        return DURACAO_MAXIMA_MS;
    }

    public boolean isLeilaoAtivo() {
        return leilaoAtivo;
    }

    public void setLeilaoAtivo(boolean leilaoAtivo) {
        this.leilaoAtivo = leilaoAtivo;
    }

    public Map<String, Integer> getLances() {
        return lances;
    }

    public void setLances(Map<String, Integer> lances) {
        this.lances = lances;
    }

    @Override
    public int TempoLeilao(int tempo) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'TempoLeilao'");
    }

   




   

   






}
