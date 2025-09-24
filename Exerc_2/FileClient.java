package Exerc_2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {
    private static final String SERVER = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        // Altere o caminho abaixo para um arquivo existente no seu PC
        File file = new File("teste.txt");

        try (
            Socket socket = new Socket(SERVER, PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(file);
        ) {
            // Enviar nome e tamanho do arquivo
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            // Enviar conteúdo
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }

            System.out.println("Arquivo enviado com sucesso: " + file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
