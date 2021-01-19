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

        // Do stuff here
        graph.getResult();

        graph.printResult();
        graph.printVertexCyclicRoutes();
    }
}

class Edge {
    public int start;
    public int end;
    public int complexity;

    Edge(int start, int end, int complexity) {
        this.start = start;
        this.end = end;
        this.complexity = complexity;
    }

    public boolean isSameEdgeAs(Edge edge) {
        return this.start == edge.start && this.end == edge.end || this.end == edge.start && this.start == edge.end;
    }
}

class HundredAcreWoodGraph {
    private int verticleCount;
    private ArrayList<LinkedList<Edge>> adjacencyList;
    private ArrayList<LinkedList<Edge>> cyclicRouteArray; // Maršrutu masīvs
    private ArrayList<Edge> chosenEdges; // Izvēlēto ceļu saraksts

    HundredAcreWoodGraph(String inputFileName) {
        cyclicRouteArray = new ArrayList<LinkedList<Edge>>();
        chosenEdges = new ArrayList<Edge>();

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
            adjacencyList = new ArrayList<LinkedList<Edge>>();

            for (int i = 0; i < verticleCount; i++) {
                adjacencyList.add(new LinkedList<Edge>());
            }

            while (fileReader.hasNextInt()) {
                int start = fileReader.nextInt();
                int end = fileReader.nextInt();
                int complexity = fileReader.nextInt();

                if (complexity > 0) {
                    adjacencyList.get(start - 1).add(new Edge(start, end, complexity));
                    adjacencyList.get(end - 1).add(new Edge(end, start, complexity));
                } else {
                    chosenEdges.add(new Edge(start, end, complexity));
                }
            }

            fileReader.close();
        } catch (FileNotFoundException fileError) {
            System.out.println("Sagaidītais ieejas fails \"" + inputFileName + "\" nav atrasts!");
        }
    }

    /**
     * Atrod visus cikliskos marsrutus, kas iziet no dotas virsotnes 1-n
     */
    public void getResult() {
        // Dabut visus iespejamos cikliskos marsrutus
        // for (int i = 0; i < verticleCount; i++) {
        // getAllRoutes(i, i, new LinkedList<Edge>());
        // }

        getAllRoutes(6, 6, new LinkedList<Edge>(), 0);
    }

    /**
     *
     * @param startingVerticle Virsotne no kuras sakta apstaigasana
     * @param currentVerticle Virotne kura atrodas
     * @param currentRoute Pasreizejais marsruts
     * @param verticleIndex
     */
    private void getAllRoutes(int startingVerticle, int currentVerticle, LinkedList<Edge> currentRoute,
            int verticleIndex) {
        if (startingVerticle == currentVerticle && !currentRoute.isEmpty()) {
            cyclicRouteArray.add(currentRoute);
        }

        System.out.println("bbbbbbbbbbbbbbbbbbbbb");
    }

    private boolean linkedListContainsEdge(LinkedList<Edge> list, Edge edge) {
        for (int i = 0; i < list.size(); i++) {
            if (edge.isSameEdgeAs(list.get(i))) {
                return true;
            }
        }

        return false;
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

    public void printVertexCyclicRoutes() {
        System.out.println("Celu saraksts: ");

        for (int i = 0; i < cyclicRouteArray.size(); i++) {
            String route = "";

            for (int j = 0; j < cyclicRouteArray.get(i).size(); j++) {
                route += cyclicRouteArray.get(i).get(j).end + ", ";
            }

            System.out.println(route);
        }
    }

    public void printResult() {
        int complexitySum = 0;

        for (int i = 0; i < chosenEdges.size(); i++) {
            complexitySum += chosenEdges.get(i).complexity;
        }

        System.out.println("-------------------------------");
        System.out.println("Rezultats ir: " + complexitySum);
        System.out.println("-------------------------------");
    }

    private void printGraphRepresentation() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            String edgeList = (i + 1) + " -> ";

            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                Edge edge = adjacencyList.get(i).get(j);

                if (j == 0) {
                    edgeList += "[" + edge.end + "," + edge.complexity + "]";
                } else {
                    edgeList += "; " + "[" + edge.end + "," + edge.complexity + "]";
                }
            }

            System.out.println(edgeList);
        }
    }
}
