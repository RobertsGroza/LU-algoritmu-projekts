// Autors: Roberts Groza rg11080

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
    }
}

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

class Subset {
    int parent;
    int rank;

    Subset(int parent, int rank) {
        this.parent = parent;
        this.rank = rank;
    }
};

class HundredAcreWoodGraph {
    private LinkedList<Edge> edgeList;
    private Edge[] mstEdges;
    private int verticleCount;

    HundredAcreWoodGraph(String inputFileName) {
        edgeList = new LinkedList<Edge>();

        fillEdgeList(inputFileName);
    }

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

    int find(Subset subsets[], int i) {
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }

        return subsets[i].parent;
    }

    void Union(Subset subsets[], int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    public void solve() {
        mstEdges = new Edge[verticleCount];

        Subset subsets[] = new Subset[verticleCount];

        for (int i = 0; i < verticleCount; i++) {
            subsets[i] = new Subset(i, 0);
        }

        int edgeCount = 0;
        int i = 0;

        while (edgeCount < verticleCount - 1) {
            Edge candidateEdge = edgeList.get(i);

            int x = find(subsets, candidateEdge.verticleA);
            int y = find(subsets, candidateEdge.verticleB);

            if (x != y) {
                mstEdges[edgeCount++] = candidateEdge;
                Union(subsets, x, y);
                edgeList.remove(i);
            } else {
                i++;
            }
        }

        for (int j = 0; j < edgeCount; j++) {
            if (mstEdges[j].complexity < 0) {
                edgeList.add(mstEdges[j]);
            }
        }

        processResult();
    }

    private void processResult() {
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
            System.out.println("Success");
        } catch (IOException e) {
            System.out.println("Error while writing into file");
            e.printStackTrace();
        }
    }
}
