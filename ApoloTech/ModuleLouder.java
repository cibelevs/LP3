package ApoloTech;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* Crie uma classe ModuleLoader que implemente Runnable. Cada instância desta
classe representa o carregamento de um módulo (e.g., “Configuração”,
“Segurança”, “Logs”, “Cache”).
a. O run() deve simular um atraso no carregamento (e.g., Thread.sleep(1000)
a 4000 milissegundos).
b. Após o atraso, deve imprimir a mensagem “Módulo [Nome do Módulo]
carregado.” E, em seguida, chamar latch.countDown() para sinalizar sua
conclusão.*/

/* No método principal (main):
a. Crie um ExecutorService (e.g., Executors.newCachedThreadPool()).
b. Instancie o ServerInitializer com o CountDownLatch compartilhado.
c. Submeta 4 instâncias do ModuleLoader ao Executor.
d. Submeta a tarefa ServerInitializer.startServer() ao Executor imediatamente
após a submissão dos ModuleLoaders.
e. A mensagem do servidor só pode aparecer após as 4 mensagens de
módulo carregado.*/

public class ModuleLouder implements Runnable {
    private String moduloNome;
    private int tempoCarregamento; // em milissegundos
    private ServerInitializer serverInitializer;


    public ModuleLouder(String moduleName, int loadTime, ServerInitializer serverInitializer) {
        this.moduloNome = moduleName;
        this.tempoCarregamento = loadTime;      
        this.serverInitializer = serverInitializer;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerInitializer serverInitializer = new ServerInitializer();
        executor.submit(new ModuleLouder("Configuração", 6000, serverInitializer));
        executor.submit(new ModuleLouder("Cache", 9000, serverInitializer));
        executor.submit(new ModuleLouder("Chaves de Criptografia", 12000, serverInitializer));
        executor.submit(new ModuleLouder("Conexão de Log", 4000, serverInitializer));
        executor.submit(() -> serverInitializer.startServer());
        executor.shutdown();
    }



    @Override   
    public void run() {
        try {
            Thread.sleep(tempoCarregamento);
            System.out.println("Módulo " + moduloNome + " carregado.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Sinaliza que o módulo foi carregado
            getServerInitializer().getLatch().countDown();
        }
    }

    //gets e sets 
    public String getModuloNome() {
        return moduloNome;
    }
    public void setModuloNome(String moduleName) {
        this.moduloNome = moduleName;
    }
    public int getTempoCarregamento() {
        return tempoCarregamento;    
    }

    public void setTempoCarregamento(int loadTime) {
        this.tempoCarregamento = loadTime;
    }
    public ServerInitializer getServerInitializer() {
        return serverInitializer;
    }
    public void setServerInitializer(ServerInitializer serverInitializer) {
        this.serverInitializer = serverInitializer;
    }

}
