import java.util.Random;


public class ArvoreBusca {
    static Node root;
    private static final int NUM_THREADS = 50;
    private static final int NUM_INSERTIONS_PER_THREAD = 2000;
    private static final int NUM_RANGE = 13000;

    static void inorder(Node root) {
        if (root != null) {
            inorder(root.left);
            System.out.print(root.value + " ");
            inorder(root.right);
        }
    }

    private static int getTotalNodesCount(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + getTotalNodesCount(node.left) + getTotalNodesCount(node.right);
    }

    public static Node insert(Node node, int value) {

        if (node == null) {
            Node newNode = new Node(value);
            return newNode;
        }
        node.lock.lock();
        if (value >= node.value) {
            node.right = insert(node.right, value);
        } else {
            node.left = insert(node.left, value);
        }
        node.lock.unlock();
        return node;
    }

    public static void main(String[] args) throws InterruptedException {

        ArvoreBusca tree = new ArvoreBusca();
        Thread[] threads = new Thread[NUM_THREADS];

        long start = System.currentTimeMillis();
        for (int z = 0; z < NUM_THREADS; z++) {
            threads[z] = new Thread(() -> {
                Random random = new Random();
                for (int i = 0; i < NUM_INSERTIONS_PER_THREAD; i ++) {
                    int insertValue = random.nextInt(NUM_RANGE);
                    root = insert(root, insertValue);
                }
            });
        }

        for (int j = 0; j < NUM_THREADS; j ++) {
            threads[j].start();
        }

        for (int i = 0; i < NUM_THREADS; i ++) {
            threads[i].join();
        }
        long end = System.currentTimeMillis();

        System.out.println("Elapsed time em milissegundos: " + (end - start));
        int nodesCount = getTotalNodesCount(root);
        System.out.println("Contagens de nos: " + nodesCount);

        // Imprimir arvore em ordem
        // inorder(root);

    }
}
