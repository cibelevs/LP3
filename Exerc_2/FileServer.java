package Exerc_2;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private static final int PORT = 1234; // porta do servidor
    private static final String SAVE_DIR = "uploads"; // pasta onde os arquivos serão salvos

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de arquivos aguardando conexões na porta " + PORT);

            // Criar diretório se não existir
            File dir = new File(SAVE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            DataInputStream dis = new DataInputStream(socket.getInputStream());
        ) {
            // Nome do arquivo
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            System.out.println("Recebendo arquivo: " + fileName + " (" + fileSize + " bytes)");

            File file = new File(SAVE_DIR, fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int read;
                long totalRead = 0;

                while ((read = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, read);
                    totalRead += read;
                    if (totalRead >= fileSize) break;
                }
            }

            System.out.println("Arquivo salvo em: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}

