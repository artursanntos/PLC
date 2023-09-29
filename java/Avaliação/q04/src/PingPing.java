import javax.sound.midi.Receiver;
import java.util.concurrent.*;

public class PingPing {

    static int NUM_MESSAGES = 10;
    private BlockingQueue<String> queue;

    // Arrat tem tamanho do numero de mensagens
    public PingPing() {
        queue = new ArrayBlockingQueue<>(NUM_MESSAGES);
    }

    public void Sender() {
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

    public void Receiver() {
        for (int i = 0; i < NUM_MESSAGES; i++) {
            try {
                String messageReceived = queue.take();
                System.out.println("Mensagem recebida: " + messageReceived);
            } catch (InterruptedException e){}
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PingPing pingping = new PingPing();
        Thread sender = new Thread(pingping::Sender);
        Thread receiver = new Thread(pingping::Receiver);

        sender.start();
        receiver.start();

        sender.join();
        receiver.join();

        System.out.println("Comunicacao finalizada");
    }
}
