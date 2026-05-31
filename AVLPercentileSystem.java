import java.util.*;

class CountNode {

    int execMs;
    int height;
    int size;

    CountNode left, right;

    CountNode(int t) {
        execMs = t;
        height = 1;
        size = 1;
    }
}

public class AVLPercentileSystem {

    static int height(CountNode n) {
        return n == null ? 0 : n.height;
    }

    static int sizeOf(CountNode n) {
        return n == null ? 0 : n.size;
    }

    static void update(CountNode n) {
        if (n == null)
            return;

        n.height = 1 + Math.max(height(n.left), height(n.right));
        n.size = 1 + sizeOf(n.left) + sizeOf(n.right);
    }

    static int balanceFactor(CountNode n) {
        if (n == null)
            return 0;

        return height(n.left) - height(n.right);
    }

    static CountNode rightRotate(CountNode y) {
        CountNode x = y.left;
        CountNode t2 = x.right;

        x.right = y;
        y.left = t2;

        update(y);
        update(x);

        return x;
    }

    static CountNode leftRotate(CountNode x) {
        CountNode y = x.right;
        CountNode t2 = y.left;

        y.left = x;
        x.right = t2;

        update(x);
        update(y);

        return y;
    }

    static CountNode insert(CountNode root, int key) {
        if (root == null)
            return new CountNode(key);

        if (key < root.execMs)
            root.left = insert(root.left, key);
        else if (key > root.execMs)
            root.right = insert(root.right, key);
        else
            return root;

        update(root);

        int bf = balanceFactor(root);

        if (bf > 1 && key < root.left.execMs)
            return rightRotate(root);

        if (bf < -1 && key > root.right.execMs)
            return leftRotate(root);

        if (bf > 1 && key > root.left.execMs) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (bf < -1 && key < root.right.execMs) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    static int countLessOrEqual(CountNode root, int v) {
        int count = 0;
        CountNode cur = root;

        while (cur != null) {
            if (v < cur.execMs) {
                cur = cur.left;
            } else {
                count += sizeOf(cur.left) + 1;
                cur = cur.right;
            }
        }

        return count;
    }

    static double percentile(CountNode root, int myMs) {
        int n = sizeOf(root);

        if (n == 0)
            return 100.0;

        int better = countLessOrEqual(root, myMs - 1);

        return 100.0 * (n - better) / n;
    }

    static void inorder(CountNode root) {
        if (root == null)
            return;

        inorder(root.left);

        System.out.println(
                "Value: " + root.execMs +
                "  Size: " + root.size +
                "  Height: " + root.height);

        inorder(root.right);
    }

    static void printTree(CountNode root, int space) {
        if (root == null)
            return;

        space += 8;

        printTree(root.right, space);

        System.out.println();

        for (int i = 8; i < space; i++)
            System.out.print(" ");

        System.out.println(root.execMs + "[" + root.size + "]");

        printTree(root.left, space);
    }

    public static void main(String[] args) {
        int[] submissions = {
                120, 85, 200, 65, 150,
                95, 180, 75, 110, 240, 90
        };

        CountNode root = null;

        for (int x : submissions) {
            root = insert(root, x);
        }

        System.out.println("FINAL AVL TREE:\n");

        printTree(root, 0);

        System.out.println("\n\nINORDER TRAVERSAL:\n");

        inorder(root);

        int mySubmission = 100;

        double p = percentile(root, mySubmission);

        System.out.println("\nSubmission: " + mySubmission + " ms");

        System.out.printf("Percentile = %.2f%%\n", p);
    }
}