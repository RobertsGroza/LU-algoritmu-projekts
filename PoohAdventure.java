// Author: Roberts Groza rg11080

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;

public class PoohAdventure {
    public static void main(String[] args) {
        HundredAcreWoodGraph graph = new HundredAcreWoodGraph("input.txt");
        graph.solve();
        graph.printResults();
    }
}

/**
 * Represents undirected Edge in graph with 2 verticles and complexity
 */
class Edge {
    int verticleA;
    int verticleB;
    int complexity;

    Edge() {
        this.verticleA = -1;
        this.verticleB = -1;
        this.complexity = 0;
    }

    Edge(int verticleA, int verticleB, int complexity) {
        this.verticleA = verticleA;
        this.verticleB = verticleB;
        this.complexity = complexity;
    }
}

/**
 * Subset used to represent Subsets in Kruskal's algorithm. Each subset has
 * unique root. The higher rank - the bigger subset (used in union algorithm)
 */
class Subset {
    int root;
    int rank;

    Subset(int root, int rank) {
        this.root = root;
        this.rank = rank;
    }
};

class HundredAcreWoodGraph {
    private LinkedList<Edge> edgeList; // Graphs edge list sorted by complexity DESC
    private int verticleCount; // Verticle count in graph

    HundredAcreWoodGraph(String inputFileName) {
        edgeList = new LinkedList<Edge>();

        fillEdgeList(inputFileName);
    }

    /**
     * Creates edgeList sorted by complexity DESC from inputFile. Edge with biggest
     * complexity will always be first in list.
     *
     * @param inputFileName
     */
    private void fillEdgeList(String inputFileName) {
        try {
            File inputFile = new File(inputFileName);
            Scanner fileReader = new Scanner(inputFile);

            verticleCount = fileReader.nextInt();

            if (fileReader.hasNextInt()) {
                edgeList.add(new Edge(fileReader.nextInt(), fileReader.nextInt(), fileReader.nextInt()));
            }

            while (fileReader.hasNextInt()) {
                int verticleA = fileReader.nextInt() - 1;
                int verticleB = fileReader.nextInt() - 1;
                int complexity = fileReader.nextInt();

                boolean added = false;

                for (int i = 0; i < edgeList.size(); i++) {
                    if (edgeList.get(i).complexity <= complexity) {
                        edgeList.add(i, new Edge(verticleA, verticleB, complexity));
                        added = true;
                        break;
                    }
                }

                if (!added) {
                    edgeList.addLast(new Edge(verticleA, verticleB, complexity));
                }
            }

            fileReader.close();
        } catch (FileNotFoundException fileError) {
            System.out.println("File \"" + inputFileName + "\" not found!");
        }
    }

    /**
     * Finds subset's route. Every subset has it's own route.
     *
     * @param subsets
     * @param verticle
     * @return
     */
    int findSet(Subset subsets[], int verticle) {
        if (subsets[verticle].root != verticle) {
            subsets[verticle].root = findSet(subsets, subsets[verticle].root);
        }

        return subsets[verticle].root;
    }

    /**
     * Union subset1 and subset2. Attaches smaller rank subset under root of higher
     * rank subset.
     *
     * @param subsets
     * @param subset1Root
     * @param subset2Root
     */
    void Union(Subset subsets[], int subset1Root, int subset2Root) {
        int root1 = findSet(subsets, subset1Root);
        int root2 = findSet(subsets, subset2Root);

        if (subsets[root1].rank < subsets[root2].rank) {
            subsets[root1].root = root2;
        } else if (subsets[root1].rank > subsets[root2].rank) {
            subsets[root2].root = root1;
        } else {
            subsets[root2].root = root1;
            subsets[root1].rank++;
        }
    }

    /**
     * 1. Gets Maximum Spanning Tree using Kruskal's algorithm.
     *
     * 2. Every Maximum Spanning Tree edge gets excluded from edgeList.
     *
     * 3. All MST edges with complexity < 0 gets included back in edgeList, because
     * negative complexity will make HundredAcreWoodGraph all edge complexity sum
     * even more smaller
     */
    public void solve() {
        Edge[] mstEdges = new Edge[verticleCount];
        Subset subsets[] = new Subset[verticleCount];

        for (int i = 0; i < verticleCount; i++) {
            subsets[i] = new Subset(i, 0);
        }

        int edgeCount = 0;
        int edgeListIterator = 0;

        while (edgeCount < verticleCount - 1) {
            Edge candidateEdge = edgeList.get(edgeListIterator);

            int subset1Root = findSet(subsets, candidateEdge.verticleA);
            int subset2Root = findSet(subsets, candidateEdge.verticleB);

            // Different subset roots means different subsets, so there will be no cycle
            if (subset1Root != subset2Root) {
                mstEdges[edgeCount++] = candidateEdge;
                Union(subsets, subset1Root, subset2Root);
                edgeList.remove(edgeListIterator);
            } else {
                edgeListIterator++;
            }
        }

        for (int i = 0; i < edgeCount; i++) {
            if (mstEdges[i].complexity < 0) {
                edgeList.add(mstEdges[i]);
            }
        }
    }

    /**
     * Prints results into output file output.txt
     */
    public void printResults() {
        int totalSum = 0;
        int edgeCount = edgeList.size();

        for (int i = 0; i < edgeCount; i++) {
            totalSum += edgeList.get(i).complexity;
        }

        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(edgeCount + "\n");
            myWriter.write(totalSum + "\n");

            for (int i = 0; i < edgeCount; i++) {
                myWriter.write(edgeList.get(i).verticleA + "\t" + edgeList.get(i).verticleB);

                if (i % 5 == 4) {
                    myWriter.write("\n");
                } else {
                    myWriter.write("\t\t");
                }
            }

            myWriter.close();
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Error while writing into file");
            e.printStackTrace();
        }
    }
}
