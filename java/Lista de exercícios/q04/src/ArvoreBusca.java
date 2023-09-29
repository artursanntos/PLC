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
            return new Node(value);
        }

        if (value >= node.value) {
            if (node.right == null) {
                node.lock.lock();
                node.right = new Node(value);
                node.lock.unlock();
                return node;
            } else {
                node.right = insert(node.right, value);
            }
        } else {
            if (node.left == null) {
                node.lock.lock();
                node.left = new Node(value);
                node.lock.unlock();
                return node;

            } else {
                node.left = insert(node.left, value);
            }
        }

        return node;
    }

    public static Node delete(Node root, int value) {

        if (root == null) return root;


        if (root.value > value) {
            root.left = delete(root.left, value);
            return root;
        } else if (root.value < value) {
            root.right = delete(root.right, value);
            return root;
        }

        root.lock.lock();
        if (root.left == null) {
            Node temp = root.right;
            root.lock.unlock();
            return temp;
        } else if (root.right == null) {
            Node temp = root.left;
            root.lock.unlock();
            return temp;
        } else {

            Node newSuccessor = root;

            Node succ = root.right;
            while (succ.left != null) {
                newSuccessor = succ;
                succ = succ.left;
            }


            if (newSuccessor != root)
                newSuccessor.left = succ.right;
            else
                newSuccessor.right = succ.right;


            root.value = succ.value;

            root.lock.unlock();
            return root;
        }
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
                for (int j = 0; j < 1000; j ++) {
                    int insertValue = random.nextInt(NUM_RANGE);
                    root = delete(root, insertValue);
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
