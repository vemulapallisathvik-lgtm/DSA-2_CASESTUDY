import java.util.*;

class Node {
    String name;
    int score;
    Node[] forward;

    Node(String name, int score, int level) {
        this.name = name;
        this.score = score;
        this.forward = new Node[level + 1];
    }
}

public class RedisZSetSkipListCaseStudy {

    static final int MAX_LEVEL = 3;
    Node head = new Node("HEAD", Integer.MIN_VALUE, MAX_LEVEL);

    public void insert(String name, int score, int level) {
        Node[] update = new Node[MAX_LEVEL + 1];
        Node current = head;

        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.forward[i] != null &&
                   current.forward[i].score < score) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        Node newNode = new Node(name, score, level);

        for (int i = 0; i <= level; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
    }

    public void displaySkipList() {
        System.out.println("===== Final Skip List (Level-by-Level) =====\n");

        for (int i = MAX_LEVEL; i >= 0; i--) {
            System.out.print("L" + i + " : -∞");
            Node current = head.forward[i];

            while (current != null) {
                System.out.print(" -> " + current.name + "(" + current.score + ")");
                current = current.forward[i];
            }

            System.out.println();
        }
    }

    public void search(int score) {
        System.out.println("\n--------------------------------------------");
        System.out.println("Search for score = " + score + " (Alice)\n");

        Node current = head;

        for (int i = MAX_LEVEL; i >= 0; i--) {
            System.out.print("Level " + i + " : ");

            while (current.forward[i] != null &&
                   current.forward[i].score < score) {
                System.out.print(current.forward[i].name + "(" +
                        current.forward[i].score + ") -> ");
                current = current.forward[i];
            }

            if (current.forward[i] != null &&
                current.forward[i].score == score) {
                System.out.println(current.forward[i].name + "(" +
                        current.forward[i].score + ")  (found)");
            } else if (current.forward[i] != null) {
                System.out.println(current.forward[i].name + "(" +
                        current.forward[i].score + ")  (" +
                        current.forward[i].score + " > " + score +
                        ") -> drop down");
            } else {
                System.out.println("NULL -> drop down");
            }
        }

        System.out.println("--------------------------------------------");
        System.out.println("RESULT: Found Alice with score = " + score);
    }

    public static void main(String[] args) {
        RedisZSetSkipListCaseStudy skipList =
                new RedisZSetSkipListCaseStudy();

        skipList.insert("Alice", 1200, 1);
        skipList.insert("Bob", 980, 0);
        skipList.insert("Carol", 1450, 1);
        skipList.insert("Dave", 870, 0);
        skipList.insert("Eve", 1100, 1);
        skipList.insert("Frank", 1300, 3);
        skipList.insert("Grace", 950, 1);
        skipList.insert("Henry", 1380, 2);

        skipList.displaySkipList();
        skipList.search(1200);
    }
}