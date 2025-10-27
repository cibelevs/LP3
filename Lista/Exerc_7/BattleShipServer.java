package Lista.Exerc_7;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * BattleshipServer.java
 *
 * Servidor TCP simples para um jogo de Batalha Naval (2 jogadores).
 * - Aceita duas conexões de cliente.
 * - Gera um tabuleiro simples (5x5) com alguns navios unitários para cada jogador.
 * - Coordena turnos: pergunta ao jogador ativo por "MOVE x y".
 * - Responde com: "HIT", "MISS", "SUNK", "WIN", "INVALID", e envia mensagens de status.
 *
 * Observações:
 * - É uma implementação educativa/simplificada, focada em como coordenar entrada/saída,
 *   threads e estado do jogo no servidor.
 */
public class BattleshipServer {

    private static final int PORT = 5555;
    private static final int BOARD_SIZE = 5;

    // Representa o estado do tabuleiro de um jogador.
    private static class Board {
        // 0 = vazio, 1 = navio intacto, 2 = navio atingido
        int[][] cells = new int[BOARD_SIZE][BOARD_SIZE];
        int remainingShips = 0;

        // coloca navios aleatórios unitários (simples)
        void placeRandomShips(int numShips, Random rnd) {
            int placed = 0;
            while (placed < numShips) {
                int r = rnd.nextInt(BOARD_SIZE);
                int c = rnd.nextInt(BOARD_SIZE);
                if (cells[r][c] == 0) {
                    cells[r][c] = 1;
                    placed++;
                }
            }
            remainingShips = numShips;
        }

        // processa um tiro; retorna "HIT","MISS","SUNK"
        String shoot(int r, int c) {
            if (r < 0 || r >= BOARD_SIZE || c < 0 || c >= BOARD_SIZE) return "INVALID";
            if (cells[r][c] == 1) {
                cells[r][c] = 2; // marcado como atingido
                remainingShips--;
                return "SUNK"; // como usamos navios unitários, HIT==SUNK
            } else if (cells[r][c] == 0) {
                return "MISS";
            } else { // já atingido
                return "INVALID";
            }
        }
    }

    // Classe que encapsula o estado do jogo entre dois jogadores
    private static class Game {
        final Board[] boards = new Board[2];
        int currentPlayer = 0; // 0 ou 1

        Game() {
            boards[0] = new Board();
            boards[1] = new Board();
        }

        // alterna turnos
        synchronized void switchTurn() {
            currentPlayer = 1 - currentPlayer;
        }

        synchronized int getCurrentPlayer() {
            return currentPlayer;
        }

        // verifica se alguém venceu
        synchronized boolean isWon() {
            return boards[0].remainingShips == 0 || boards[1].remainingShips == 0;
        }

        synchronized int getWinner() {
            if (boards[0].remainingShips == 0) return 1;
            if (boards[1].remainingShips == 0) return 0;
            return -1;
        }
    }

    // Cada cliente fica em sua própria thread com referência ao Game
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final Game game;
        private final int playerIndex; // 0 ou 1
        private final BufferedReader in;
        private final PrintWriter out;

        ClientHandler(Socket socket, Game game, int playerIndex) throws IOException {
            this.socket = socket;
            this.game = game;
            this.playerIndex = playerIndex;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }

        // envia mensagem de servidor para cliente
        void send(String msg) {
            out.println(msg);
        }

        @Override
        public void run() {
            try {
                send("WELCOME Player " + (playerIndex + 1));
                send("WAITING_FOR_OPPONENT");

                // loop principal: espera o jogo começar e depois processa comandos
                while (!game.isWon()) {
                    // se é o jogador atual, pedimos movimento
                    if (game.getCurrentPlayer() == playerIndex) {
                        send("YOUR_TURN"); // sinal para cliente
                        // lê comando do cliente
                        String line = in.readLine();
                        if (line == null) {
                            System.out.println("Player " + (playerIndex + 1) + " disconnected.");
                            break;
                        }
                        line = line.trim();
                        // comando esperado: MOVE x y
                        if (line.toUpperCase().startsWith("MOVE")) {
                            String[] parts = line.split("\\s+");
                            if (parts.length == 3) {
                                try {
                                    int r = Integer.parseInt(parts[1]);
                                    int c = Integer.parseInt(parts[2]);
                                    // atira no tabuleiro do oponente
                                    int opponent = 1 - playerIndex;
                                    String result;
                                    synchronized (game) {
                                        result = game.boards[opponent].shoot(r, c);
                                        // se result == SUNK e remainingShips==0 -> WIN
                                        if (result.equals("SUNK") && game.boards[opponent].remainingShips == 0) {
                                            // anuncie para ambos
                                            send("WIN");
                                        }
                                    }
                                    send("RESULT " + result);
                                    // notifica o outro jogador informando que oponente moveu
                                    // (no servidor central este canal será visto pela outra thread quando ela ler)
                                    // troca de turno
                                    game.switchTurn();
                                } catch (NumberFormatException e) {
                                    send("INVALID Command: coordinates must be integers");
                                }
                            } else {
                                send("INVALID Command format. Use: MOVE row col");
                            }
                        } else {
                            send("INVALID Unknown command. Use: MOVE row col");
                        }
                    } else {
                        // Não é o turno: informe e espere uma notificação mínima para rechecagem
                        send("OPPONENT_TURN");
                        // aguardamos um pequeno intervalo para evitar busy-wait intenso.
                        Thread.sleep(200);
                    }
                }

                // após fim de jogo, informe se você venceu/perdeu
                if (game.isWon()) {
                    int winner = game.getWinner();
                    if (winner == playerIndex) {
                        send("GAME_OVER YOU_WIN");
                    } else {
                        send("GAME_OVER YOU_LOSE");
                    }
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Erro client handler: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }

    // servidor principal
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("BattleshipServer started on port " + PORT);
        System.out.println("Waiting for 2 players to connect...");

        // cria game e posiciona navios
        Game game = new Game();
        Random rnd = new Random();
        // definimos 3 navios por jogador para exemplo
        game.boards[0].placeRandomShips(3, rnd);
        game.boards[1].placeRandomShips(3, rnd);

        // aceita 2 conexões
        Socket p1 = serverSocket.accept();
        System.out.println("Player 1 connected.");
        ClientHandler ch1 = new ClientHandler(p1, game, 0);

        Socket p2 = serverSocket.accept();
        System.out.println("Player 2 connected.");
        ClientHandler ch2 = new ClientHandler(p2, game, 1);

        // cria executor para rodar ambos
        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.submit(ch1);
        exec.submit(ch2);

        // aguarda fim da partida
        exec.shutdown();
    }

    public static void main(String[] args) {
        try {
            new BattleshipServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

