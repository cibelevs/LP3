import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SistemaReservaCinema {
    
    public static void main(String[] args) throws InterruptedException {
        SalaCinema sala = new SalaCinema(10);
        
        System.out.println("SISTEMA DE RESERVA DE CINEMA COM FILA INICIADO");
        System.out.println("=".repeat(60));
        System.out.println("Total de assentos disponíveis: 10");
        System.out.println("=".repeat(60) + "\n");
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        String[] nomes = {"Ana", "Bruno", "Carlos", "Diana", "Eduardo", 
                         "Fernanda", "Gabriel", "Helena", "Igor", "Julia",
                         "Lucas", "Maria", "Nicolas", "Olivia", "Paulo"};
        
        Random random = new Random();
        
        System.out.println("Usuários chegando e entrando na fila...\n");
        
        for (String nome : nomes) {
            int assentoDesejado = random.nextInt(10) + 1;
            executor.submit(new Usuario(nome, sala, assentoDesejado));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        Thread.sleep(1000);
        
        System.out.println("\nFila completa! Total de pessoas aguardando: " + 
                           sala.tamanhoFila());
        sala.exibirStatus();
        sala.processarReservas();
        sala.exibirStatus();
        
        System.out.println("Sistema finalizado com sucesso!");
    }
}