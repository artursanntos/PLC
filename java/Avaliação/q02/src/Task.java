import java.util.List;

class Task {
    String id;
    int time;
    List<String> dependencies;
    Colmeia colmeia;

    public Task(String id, int time, List<String> dependencies, Colmeia colmeia) {
        this.id = id;
        this.time = time;
        this.dependencies = dependencies;
        this.colmeia = colmeia;
    }

    public boolean canBeExecuted() {
        // Verifica se todas as dependências da tarefa foram concluídas
        for (String dependency : dependencies) {
            colmeia.mu.lock();
            if (!colmeia.doneTasks.contains(dependency)) {
                colmeia.mu.unlock();
                return false;
            }
            colmeia.mu.unlock();
        }

        return true;
    }
}
