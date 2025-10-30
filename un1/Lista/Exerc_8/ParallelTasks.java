package Lista.Exerc_8;

import java.util.*;
import java.util.concurrent.*;

/**
 * ParallelTasks.java
 *
 * Exemplo simples de execução paralela:
 * - Cria um ExecutorService (fixed pool)
 * - Submete N Callables que retornam um Integer
 * - Coleta os Futures, espera conclusão e combina os resultados (soma)
 *
 * Cada tarefa simula trabalho via Thread.sleep e retorna um número.
 */
public class ParallelTasks {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // número de tarefas e tamanho do pool
        int numTasks = 8;
        int poolSize = 4;

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        List<Callable<Integer>> tasks = new ArrayList<>();
        Random rnd = new Random();

        // cria tarefas
        for (int i = 0; i < numTasks; i++) {
            final int taskId = i + 1;
            tasks.add(() -> {
                int workTimeMs = 500 + rnd.nextInt(1000); // 500-1500ms
                System.out.println("Task " + taskId + " started, will take " + workTimeMs + "ms");
                try {
                    Thread.sleep(workTimeMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                int result = taskId * 10; // resultado simbólico
                System.out.println("Task " + taskId + " finished with result " + result);
                return result;
            });
        }

        // submete todas e obtém futures
        List<Future<Integer>> futures = executor.invokeAll(tasks);

        // combina resultados (soma)
        int total = 0;
        for (Future<Integer> f : futures) {
            total += f.get(); // bloqueia até cada tarefa finalizar
        }

        executor.shutdown();

        System.out.println("Total combined result: " + total);
    }
}

