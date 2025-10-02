package ApoloTech;
import java.util.concurrent.CountDownLatch;

/**A Apolo Tech é responsável por um microsserviço crítico de autenticação. Para garantir
    a segurança e o desempenho, o microsserviço principal (AuthService) só pode se liga

    porta (SocketBind) e começar a aceitar requisições externas depois que seus quatro
    módulos essenciais de inicialização forem completamente carregados. Se o servidor
    iniciar antes da conclusão de qualquer um desses módulos (Configuração, Cache,
    Chaves de Criptografia, e Conexão de Log), ele pode falhar ou expor vulnerabilidades de
    segurança. Sabendo que o módulo de configuração leva em média 6 segundos para ser
    carregado, o de cache leva em média 9 segundos, o de chaves de criptografia em média
    12 segundos e o da conexão de log em média 4 segundos.
    A Apolo Tech precisa de uma solução robusta e concorrente para gerenciar essa
    dependência. Desta forma ela te contratou com o intuito de solucionar o seu problema,
    para isso você irá implementar um sistema de inicialização de servidor que só inicia a
    aceitação de conexões (simulada) depois que todos os módulos de configuração e
    segurança forem carregados.
    Requisitos
    1. Crie uma classe ServerInitializer que:
    a. Contém uma instância de CountDownLatch inicializada com o valor 4.
    b. Possui um método waitForInitialization() que chama latch.await().
    c. Possui um método startServer() que imprime a mensagem “Servidor
    Principal Online: Pronto para aceitar conexões (Socket.bind())” após o
    await() ser liberado.
*/

public class ServerInitializer {
    private static CountDownLatch latch = new CountDownLatch(4);


    /* 
    public static void main(String[] args) {

       Thread configModule = new Thread(() -> {
            try {
                System.out.println("Iniciando módulo de Configuração...");
                Thread.sleep(6000); // Simula o tempo de carregamento
                System.out.println("Módulo de Configuração carregado.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }); 
    }

        Aqui foi retirado pois esta criando várias instâncias do CountDownLatch
        e cada instância tem seu próprio contador, o que não é o comportamento desejado.

    */
    public void waitForInitialization() {
        try {
            latch.await(); // Aguarda até que todos os módulos sejam carregados
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Simula a inicialização do servidor principal
    public void startServer() {
        waitForInitialization();
        System.out.println("Servidor Principal Online: Pronto para aceitar conexões (Socket.bind())");
    }

    public CountDownLatch getLatch(){
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        ServerInitializer.latch = latch;
    }
}
