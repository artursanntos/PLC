import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    int value;
    Node left, right;
    ReentrantLock lock;


    public Node(int value) {
        this.value = value;
        left = right = null;
        lock = new ReentrantLock();
    }
}
