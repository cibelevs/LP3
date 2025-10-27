package Lista.Exerc_2;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    // Define a porta onde o servidor vai escutar conexões
    private static final int PORT = 1234;
    // Define o diretório onde os arquivos recebidos serão salvos
    private static final String SAVE_DIR = "uploads";

    public static void main(String[] args) {
        // Cria um ServerSocket que escuta na porta especificada
        // O try-with-resources garante que o socket será fechado automaticamente
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de arquivos aguardando conexões na porta " + PORT);

            // Cria o diretório de uploads se ele não existir
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                dir.mkdir(); // Cria o diretório
            }

            // Loop infinito para aceitar múltiplos clientes
            while (true) {
                // Aguarda e aceita uma conexão de cliente (método bloqueante)
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                // Para cada cliente, cria uma nova thread para processá-lo
                // Isso permite que o servidor atenda múltiplos clientes simultaneamente
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método que processa a comunicação com um cliente específico
    private static void handleClient(Socket socket) {
        // DataInputStream permite ler tipos primitivos (UTF, long, etc) de forma conveniente
        try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            
            // Lê o nome do arquivo enviado pelo cliente
            String fileName = dis.readUTF();
            // Lê o tamanho do arquivo em bytes
            long fileSize = dis.readLong();

            System.out.println("Recebendo arquivo: " + fileName + " (" + fileSize + " bytes)");

            // Cria um objeto File representando onde o arquivo será salvo
            File file = new File(SAVE_DIR, fileName);

            // FileOutputStream para escrever os bytes do arquivo no disco
            try (FileOutputStream fos = new FileOutputStream(file)) {
                // Buffer de 4KB para leitura/escrita eficiente
                byte[] buffer = new byte[4096];
                int read; // Quantidade de bytes lidos em cada operação
                long totalRead = 0; // Total de bytes recebidos até o momento

                // Lê dados do cliente e escreve no arquivo
                while ((read = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, read); // Escreve os bytes lidos no arquivo
                    totalRead += read; // Atualiza o contador
                    
                    // Para quando recebeu todos os bytes do arquivo
                    if (totalRead >= fileSize) break;
                }
            }

            System.out.println("Arquivo salvo em: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Garante que o socket será fechado mesmo se ocorrer erro
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}