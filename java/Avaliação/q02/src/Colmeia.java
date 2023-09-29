import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Colmeia {
    int numOperarios;
    int numTasks;
    List<String> doneTasks;
    BlockingQueue<Task> allTasks;
    ExecutorService es;
    Lock mu;


    public Colmeia(int operarios, int tasks) {
        numOperarios = operarios;
        numTasks = tasks;
        es = Executors.newFixedThreadPool(operarios);
        mu = new ReentrantLock();
        allTasks = new LinkedBlockingQueue<>();
        doneTasks = new ArrayList<>();
    }

    // adiciona tasks à fila de tasks
    public void addTasks() throws InterruptedException {
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < this.numTasks; i++) {

            // Tratando entrada das tasks
            String entrada = scan.nextLine();
            String[] splitEntrada = entrada.split(" ");
            String id = splitEntrada[0];
            int tempo = Integer.parseInt(splitEntrada[1]);
            List<String> dependencies = Arrays.asList(splitEntrada).subList(2, splitEntrada.length);

            // Criando nova task e adicionando à fila de tasks
            Task task = new Task(id, tempo, dependencies, this);
            this.allTasks.put(task);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // Inicializando colmeia
        Scanner scan = new Scanner(System.in);
        String entrada = scan.nextLine();
        String[] splitEntrada = entrada.split(" ");
        int operarios = Integer.parseInt(splitEntrada[0]);
        int tasks = Integer.parseInt(splitEntrada[1]);

        // System.out.println("Operarios: " + operarios);
        // System.out.println("Tasks: " + tasks);
        Colmeia colmeia = new Colmeia(operarios, tasks);

        colmeia.addTasks();

        /* Printar as tasks da colmeia
        System.out.println("Tasks:\n");
        for (Task task : colmeia.allTasks) {
            System.out.println("Id: " + task.id);
            System.out.println("Tempo: " + task.time);
            System.out.println("Dependencias: " + task.dependencies);
            System.out.println("\n");
        }*/

        // Inicializando pool
        ExecutorService es = Executors.newFixedThreadPool(colmeia.numOperarios);

        // Executa enquanto ainda houver tasks
        while (!colmeia.allTasks.isEmpty()) {
            Task currentTask = colmeia.allTasks.take();
            if (currentTask.canBeExecuted()) {

                // Executa a task em uma instancia de operário
                es.execute(new Operario(currentTask));

                // Adiciona a task às tasks finalizadas
                colmeia.mu.lock();
                colmeia.doneTasks.add(currentTask.id);
                colmeia.mu.unlock();

            } else {
                // Joga para o fim da fila
                System.out.println(currentTask.id + " devolvida para a fila");
                colmeia.allTasks.put(currentTask);
            }
        }
        Thread.sleep(2000);
        es.shutdown();
        System.out.println("Todas as tarefas foram realizadas.");
    }
}
