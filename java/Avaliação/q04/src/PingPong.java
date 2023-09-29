import java.util.concurrent.*;

public class PingPong {

    static int NUM_MESSAGES = 10;
    private BlockingQueue<String> queue;

    // Array só tem tamanho 1
    public PingPong() {
        queue = new ArrayBlockingQueue<>(1);
    }

    public void pingPongSender() {
        for (int i = 0; i < NUM_MESSAGES; i++) {
            try {
                String message = "Ping " + (i + 1);
                queue.put(message);
                System.out.println("Mensagem " + (i + 1) + " enviada");
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public void pingPongReceiver() {
        for (int i = 0; i < NUM_MESSAGES; i++) {
            try {
                String messageReceived = queue.take();
                System.out.println("Mensagem recebida: " + messageReceived);
            } catch (InterruptedException e){}
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PingPong pingpong = new PingPong();
        Thread sender = new Thread(pingpong::pingPongSender);
        Thread receiver = new Thread(pingpong::pingPongReceiver);

        sender.start();
        receiver.start();

        sender.join();
        receiver.join();

        System.out.println("Comunicacao finalizada");
    }
}
