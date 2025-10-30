package Lista.Exerc_6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Serv {
    private static final int PORTA = 12345;
    
    // Mapa para armazenar os votos de cada opção (opção -> quantidade de votos)
    private static final Map<String, Integer> votos = new ConcurrentHashMap<>();
    
    // Set para controlar quais clientes já votaram (IP + porta)
    private static final Set<String> clientesQueVotaram = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR DE VOTAÇÃO INICIADO ===");
        System.out.println("Aguardando conexões na porta " + PORTA + "...");
        
        // Inicializa as opções de votação com zero votos
        inicializarOpcoes();

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            // Loop principal do servidor - aceita múltiplos clientes
            while (true) {
                // Aguarda conexão de um cliente (método bloqueante)
                Socket clienteSocket = serverSocket.accept();
                
                // Obtém informações do cliente conectado
                String enderecoCliente = clienteSocket.getInetAddress().getHostAddress() + ":" + clienteSocket.getPort();
                System.out.println("Cliente conectado: " + enderecoCliente);
                
                // Cria uma nova thread para atender cada cliente
                // Isso permite que múltiplos clientes sejam atendidos simultaneamente
                new Thread(() -> atenderCliente(clienteSocket, enderecoCliente)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inicializa as opções de votação disponíveis
     */
    private static void inicializarOpcoes() {
        votos.put("Java", 0);
        votos.put("Python", 0);
        votos.put("C++", 0);
        votos.put("JavaScript", 0);
        votos.put("Go", 0);
        System.out.println("Opções de votação inicializadas: " + votos.keySet());
    }

    /**
     * Método que processa a comunicação com um cliente específico
     * @param socket Socket do cliente
     * @param enderecoCliente Endereço do cliente para controle de votação
     */
    private static void atenderCliente(Socket socket, String enderecoCliente) {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Envia mensagem de boas-vindas ao cliente
            saida.println("=== SISTEMA DE VOTAÇÃO ===");
            saida.println("Bem-vindo! Opções disponíveis: Java, Python, C++, JavaScript, Go");
            
            // Verifica se este cliente já votou
            boolean jaVotou = clientesQueVotaram.contains(enderecoCliente);
            
            if (jaVotou) {
                saida.println("AVISO: Você já votou anteriormente. Apenas um voto por cliente é permitido.");
            } else {
                saida.println("Digite 'VOTAR <opcao>' para votar (ex: VOTAR Java)");
            }
            
            saida.println("Digite 'RESULTADOS' para ver os resultados atualizados");
            saida.println("Digite 'SAIR' para encerrar a conexão");
            saida.println("=====================================");

            String mensagemCliente;
            // Loop para processar múltiplos comandos do mesmo cliente
            while ((mensagemCliente = entrada.readLine()) != null) {
                System.out.println("Comando recebido de " + enderecoCliente + ": " + mensagemCliente);
                
                // Processa o comando do cliente
                String resposta = processarComando(mensagemCliente, enderecoCliente, jaVotou);
                saida.println(resposta);
                
                // Se o comando foi SAIR, encerra a conexão
                if (mensagemCliente.equalsIgnoreCase("SAIR")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erro na comunicação com cliente " + enderecoCliente + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Conexão encerrada com cliente: " + enderecoCliente);
            } catch (IOException e) {
                System.err.println("Erro ao fechar socket do cliente " + enderecoCliente);
            }
        }
    }

    /**
     * Processa os comandos recebidos do cliente
     * @param comando Comando enviado pelo cliente
     * @param enderecoCliente Endereço do cliente para controle
     * @param jaVotou Indica se o cliente já votou anteriormente
     * @return Resposta a ser enviada ao cliente
     */
    private static String processarComando(String comando, String enderecoCliente, boolean jaVotou) {
        // Converte o comando para maiúsculas para facilitar a comparação
        String comandoUpper = comando.toUpperCase();
        
        if (comandoUpper.startsWith("VOTAR ")) {
            return processarVoto(comando, enderecoCliente, jaVotou);
        } else if (comandoUpper.equals("RESULTADOS")) {
            return obterResultados();
        } else if (comandoUpper.equals("SAIR")) {
            return "Obrigado por usar o sistema de votação!";
        } else {
            return "Comando inválido. Comandos disponíveis: VOTAR, RESULTADOS, SAIR";
        }
    }

    /**
     * Processa um voto do cliente
     * @param comando Comando de voto completo
     * @param enderecoCliente Endereço do cliente
     * @param jaVotou Indica se o cliente já votou
     * @return Mensagem de confirmação ou erro
     */
    private static String processarVoto(String comando, String enderecoCliente, boolean jaVotou) {
        // Verifica se o cliente já votou
        if (jaVotou) {
            return "ERRO: Você já realizou seu voto. Não é permitido votar mais de uma vez.";
        }
        
        // Extrai a opção do comando (remove "VOTAR " do início)
        String opcao = comando.substring(6).trim();
        
        // Verifica se a opção é válida
        if (!votos.containsKey(opcao)) {
            return "ERRO: Opção '" + opcao + "' não encontrada. Opções válidas: " + votos.keySet();
        }
        
        // Registra o voto (operação thread-safe)
        synchronized (votos) {
            votos.put(opcao, votos.get(opcao) + 1);
        }
        
        // Marca o cliente como tendo votado
        clientesQueVotaram.add(enderecoCliente);
        
        System.out.println("Voto registrado: " + enderecoCliente + " votou em " + opcao);
        return "SUCESSO: Seu voto em '" + opcao + "' foi registrado! Obrigado por votar.";
    }

    /**
     * Gera uma string com os resultados atualizados da votação
     * @return String formatada com os resultados
     */
    private static String obterResultados() {
        StringBuilder resultados = new StringBuilder();
        resultados.append("=== RESULTADOS DA VOTAÇÃO ===\n");
        
        // Calcula o total de votos
        int totalVotos = votos.values().stream().mapToInt(Integer::intValue).sum();
        
        // Ordena as opções por número de votos (decrescente)
        votos.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                String opcao = entry.getKey();
                int votosOpcao = entry.getValue();
                double percentual = totalVotos > 0 ? (votosOpcao * 100.0) / totalVotos : 0;
                
                resultados.append(String.format("%-12s: %2d votos (%5.1f%%)\n", 
                    opcao, votosOpcao, percentual));
            });
        
        resultados.append("-----------------------------\n");
        resultados.append("Total de votos: ").append(totalVotos).append("\n");
        resultados.append("Total de votantes: ").append(clientesQueVotaram.size());
        
        return resultados.toString();
    }
}