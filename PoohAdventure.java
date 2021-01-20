// Autors: Roberts Groza rg11080

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
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
        // graph.printVertexCyclicRoutes();
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
    private int verticleCount; // grafa virsotnu skaits
    private int[][] adjacencyMatrix; // Grafa reprezentacijas matrica
    private ArrayList<int[]> cyclicRouteArray; // Maršrutu masīvs
    private ArrayList<Edge> chosenEdges; // Izvēlēto ceļu saraksts

    HundredAcreWoodGraph(String inputFileName) {
        cyclicRouteArray = new ArrayList<int[]>();
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
            createAdjacencyMatrix();

            while (fileReader.hasNextInt()) {
                int verticleA = fileReader.nextInt();
                int verticleB = fileReader.nextInt();
                int complexity = fileReader.nextInt();

                if (complexity > 0) {
                    adjacencyMatrix[verticleA - 1][verticleB - 1] = complexity;
                    adjacencyMatrix[verticleB - 1][verticleA - 1] = complexity;
                } else {
                    chosenEdges.add(new Edge(verticleA, verticleB, complexity));
                }
            }

            fileReader.close();
        } catch (FileNotFoundException fileError) {
            System.out.println("Sagaidītais ieejas fails \"" + inputFileName + "\" nav atrasts!");
        }
    }

    private void createAdjacencyMatrix() {
        adjacencyMatrix = new int[verticleCount][verticleCount];

        for (int i = 0; i < verticleCount; i++) {
            for (int j = 0; j < verticleCount; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    /**
     * Atrod visus cikliskos marsrutus, kas iziet no dotas virsotnes 1-n
     */
    public void getResult() {
        // Iespejams routei jabut linkedList, tapec ka viena punkta var atgriezties
        // vairak neka 2 reizes
        int[] newRoute = new int[40]; // + 1, jo ja apiet visas virsotnes plus
        // atgriezas sakumpunkta ir
        // +1

        for (int i = 0; i < 40; i++) {
            newRoute[i] = -1;
        }

        System.out.println("shiiiit");

        newRoute[0] = 6;
        getAllRoutes(6, 6, 1, newRoute, false);

        printInfo();

        // String paths = "" + 0;
        // searchForShortestCycle(0, 0, paths);
    }

    // private void searchForShortestCycle(int startingVerticle, int
    // currentVerticle, String paths) {
    // if (startingVerticle == currentVerticle && paths.length() > 1) {
    // System.out.println("We found love: " + paths);
    // return;
    // } else {
    // for (int i = 0; i < verticleCount; i++) {
    // if(adjacencyMatrix[lastVerticle][i] > 0 )
    // }
    // }
    // }

    private void getAllRoutes(int startingVerticle, int lastVerticle, int routeLenght, int[] currentRoute,
            boolean isFound) {
        if (isFound) {
            return;
        }

        if (startingVerticle != lastVerticle || routeLenght < 4) {
            for (int i = 0; i < verticleCount; i++) {
                if (adjacencyMatrix[lastVerticle][i] > 0
                        && !routeContainsEdge(currentRoute, routeLenght, lastVerticle, i)) {
                    int[] newRoute = new int[40];   // Vajadzetu kaut ko labaku izdomat
                    System.arraycopy(currentRoute, 0, newRoute, 0, 40);
                    newRoute[routeLenght - 1] = i;

                    getAllRoutes(startingVerticle, i, routeLenght + 1, newRoute, false);
                }
            }
        } else {
            cyclicRouteArray.add(currentRoute);

            String aaa = "teeeeee: " + (startingVerticle + 1);
            // System.out.println(routeLenght);
            for (int i = 0; i < 40; i++) {
                if (currentRoute[i] > -1) {
                    aaa += "-" + (currentRoute[i] + 1);
                }
            }
            System.out.println(aaa);
        }

        // System.out
        // .println("starting: " + startingVerticle + ", last: " + lastVerticle + ",
        // routeLength: " + routeLenght);

        // if (startingVerticle == lastVerticle && routeLenght > 3) {
        // cyclicRouteArray.add(currentRoute);

        // // adjacencyMatrix[6][2] = 0;
        // // adjacencyMatrix[2][6] = 0;

        // // int easiestEdge = 101;
        // // int a = -1;
        // // int b = -1;

        // // for (int i = 1; i < routeLenght - 1; i++) {
        // // if (adjacencyMatrix[i-1][i] > 0 && adjacencyMatrix[i-1][i] < easiestEdge)
        // {
        // // easiestEdge = adjacencyMatrix[i-1][i];
        // // a = i;
        // // b = i - 1;
        // // }
        // // }

        // // System.out.println("Houston this could work?" + a + "-" + b);

        // // if (a > -1 && b > -1) {
        // // adjacencyMatrix[a][b] = 0;
        // // adjacencyMatrix[b][a] = 0;
        // // }

        // String aaa = "teeeeee: " + (startingVerticle + 1);
        // // System.out.println(routeLenght);
        // for (int i = 0; i < verticleCount; i++) {
        // if (currentRoute[i] > -1) {
        // aaa += "-" + (currentRoute[i] + 1);
        // }
        // }
        // System.out.println(aaa);
        // } else if (routeLenght > 8) {
        // // System.out.println("Paarsniegts");

        // // String aaa = "teeeeee: " + (startingVerticle + 1);
        // // for (int i = 0; i < verticleCount; i++) {
        // // if (currentRoute[i] > -1) {
        // // aaa += "-" + (currentRoute[i] + 1);
        // // }
        // // }
        // // System.out.println(aaa);
        // } else {
        // for (int i = 0; i < verticleCount; i++) {
        // if (adjacencyMatrix[lastVerticle][i] > 0
        // && !routeContainsEdge(currentRoute, routeLenght, lastVerticle, i)) {
        // // System.out.println(routeLenght + "-" + i);
        // int[] newRoute = new int[40];
        // System.arraycopy(currentRoute, 0, newRoute, 0, 40);
        // newRoute[routeLenght - 1] = i;

        // getAllRoutes(startingVerticle, i, routeLenght + 1, newRoute);

        // // printInfo();
        // }
        // }
        // }
    }

    /**
     * lai izvairitos no iespejama loop. Piemeram 7 -> 3 -> 1 -> 2 -> 1 -> 2 -> 1 ->
     * 2 utt...
     *
     * @param route
     * @param routeLength
     * @param verticleA
     * @param verticleB
     * @return
     */
    private boolean routeContainsEdge(int[] route, int routeLength, int verticleA, int verticleB) {
        for (int i = 1; i < routeLength; i++) {
            // System.out.println(verticleA + "--" + verticleB + "------------" + route[i-1]
            // + "-L-" + route[i] + "route legnth" + routeLength);
            if (route[i - 1] == verticleA && route[i] == verticleB
                    || route[i - 1] == verticleB && route[i] == verticleA) {
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

    // public void printVertexCyclicRoutes() {
    // System.out.println("Celu saraksts: ");

    // for (int i = 0; i < cyclicRouteArray.size(); i++) {
    // String route = "";

    // for (int j = 0; j < cyclicRouteArray.get(i).size(); j++) {
    // route += cyclicRouteArray.get(i).get(j).end + ", ";
    // }

    // System.out.println(route);
    // }
    // }

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
        for (int i = 0; i < verticleCount; i++) {
            String matrixRow = (i + 1) + " => ";

            for (int j = 0; j < verticleCount; j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    matrixRow += "[" + (j + 1) + "," + adjacencyMatrix[i][j] + "]";
                    matrixRow += "; ";
                }

            }

            System.out.println(matrixRow);
        }
    }
}
