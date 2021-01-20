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
    private ArrayList<Edge> easyEdges; // Skatunes, kuru sarezgitiba ir negativa vai 0
    private ArrayList<Edge> mstEdges; // Maximum spaning tree skautnes

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

            int complexitySum = 0; // TODO: Iznemt

            while (fileReader.hasNextInt()) {
                int verticleA = fileReader.nextInt();
                int verticleB = fileReader.nextInt();
                int complexity = fileReader.nextInt();

                if (complexity > 0) {
                    adjacencyList.get(verticleA - 1).add(new Verticle(verticleB, complexity));
                    adjacencyList.get(verticleB - 1).add(new Verticle(verticleA, complexity));

                    complexitySum += complexity; // TODO: Iznemt
                } else {
                    easyEdges.add(new Edge(verticleA, verticleB, complexity));
                }
            }

            System.out.println("vispar kopa summa: " + complexitySum); // TODO: iznemt

            fileReader.close();
        } catch (FileNotFoundException fileError) {
            System.out.println("Sagaidītais ieejas fails \"" + inputFileName + "\" nav atrasts!");
        }
    }

    public void getPoohsSolution() {
        // Ar Prima algoritmu iegustam Maximum spaning tree
        System.out.println("$$$$$$$$$$$$$$$$$");
        System.out.println("Prima algoritms :");
        getMstEdges();

        // No kopejas grafa skautnu summas atnemam MST skautnu summu un pieskaita
        // easyEdges skautnu summu

        // Izvada rezultatus izejas faila
    }

    private void getMstEdges() {
        TreeSet<Verticle> candidates = new TreeSet<Verticle>(new VerticleComparator());

        for (int i = 0; i < adjacencyList.get(0).size(); i++) {
            candidates.add(adjacencyList.get(0).get(i));
        }

        System.out.println("priority queue test: ");
        Verticle biggestVerticle = candidates.pollFirst();
        System.out.println(1 + " -> " + biggestVerticle.verticle + "(" + biggestVerticle.complexity + ")");
        biggestVerticle = candidates.pollFirst();
        System.out.println(1 + " -> " + biggestVerticle.verticle + "(" + biggestVerticle.complexity + ")");
        biggestVerticle = candidates.pollFirst();
        System.out.println(1 + " -> " + biggestVerticle.verticle + "(" + biggestVerticle.complexity + ")");
    }

    /// Printesanas metodes
    public void printInfo() {
        System.out.println("-------------------------------");
        System.out.println("Virsotnu skaits:");
        System.out.println(verticleCount);
        System.out.println("-------------------------------");
        System.out.println("Izveidota grafa reprezentacija:");
        printGraphRepresentation();
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

                neighbours += "[" + verticle.verticle + "," + verticle.complexity + "]";
            }

            System.out.println(neighbours);
        }
    }
}
