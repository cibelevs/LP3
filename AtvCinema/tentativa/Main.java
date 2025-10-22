
public class Main {
    public static void main(String[] args) {
        Cinema cinema = new Cinema();

        // Cria 15 clientes para disputar 10 lugares
        for (int i = 1; i <= 15; i++) {
            new Cliente(i, cinema).start();
        }

        // Espera um pouco antes de mostrar o resultado final
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cinema.exibirLugares();
    }
}