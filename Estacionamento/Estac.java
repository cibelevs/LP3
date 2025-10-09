

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

//import java.util.concurrent.Semaphore;
/*
 * Você foi contratado pela empresa para implementar um sistema que gerencia um
estacionamento com vagas limitadas e controla o fluxo de veículos entrando e saindo.

Os veículos devem ser categorizados em NORMAL ou PRIORITARIO. O estacionamento
deve garantir que veículos prioritários tenham preferência no alocamento das vagas.

Além disso, o sistema deve registrar o log de entrada e saída de veículos, e um contador
de veículos que não conseguiram estacionar por falta de vaga.

REQUISITOS:
1. O estacionamento possui:
    a. 5 vagas regulares
    b. 2 vagas para idosos/deficientes (prioritárias)
    c. 1 portão de entrada (apenas 1 veículo por vez)
    d. 1 portão de saída (apenas 1 veículo por vez)
2. Use Semaphore para controlar:
    a. Vagas regulares disponíveis
    b. Vagas prioritárias disponíveis
    c. Acesso ao portão de entrada
    d. Acesso ao portão de saída
3. Use Executors para:
    a. Gerenciar chegada de veículos (FixedThreadPool com 4 threads)
    b. Gerenciar a chegada de 20 veículos. Os veículos devem ser gerados
    aleatoriamente sendo 30% prioritários e 70% normais.
    c. Simular permanência e saída dos veículos
4. Cada veículo deve:
    a. Esperar para entrar pelo portão (acquire portão entrada)
    b. Tentar conseguir uma vaga (prioritária primeiro, se aplicável)
    c. Permanecer estacionado por tempo aleatório (1-5 segundos)
    d. Sair pelo portão (acquire portão saída)
    e. Liberar todos os recursos ao sair
 
 */
public class Estac {
    private static int contadorSemVaga = 0;
    private static Semaphore vagaNorm = new Semaphore(5);
    private static Semaphore vagaPri = new Semaphore(2);
    private static Semaphore entrada = new Semaphore(1); //lock serve para sincronizar o acesso ao portão de entrada
    private static Semaphore saida = new Semaphore(1);
    private Veiculo veiculo;
    /*
        seria util criar uma lista de veiculos estacionados? private List<Veiculo> veiculosEstacionados; 
        nao, pois nao precisamos rastrear quais veiculos estao estacionados'
    */ 
    

    public static void main(String[] args) {
        // Implementação do sistema de estacionamento conforme os requisitos
        ExecutorService executor = Executors.newFixedThreadPool(4); // Pool de threads para gerenciar veículos  
        for (int i = 0; i < 20; i++) {
            boolean isPrioritario = Math.random() < 0.3; // 30% de chance de ser prioritário
            Veiculo veiculo = new Veiculo(i + 1, isPrioritario); //cria um novo veículo

            entrada.acquireUninterruptibly(); //espera para entrar pelo portão

            // Cada veículo é gerenciado por uma thread separada
            executor.execute(() -> {
                System.out.println("Veículo " + veiculo.getId() + "tentando acessar o estacionamento.");
                if(veiculo.getPrioritario()){
                    if(vagaPri.tryAcquire()){
                        System.out.println("Veículo prioritário " + veiculo.getId() + " conseguiu vaga prioritária.");
                        // a vaga prioritaria já é atualizada ao adquirir o semaphore
                        veiculo.setEstacionado(true); // colocar o status de estacionado como true
                    } else {
                        System.out.println("Vagas prioritárias cheias para veículo " + veiculo.getId() + ". Tentando vaga normal.");
                        if(vagaNorm.tryAcquire()){
                            System.out.println("Veículo prioritário " + veiculo.getId() + " conseguiu vaga normal.");
                            veiculo.setEstacionado(true); // colocar o status de estacionado como true
                        }
                        else {
                            System.out.println("Estacionamento cheio para veículo " + veiculo.getId() + ". Saindo sem estacionar.");
                            contadorSemVaga++;
                            veiculo.setEstacionado(false); // colocar o status de estacionado como false
                    }       
                }
                }else {
                    if(vagaNorm.tryAcquire()){
                        System.out.println("Veículo prioritário " + veiculo.getId() + " conseguiu vaga normal.");
                        veiculo.setEstacionado(true); // colocar o status de estacionado como true
                    } else {
                        System.out.println("Estacionamento cheio para veículo " + veiculo.getId() + ". Saindo sem estacionar.");
                        contadorSemVaga++;
                        veiculo.setEstacionado(false); // colocar o status de estacionado como false
                    }                        
                }
                //imprimirStatus();
            });

            try {
                int tempoEstacionado = 1000 + (int)(Math.random() * 3000);
                Thread.sleep(tempoEstacionado);    
                //int soma = 0;
                //soma += tempoEstacionado;
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(veiculo.getEstacionado()){
                saida.acquireUninterruptibly(); //espera para sair pelo portão
                if(veiculo.getPrioritario()){
                    vagaPri.release(); //libera a vaga prioritária
                } else {
                    vagaNorm.release(); //libera a vaga normal
                }
                veiculo.setEstacionado(false); // colocar o status de estacionado como false
                System.out.println("Veículo " + veiculo.getId() + " saiu do estacionamento.");
                saida.release(); //libera o portão de saída
            }
            entrada.release(); //libera o portão de entrada
        

        
        }


        imprimirStatusFinal();
    }



    public static void imprimirStatus(){
        System.out.println("\n*------------STATUS DO ESTACIONAMENTO------------*");
        System.out.println("Vagas regulares disponiveis: " );
        System.out.println("Vagas prioritárias disponíveis: ");
        System.out.println("Total de entradas: ");
        System.out.println("Total de desistências: \n");
    }


    

    public static void imprimirStatusFinal(){
        System.out.println("\n*------------FIM DO DIA------------*");
        System.out.println("*------------STATUS DO ESTACIONAMENTO------------*");
        System.out.println("Total de veiculos: 20");
        System.out.println("Conseguiram entrar: ");
        System.out.println("Desistiram: ");
        System.out.println("Taxa de sucesso: ");
        System.out.println("Tempo medio de permanencia: ");
        System.out.println("Tempo mínimo de permanencia: ");
        System.out.println("Tempo máximo de permanencia: ");
        System.out.println("Taxa de ocupação: ");
        System.out.println("----------------------------------------------------\n");
        System.out.println("SIMULAÇÃO FINALIZADA");
    }

   





}

