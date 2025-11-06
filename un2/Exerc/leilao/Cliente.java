import java.rmi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // Importar java.util.Map
import java.util.Random;




class Cliente {
    public static void main (String args[]){
        try{
            String nomeObjetoRemoto = "rmi://localhost:5000/Interface_CLI_SERV"; 

            // 1. Acessa o objeto remoto.
            Interface_CLI_SERV objRemoto = (Interface_CLI_SERV) Naming.lookup(nomeObjetoRemoto);
            System.out.println("Conexão com o servidor RMI estabelecida.");
             
            
            //iniciar leilao
            int tempo = 300; // 5 minutos em segundos
            objRemoto.IniciarLeilao(tempo); // Inicia leilão por 300 segundos (5 minutos)
            System.out.println("Leilão iniciado por " + tempo + " segundos.");

            //  CRIAR E REGISTRAR O CALLBACK NO SERVIDOR
            CallbackCliente callback = new CallbackCliente();
            objRemoto.registrarParaNotificacao(callback);
            System.out.println("Registrado para receber notificações do leilão.");



            // =============== Testes dos métodos remotos ===============


            //  Teste  método listarLances 
            System.out.println("\n--- 1. Testando listarLances() (Inicial) ---");
            // retorno para Map<String, Integer>
            Map<String, Integer> lancesAtuais = objRemoto.listarLances();
            
            System.out.println("Lances atuais recebidos do servidor:");
            for (Map.Entry<String, Integer> entry : lancesAtuais.entrySet()) {
                System.out.println("  Código: " + entry.getKey() + ", Lance: " + entry.getValue());
            }

            //  Teste  metodo OfertarLance 
            System.out.println("\n Testando OfertarLance()");
            
            // Exemplo de Oferta )
            String codigoTeste1 = "NB67"; 
            int valorTeste1 = 9500;
            System.out.println("Tentando ofertar: Código: " + codigoTeste1 + ", Valor: " + valorTeste1);
            objRemoto.OfertarLance(codigoTeste1, valorTeste1);
            
            String codigoTeste2 = "AS21"; 
            int valorTeste2 = 3000;
            System.out.println("Tentando ofertar: Código: " + codigoTeste2 + ", Valor: " + valorTeste2);
            objRemoto.OfertarLance(codigoTeste2, valorTeste2);
            

            // --- Verificando os Lances Atualizados (Sua linha da imagem corrigida) ---
            System.out.println("\n--- 3. Verificando listarLances() após ofertas ---");
            
            //  Muda para Map<String, Integer>
            Map<String, Integer> lancesAtualizados = objRemoto.listarLances();
            
            System.out.println("Lances atualizados recebidos do servidor:");
            for (Map.Entry<String, Integer> entry : lancesAtualizados.entrySet()) {
                System.out.println("  Código: " + entry.getKey() + ", Lance: " + entry.getValue());
            }

           
            String codigoConsulta = "NB67"; // Código para consultar o maior lance
            System.out.println("\n--- 4. Testando maiorlance desse produto ---" + codigoConsulta);
            int  maiorLance = objRemoto.maiorlance_desse_produto(codigoConsulta);
            if (maiorLance > 0 ) {
                System.out.println("Maior lance recebido do servidor:");
                System.out.println( ", Lance: " + maiorLance);
            } else {
                System.out.println("Nenhum lance encontrado no servidor.");
            }

             System.out.println("\n--- 5. Testando maiorlance de todos produtos ---");
            
            int  maiorlance = objRemoto.maiorlance_de_todos_produtos();
            if (maiorlance > 0 ) {
                System.out.println("Maior lance recebido do servidor:");
                System.out.println( ", Lance: " + maiorlance);
            } else {
                System.out.println("Nenhum lance encontrado no servidor.");
            }   

          //  Teste  método listarLances 
           // Lista de produtos para receber lances
            String[] produtos = {"NB67", "AD29", "CL34"}; 
            int numClientes = 5;
            int lancesPorCliente = 50;
            List<Thread> threads = new ArrayList<>();

            //======================== THREADS SENDO CRIADAS AQUI =======================================

            for (int i = 0; i < numClientes; i++) {
                // Cada thread foca em um produto diferente (usando operador módulo)
                String produtoFoco = produtos[i % produtos.length]; 
                
                // Cria a tarefa de lance. 
                LanceTask task = new LanceTask(objRemoto, produtoFoco, lancesPorCliente);
                
                // Cria e inicia a thread
                Thread t = new Thread(task, "Cliente-Thread-" + (i + 1));
                threads.add(t);
                t.start();
            }


            // Espera todas as threads terminarem
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("\n--- TESTE DE MULTITHREADING CONCLUÍDO ---");


            // Reconsulta para ver o resultado da concorrência
            System.out.println("\n--- 6. Verificando lances FINAIS após o teste multithread ---");
            Map<String, Integer> lancesFinais = objRemoto.listarLances();
            
            System.out.println("Lances Finais Recebidos:");
            for (Map.Entry<String, Integer> entry : lancesFinais.entrySet()) {
                System.out.println("  Código: " + entry.getKey() + ", Lance: " + entry.getValue());
            }
            // FIM DO BLOCO TRY
        } catch(Exception e ) {
            System.err.println("Exceção no Cliente: " + e.toString());
            e.printStackTrace();
        }
    }


            //============== Teste de concorrência com múltiplas threads ==============


            // AQUI APENAS DETERMINAMOS O COMPORTAMENTO DA THREAD , POIS ELAS FORAM INICIADAS NO MAIN 


static class LanceTask implements Runnable { 
        private final Interface_CLI_SERV objRemoto;
        private final String codigoProduto;
        private final int numLances;

        public LanceTask(Interface_CLI_SERV objRemoto, String codigoProduto, int numLances) {
            this.objRemoto = objRemoto;
            this.codigoProduto = codigoProduto;
            this.numLances = numLances;
        }

        @Override
        public void run() {
            // ... (restante da lógica da LanceTask)
            Random rand = new Random();
            String threadName = Thread.currentThread().getName();
            
            System.out.println(threadName + " começou a dar lances no produto " + codigoProduto);
            
            for (int i = 0; i < numLances; i++) {
                try {
                    // Tenta pegar o lance atual para garantir que o próximo lance é maior que o mínimo (5%)
                    int lanceAtual = objRemoto.maiorlance_desse_produto(codigoProduto);
                    int lanceMinimo = (int) (lanceAtual * 1.05) + 1;
                    
                    // Gera um valor aleatório razoável para testar (ex: entre o lance mínimo e + 500)
                    int valorOfertado = lanceMinimo + rand.nextInt(500); 

                    boolean sucesso = objRemoto.OfertarLance(codigoProduto, valorOfertado);
                    
                    if (sucesso) {
                        System.out.println(threadName + ": Sucesso! Novo lance em " + codigoProduto + " = " + valorOfertado);
                    } else {
                        // O Servidor já imprime a recusa, não precisa de mais prints no cliente aqui
                    }
                    
                    // Pausa curta para simular latência de rede
                    Thread.sleep(rand.nextInt(50)); 
                    
                } catch (RemoteException e) {
                    System.err.println(threadName + ": Erro RMI - Servidor inacessível.");
                    break;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
   }

}








       
