// Autors: Roberts Groza rg11080

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Comparator;
import java.util.TreeSet;

public class PoohAdventure {
    public static void main(String[] args) {
        HundredAcreWoodGraph graph = new HundredAcreWoodGraph("input.txt");
        graph.printInfo();

        // Do stuff here
        graph.getPoohsSolution();

        graph.printResult();
        // graph.printVertexCyclicRoutes();
    }
}

// Paligklases
// TODO: Iznest sava faila

// Klase, kas reprezentes grafa virsotni max-heap kaudze
class Verticle {
    public int verticle;
    public int complexity;

    Verticle() {
    }

    Verticle(int verticle, int complexity) {
        this.verticle = verticle;
        this.complexity = complexity;
    }
}

// Comparator, lai pielagotu JAVA TreeSet musu nepieciesamibai - nemtu
// 'grutakas' virsotnes
class VerticleComparator implements Comparator<Verticle> {

    @Override
    public int compare(Verticle verticle0, Verticle verticle1) {
        return verticle1.complexity - verticle0.complexity;
    }
}

class Edge {
    public int verticleA;
    public int verticleB;
    public int complexity;

    Edge(int verticleA, int verticleB, int complexity) {
        this.verticleA = verticleA;
        this.verticleB = verticleB;
        this.complexity = complexity;
    }

    public boolean isSameEdgeAs(Edge edge) {
        return this.verticleA == edge.verticleA && this.verticleB == edge.verticleB
                || this.verticleA == edge.verticleB && this.verticleB == edge.verticleA;
    }
}

class HundredAcreWoodGraph {
    private int verticleCount; // grafa virsotnu skaits
    private ArrayList<LinkedList<Verticle>> adjacencyList; // Grafa reprezentacija saraksta
    private int[][] adjacencyMatrix;
    private ArrayList<Edge> easyEdges; // Skatunes, kuru sarezgitiba ir negativa vai 0
    private ArrayList<Edge> mstEdges; // Maximum spaning tree skautnes
    private int totalSum = 0;

    HundredAcreWoodGraph(String inputFileName) {
        adjacencyList = new ArrayList<LinkedList<Verticle>>();
        easyEdges = new ArrayList<Edge>();
        mstEdges = new ArrayList<Edge>();

        buildAdjacencyList(inputFileName);
    }

    /**
     * Izveido grafa reprezentaciju atmiņa no dota faila
     *
     * @param inputFileName
     */
    private void buildAdjacencyList(String inputFileName) {
        try {
            File inputFile = new File(inputFileName);
            Scanner fileReader = new Scanner(inputFile);

            verticleCount = fileReader.nextInt();

            for (int i = 0; i < verticleCount; i++) {
                adjacencyList.add(new LinkedList<Verticle>());
            }

            adjacencyMatrix = new int[verticleCount][verticleCount];

            for (int i = 0; i < verticleCount; i++) {
                for (int j = 0; j < verticleCount; j++) {
                    adjacencyMatrix[i][j] = -101;
                }
            }

            while (fileReader.hasNextInt()) {
                int verticleA = fileReader.nextInt();
                int verticleB = fileReader.nextInt();
                int complexity = fileReader.nextInt();

                // if (complexity > 0) {
                // totalSum += complexity;
                // adjacencyList.get(verticleA - 1).add(new Verticle(verticleB - 1,
                // complexity));
                // adjacencyList.get(verticleB - 1).add(new Verticle(verticleA - 1,
                // complexity));
                adjacencyMatrix[verticleA - 1][verticleB - 1] = complexity;
                adjacencyMatrix[verticleB - 1][verticleA - 1] = complexity;
                // } else {
                // easyEdges.add(new Edge(verticleA, verticleB, complexity));
                // }

                if (complexity < 1) {
                    easyEdges.add(new Edge(verticleA, verticleB, complexity));
                } else {
                    totalSum += complexity;
                }
            }

            fileReader.close();
        } catch (FileNotFoundException fileError) {
            System.out.println("Sagaidītais ieejas fails \"" + inputFileName + "\" nav atrasts!");
        }
    }

    public void getPoohsSolution() {
        // Ar Prima algoritmu iegustam Maximum spaning tree
        System.out.println("$$$$$$$$$$$$$$$$$");
        System.out.println("Prima algoritms :");
        // getMstEdges();
        kruskalsMst();

        // No kopejas grafa skautnu summas atnemam MST skautnu summu un pieskaita
        // easyEdges skautnu summu

        // Izvada rezultatus izejas faila
    }

    private int find(int i, int[] parents) {
        while (parents[i] != i) {
            i = parents[i];
        }
        return i;
    }

    // Does union of i and j. It returns
    // false if i and j are already in same
    // set.
    private void union1(int i, int j, int[] parents) {
        int a = find(i, parents);
        int b = find(j, parents);
        parents[a] = b;
    }

