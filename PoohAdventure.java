// Autors: Roberts Groza rg11080

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;

public class PoohAdventure {
    public static void main(String[] args) {
        HundredAcreWoodGraph graph = new HundredAcreWoodGraph("input.txt");
        graph.printInfo();
    }
}

class Edge {
    public int vertex;
    public int complexity;

    Edge(int vertex, int complexity) {
        this.vertex = vertex;
        this.complexity = complexity;
    }
}

class HundredAcreWoodGraph {
    private int verticleCount;
    private ArrayList<LinkedList<Edge>> adjacencyList;

    HundredAcreWoodGraph(String inputFileName) {
        buildAdjacencyList(inputFileName);
    }

    private void buildAdjacencyList(String inputFileName) {
        try {
            File inputFile = new File(inputFileName);
            Scanner fileReader = new Scanner(inputFile);

            verticleCount = fileReader.nextInt();
            adjacencyList = new ArrayList<LinkedList<Edge>>();

            for (int i = 0; i < verticleCount; i++) {
                adjacencyList.add(new LinkedList<Edge>());
            }

            while (fileReader.hasNextInt()) {
                int start = fileReader.nextInt();
                int end = fileReader.nextInt();
                int complexity = fileReader.nextInt();

                LinkedList<Edge> startEdges = adjacencyList.get(start - 1);
                LinkedList<Edge> endEdges = adjacencyList.get(end - 1);

                startEdges.add(new Edge(end, complexity));
                endEdges.add(new Edge(start, complexity));
            }

            fileReader.close();
        } catch (FileNotFoundException fileError) {
            System.out.println("Sagaidītais ieejas fails \"" + inputFileName + "\" nav atrasts!");
        }
    }

    public void printInfo() {
        System.out.println("### Grafs izveidots ###");
        System.out.println("Virsotnu skaits:");
        System.out.println(verticleCount);
        System.out.println("Izveidota grafa reprezentacija:");
        printGraphRepresentation();
    }

    private void printGraphRepresentation() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            String edgeList = (i + 1) + " -> ";

            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                Edge edge = adjacencyList.get(i).get(j);

                if (j == 0) {
                    edgeList += "[" + edge.vertex + "," + edge.complexity + "]";
                } else {
                    edgeList += "; " + "[" + edge.vertex + "," + edge.complexity + "]";
                }
            }

            System.out.println(edgeList);
        }
    }
}
