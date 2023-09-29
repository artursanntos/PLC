public class Operario implements Runnable {
    Task task;

    public Operario(Task task){
        this.task = task;
    }

    // executa a task
    public void run() {
        try {
            Thread.sleep(task.time);
            System.out.println("Tarefa " + task.id + " feita!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