    private void kruskalsMst() {
        int[] parents = new int[verticleCount];
        int sum = 0;

        // Initialize sets of disjoint sets.
        for (int i = 0; i < verticleCount; i++) {
            parents[i] = i;
        }

        // Include minimum weight edges one by one
        int edge_count = 0;
        while (edge_count < verticleCount - 1) {
            int max = -101, a = -1, b = -1;
            for (int i = 0; i < verticleCount; i++) {
                for (int j = 0; j < verticleCount; j++) {
                    if (find(i, parents) != find(j, parents) && adjacencyMatrix[i][j] > max) {
                        max = adjacencyMatrix[i][j];
                        a = i;
                        b = j;
                    }
                }
            }

            union1(a, b, parents);
            edge_count++;
            // System.out.printf("Edge %d:(%d, %d) cost:%d \n", edge_count++, a, b, max);
            System.out.println("aaaaaaaa: " + max);
            if (max > 0) {
                sum += max;
            }
        }
        int easyEdgesSum = 0;
        for (int i = 0; i < easyEdges.size(); i++) {
            easyEdgesSum += easyEdges.get(i).complexity;
        }
        System.out.printf("\n Maximum cost= %d \n", sum);
        System.out.println("easyEdgesSum: " + easyEdgesSum);

        System.out.println("Programmas rezultats: " + (totalSum - sum + easyEdgesSum));
    }

    private void getMstEdges() {
        Verticle[] graphVerticles = new Verticle[verticleCount]; // Masivs ar grafa virsotnem
        Boolean[] mstSet = new Boolean[verticleCount]; // Masivs, kas pasaka, vai virsotne ir MST
        int[] parent = new int[verticleCount]; // Virsotnes vecaks MST
        TreeSet<Verticle> candidates = new TreeSet<Verticle>(new VerticleComparator()); // Max-heap ar kandidatiem

        for (int i = 0; i < verticleCount; i++) {
            graphVerticles[i] = new Verticle(i, -101);
            mstSet[i] = false;
            parent[i] = -1;
        }

        // int temp = 0;
        // int tempx = 0;
        // for (int i = 0; i < verticleCount; i++) {
        // for (int j = 0; j < adjacencyList.get(i).size(); j++) {
        // if (adjacencyList.get(i).get(j).complexity > tempx) {
        // tempx = adjacencyList.get(i).get(j).complexity;
        // temp = i;
        // }
        // }
        // }

        // Pirmo virsotni ieliek ka mst un uzliek 0, lai ta butu kaudzes augsa pirmaja
        // iteracija
        mstSet[0] = true;
        graphVerticles[0].complexity = 0;
        parent[0] = 0;

        for (int i = 0; i < verticleCount; i++) {
            candidates.add(graphVerticles[i]);
        }

        while (!candidates.isEmpty()) {
            Verticle candidate = candidates.pollFirst();
            mstSet[candidate.verticle] = true;

            LinkedList<Verticle> candidateAdjacencyList = adjacencyList.get(candidate.verticle);

            for (int i = 0; i < candidateAdjacencyList.size(); i++) {
                Verticle destination = candidateAdjacencyList.get(i);

                if (mstSet[destination.verticle] == false) {
                    if (graphVerticles[destination.verticle].complexity < destination.complexity) {
                        candidates.remove(graphVerticles[destination.verticle]);
                        graphVerticles[destination.verticle].complexity = destination.complexity;
                        candidates.add(graphVerticles[destination.verticle]);
                        parent[destination.verticle] = candidate.verticle;
                    }
                }
            }
        }

        int sum = 0;
        int easyEdgesSum = 0;
        for (int i = 0; i < verticleCount; i++) {
            sum += graphVerticles[i].complexity;
        }

        for (int i = 0; i < easyEdges.size(); i++) {
            easyEdgesSum += easyEdges.get(i).complexity;
        }
        System.out.println("summa: " + sum);
        System.out.println("easyEdgesSum: " + easyEdgesSum);

        System.out.println("Programmas rezultats: " + (totalSum - sum + easyEdgesSum));
    }

    /// Printesanas metodes
    public void printInfo() {
        System.out.println("-------------------------------");
        System.out.println("Virsotnu skaits:");
        System.out.println(verticleCount);
        System.out.println("-------------------------------");
        // System.out.println("Izveidota grafa reprezentacija:");
        // printGraphRepresentation();
        System.out.println("-------------------------------");
        System.out.println("Izveidota grafa reprezentacija matrica:");
        printGraphRepresentationMatrix();
    }

    public void printResult() {
        int complexitySum = 0;

        System.out.println("-------------------------------");
        System.out.println("Rezultats ir: " + complexitySum);
        System.out.println("-------------------------------");
    }

    private void printGraphRepresentation() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            String neighbours = (i + 1) + " -> ";

            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                Verticle verticle = adjacencyList.get(i).get(j);

                if (j != 0) {
                    neighbours += "; ";
                }

                neighbours += "[" + (verticle.verticle + 1) + "," + verticle.complexity + "]";
            }

            System.out.println(neighbours);
        }
    }

    private void printGraphRepresentationMatrix() {
        for (int i = 0; i < verticleCount; i++) {
            String matrixRow = (i + 1) + " => ";

            for (int j = 0; j < verticleCount; j++) {
                if (adjacencyMatrix[i][j] > -101) {
                    matrixRow += "[" + (j + 1) + "," + adjacencyMatrix[i][j] + "]";
                    matrixRow += "; ";
                }

            }

            System.out.println(matrixRow);
        }
    }
}
