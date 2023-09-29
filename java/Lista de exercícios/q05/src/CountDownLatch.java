import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatch {

    int counter;
    private final Lock lock = new ReentrantLock();
    private static final int NUM_THREADS = 20;
    private static final int COUNTER_SIZE = 13;

    private CountDownLatch(int value) {
        counter = value;
    }

    private synchronized void await(int id) {
        while (counter > 0){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error!");
            }
        }

        System.out.println("Thread " + id + " desbloqueada");
    }

    private synchronized void countDown() {
        lock.lock();
        counter = counter - 1;
        System.out.println("Contador decrementado: " + counter);
        lock.unlock();
        notifyAll();

    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(COUNTER_SIZE);
        Thread[] threads = new Thread[NUM_THREADS];

        for (int z = 0; z < NUM_THREADS; z++) {
            int id = z;
            threads[z] = threads[z] = new Thread(() -> {countDownLatch.await(id);});
            threads[z].start();
        }

        for (int i = 0; i < COUNTER_SIZE; i++ ) {
            countDownLatch.countDown();
        }

        for (int j = 0; j < NUM_THREADS; j++) {
            threads[j].join();
        }

    }
}
