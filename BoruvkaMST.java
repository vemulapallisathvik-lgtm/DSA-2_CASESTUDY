import java.util.ArrayList;
import java.util.List;

class Edge {

    int u, v;
    int weight;

    Edge(int u, int v, int w) {

        this.u = u;
        this.v = v;
        this.weight = w;
    }

    @Override
    public String toString() {

        return "(" + u + " - " + v + " : " + weight + ")";
    }
}

class UnionFind {

    int[] parent;
    int[] rank;

    UnionFind(int n) {

        parent = new int[n];
        rank = new int[n];

        for (int i = 0; i < n; i++) {

            parent[i] = i;
        }
    }

    // Path Compression
    int find(int x) {

        if (parent[x] != x) {

            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    // Union by Rank
    boolean union(int a, int b) {

        int ra = find(a);
        int rb = find(b);

        // already same component
        if (ra == rb)
            return false;

        // ensure ra has bigger rank
        if (rank[ra] < rank[rb]) {

            int temp = ra;
            ra = rb;
            rb = temp;
        }

        parent[rb] = ra;

        if (rank[ra] == rank[rb]) {

            rank[ra]++;
        }

        return true;
    }
}

public class BoruvkaMST {

    // Vertex mapping
    static final int NYC = 0;
    static final int LON = 1;
    static final int FRA = 2;
    static final int SIN = 3;
    static final int TOK = 4;
    static final int SYD = 5;
    static final int MUM = 6;
    static final int SAO = 7;

    static String[] names = {
            "NYC", "LON", "FRA", "SIN",
            "TOK", "SYD", "MUM", "SAO"
    };

    static List<Edge> boruvka(int n, List<Edge> edges) {

        UnionFind uf = new UnionFind(n);

        List<Edge> mst = new ArrayList<>();

        int components = n;

        int phase = 1;

        while (components > 1) {

            System.out.println("\n========== PHASE " + phase + " ==========");

            // cheapest edge for each component
            Edge[] cheapest = new Edge[n];

            // STEP 1:
            // Find cheapest outgoing edge for every component
            for (Edge e : edges) {

                int setU = uf.find(e.u);
                int setV = uf.find(e.v);

                // ignore internal edges
                if (setU == setV)
                    continue;

                // update cheapest for setU
                if (cheapest[setU] == null ||
                        e.weight < cheapest[setU].weight) {

                    cheapest[setU] = e;
                }

                // update cheapest for setV
                if (cheapest[setV] == null ||
                        e.weight < cheapest[setV].weight) {

                    cheapest[setV] = e;
                }
            }

            // print cheapest edges
            System.out.println("\nCheapest edges per component:");

            for (int i = 0; i < n; i++) {

                if (cheapest[i] != null) {

                    Edge e = cheapest[i];

                    System.out.println(
                            "Component " + i +
                            " -> " +
                            names[e.u] + " - " +
                            names[e.v] +
                            " ($" + e.weight + "M)"
                    );
                }
            }

            boolean progress = false;

            // STEP 2:
            // Add cheapest edges simultaneously
            for (int i = 0; i < n; i++) {

                Edge e = cheapest[i];

                if (e != null) {

                    if (uf.union(e.u, e.v)) {

                        mst.add(e);

                        components--;

                        progress = true;

                        System.out.println(
                                "\nAdded to MST: " +
                                names[e.u] + " - " +
                                names[e.v] +
                                " ($" + e.weight + "M)"
                        );

                        System.out.println(
                                "Components remaining = " +
                                components
                        );
                    }
                }
            }

            // safety
            if (!progress) {

                break;
            }

            phase++;
        }

        return mst;
    }

    public static void main(String[] args) {

        List<Edge> edges = new ArrayList<>();

        // Build graph

        edges.add(new Edge(NYC, LON, 4));
        edges.add(new Edge(NYC, FRA, 7));
        edges.add(new Edge(NYC, SAO, 5));

        edges.add(new Edge(LON, FRA, 2));
        edges.add(new Edge(LON, MUM, 9));

        edges.add(new Edge(FRA, MUM, 6));
        edges.add(new Edge(FRA, TOK, 11));

        edges.add(new Edge(SIN, TOK, 3));
        edges.add(new Edge(SIN, MUM, 4));

        edges.add(new Edge(TOK, SYD, 5));

        edges.add(new Edge(SYD, SIN, 6));

        edges.add(new Edge(MUM, SAO, 13));

        // Run Boruvka
        List<Edge> mst = boruvka(8, edges);

        // Print final MST
        System.out.println("\n========== FINAL MST ==========");

        int totalCost = 0;

        for (Edge e : mst) {

            System.out.println(
                    names[e.u] + " - " +
                    names[e.v] +
                    " : $" + e.weight + "M"
            );

            totalCost += e.weight;
        }

        System.out.println("\nTotal MST Cost = $" + totalCost + "M");

        /*
            COMPLEXITIES

            Per Phase:
            O(E)

            Number of Phases:
            O(log V)

            Overall:
            O(E log V)

            For this problem:

            V = 8
            E = 12

            Per Phase = O(12)
            Phases ≈ log2(8) = 3

            Total ≈ O(12 * 3) = O(36)
         */
    }
}