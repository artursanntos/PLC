import java.util.*;
import java.util.concurrent.locks.*;

public class Aeroporto {
    private int pistasDisponiveis;
    private final Lock lock;
    private final Condition pistaLivre;
    private final PriorityQueue<Aviao> filaAvioes;
    private long startTime;

    public Aeroporto(int pistasDisponiveis) {
        this.pistasDisponiveis = pistasDisponiveis;
        this.lock = new ReentrantLock();
        this.pistaLivre = lock.newCondition();
        this.filaAvioes = new PriorityQueue<>();
    }

    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }


    public void getPista(Aviao aviao) throws InterruptedException {

        while (elapsedTime() < aviao.getHoraEsperada()){
            // Não chegou a hora do avião - simula o voo, ou embarque de passageiros
        }
        lock.lock();
        try {

            // Checa se há espaço
            while (pistasDisponiveis == 0) {
                // System.out.println("Esperando... ");
                pistaLivre.await();
            }

            // Retira aviao da fila e pega a pista
            filaAvioes.poll();
            pistasDisponiveis--;

            // Caso ainda hajam pistas disponíveis, ele pode enviar um signal
            if (pistasDisponiveis > 0) {
                pistaLivre.signalAll();
            }
        } finally {
            lock.unlock();
        }

    }

    public void freePista() {
        lock.lock();
        try {
            // Libera a pista
            pistasDisponiveis++;
            pistaLivre.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void aviaoDecola(Aviao aviao) throws InterruptedException {
        getPista(aviao);
        System.out.println("Aviao decolou - Hora esperada: " + aviao.getHoraEsperada() +
                ", Hora real: " + elapsedTime() + ", Atraso: " +
                (elapsedTime() - aviao.getHoraEsperada()));

        // tempo utilizando a pista
        Thread.sleep(500);
        freePista();

    }

    public void aviaoAterrisa(Aviao aviao) throws InterruptedException {
        getPista(aviao);
        System.out.println("Aviao aterrissou - Hora esperada: " + aviao.getHoraEsperada() +
                ", Hora real: " + elapsedTime() + ", Atraso: " +
                (elapsedTime() - aviao.getHoraEsperada()));

        // tempo utilizando a pista
        Thread.sleep(500);
        freePista();

    }

    /*
    A entrada deve ser da seguinte forma:
    K - numero de pistas
    N - numero de avioes para decolar
    M - numero de avioes para pousar
    abaixo virao N tempos avioes para decolar e em seguida M tempos avioes para pousar

    exemplo:
    4
    4
    3
    500
    800
    300
    200
    500
    100
    400
     */

    public static void main(String[] args) throws InterruptedException {
        Scanner scan = new Scanner(System.in);

        // Inicializando aeroporto
        int numPistas = scan.nextInt();
        Aeroporto aeroporto = new Aeroporto(numPistas);

        // Identifica aviões para decolagem
        int avioesParaDecolar = scan.nextInt();
        int avioesParaPousar = scan.nextInt();
        int totalSize = avioesParaPousar + avioesParaDecolar;
        Thread[] threadsAvioes = new Thread[totalSize];

        // Instanciar threads dos aviões para decolar
        for (int i = 0; i < avioesParaDecolar; i++) {
            Aviao aviaoDecolagem = new Aviao(scan.nextLong());
            // Adicionando à fila dos avioes
            aeroporto.filaAvioes.add(aviaoDecolagem);
            threadsAvioes[i] = new Thread(() -> {
                try {
                    aeroporto.aviaoDecola(aviaoDecolagem);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }
        // Instanciar threads dos aviões para pousar
        for (int i = avioesParaDecolar; i < totalSize; i++) {
            Aviao aviaoPouso = new Aviao(scan.nextLong());
            // Adicionando à fila dos avioes
            aeroporto.filaAvioes.add(aviaoPouso);
            threadsAvioes[i] = new Thread(() -> {
                try {
                    aeroporto.aviaoAterrisa(aviaoPouso);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Tempo de início
        // System.out.println(aeroporto.elapsedTime());

        // Iniciando tempo de contagem
        aeroporto.startTime = System.currentTimeMillis();
        for (Thread t : threadsAvioes) {
            t.start();
        }
    }
}

// Classe de Aviao comparável para PriorityQueue
class Aviao implements Comparable<Aviao> {
    private final long horaEsperada;

    public Aviao(long horaEsperada) {
        this.horaEsperada = horaEsperada;
    }

    public long getHoraEsperada() {
        return horaEsperada;
    }

    // Definindo um método de compareTo baseado na horaEsperada
    @Override
    public int compareTo(Aviao o) {
        return Long.compare(this.horaEsperada, o.horaEsperada);
    }
}