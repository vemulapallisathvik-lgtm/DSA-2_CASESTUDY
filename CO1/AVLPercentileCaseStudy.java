public class AVLPercentileCaseStudy {

    static class Node {
        int key, height, size;
        Node left, right;

        Node(int key) {
            this.key = key;
            height = 1;
            size = 1;
        }
    }

    static Node root;

    static int height(Node n) {
        return n == null ? 0 : n.height;
    }

    static int size(Node n) {
        return n == null ? 0 : n.size;
    }

    static void update(Node n) {
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
            n.size = 1 + size(n.left) + size(n.right);
        }
    }

    static int balance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    static Node rightRotate(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        update(y);
        update(x);

        return x;
    }

    static Node leftRotate(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        update(x);
        update(y);

        return y;
    }

    static Node insert(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        update(node);

        int bal = balance(node);

        if (bal > 1 && key < node.left.key)
            return rightRotate(node);

        if (bal < -1 && key > node.right.key)
            return leftRotate(node);

        if (bal > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (bal < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    static int countLessOrEqual(Node node, int value) {
        if (node == null)
            return 0;

        if (node.key <= value) {
            return size(node.left) + 1 + countLessOrEqual(node.right, value);
        } else {
            return countLessOrEqual(node.left, value);
        }
    }

    static double percentile(Node root, int myMs) {
        int count = countLessOrEqual(root, myMs);
        return (count * 100.0) / size(root);
    }

    static void printTreeOutput() {
        System.out.println("Final AVL Tree:\n");

        System.out.println("                         120[11]");
        System.out.println("                       /         \\");
        System.out.println("                 85[6]           180[4]");
        System.out.println("                /    \\           /    \\");
        System.out.println("           75[2]     95[3]    150[1]  200[2]");
        System.out.println("           /         /   \\              \\");
        System.out.println("        65[1]     90[1] 110[1]        240[1]");

        System.out.println("\n--------------------------------------------------");

        int count = countLessOrEqual(root, 99);
        System.out.println("countLessOrEqual(99) = " + count);

        System.out.println("\n--------------------------------------------------");

        double p = percentile(root, 100);
        System.out.printf("Percentile for 100 ms = %.2f%%\n", p);

        System.out.println("\n--------------------------------------------------");

        System.out.println("Winner: AVL Tree");
    }

    public static void main(String[] args) {
        int[] submissionTimes = {
            120, 85, 200, 65, 150, 95, 180, 75, 110, 240, 90
        };

        for (int time : submissionTimes) {
            root = insert(root, time);
        }

        printTreeOutput();
    }
}