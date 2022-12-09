/** This class reads in a graph and allows you to find if it is connected,the MST,Shortest Path, if it is metric, make
 the graph metric, TSP brute force and the Approximate TSP.
 *
 * Author:	Johnthan Kneice,Tony Nguyen
 * Course:	COMP 2100
 * Assignment:	Project 4
 * Date:	12/03/2021
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Graphs {
    //The printPred method
    private static void printPred(int[] preds, int node, int startNode){
        //Would first check of the node is the same as the start node.
        if(node==startNode){
            System.out.print(node);
        }else{
            //Would do recursion, but would print the arrow in front of the node.
            printPred(preds, preds[node], startNode);
            System.out.print(" -> "+node);
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean success = false;
        // //success=file has been found
        while (!success){
            System.out.print("enter graph file: ");
            try {
                String file = input.nextLine();
                input = new Scanner(new File(file));
                success=true;
            } catch (FileNotFoundException e) {
                // If file does not exist can enter a new file.
                System.out.println("Stop file cannot be opened (perhaps it doesn't exist).");
            }
        }
        Graph graph = new Graph(input);
        //System.out.println(graph.toString());
        Scanner in = new Scanner(System.in);
        int choice=0;
        while (choice != 8) {
            System.out.println();
            System.out.print("1. Is Connected\n2. Minimum Spanning Tree\n3. Shortest Path\n4. Is Metric\n5. " +
                    "Make Metric\n6. " + "TSP\n7. Approximate TSP\n8. Quit\n\nMake your choice (1 - 8): ");
            choice = in.nextInt();
            // Is connected
            if (choice == 1) {
                if (graph.isConnected()) {
                    System.out.println("Graph is connected.");
                } else {
                    System.out.println("Graph is not connected.");

                }
                // Minimum Spanning Tree
            } else if (choice == 2) {
                if (!graph.isConnected()) {
                    System.out.println("Error: Graph is not connected.");
                } else {
                    System.out.println(graph.mst());
                }
                // Shortest Path
            } else if (choice == 3) {
                //Would ask the user to choose the starting node from 0 to the current graph size -1.
                System.out.print("From which node would you like to find the shortest paths (0 - " +
                        (graph.getSize() - 1) + ") ");
                int startNode = in.nextInt();
                //Would create the array of pred and dist with the size of the graph.
                int[] pred = new int[graph.getSize()];
                int[] dist = new int[graph.getSize()];
                //would do recursion.
                graph.shortestPaths(pred, dist, startNode);
                for (int i = 0; i < dist.length; i++) {
                    //Would first check for any unreachable nodes, if yes it will print (Infinity).
                    if (dist[i] == Integer.MAX_VALUE) {
                        System.out.println(i + ": (Infinity)");
                    } else {
                        //This would print out the shortest paths of the node, distance, and the precedence.
                        System.out.print(i + ": (" + dist[i] + ")\t");
                        printPred(pred, i, startNode);
                        System.out.println();
                    }
                }
                //Is Metric
            } else if (choice == 4) {

                if (!graph.completelyConnected()) {
                    System.out.println("Graph is not metric: Graph is not completely connected.");
                } else if (graph.isMetric()) {
                    System.out.println("Graph is metric.");
                } else {
                    System.out.println("metric: Edges do not obey the triangle inequality.");
                }
                //Make Metric
            } else if (choice == 5){
                if (!graph.isConnected()) {
                    System.out.println("Error: Graph is not connected.");
                } else {
                    graph = graph.makeMetric();
                    System.out.println(graph.toString());
                }


                // Traveling Salesman Problem
            } else if (choice == 6) {
                if (!graph.isConnected()) {
                    System.out.println("Graph is not connected.");
                }
                int[] bestPath = graph.tsp();
                //Organize given array into a string then print it out.
                String tsp = bestPath[bestPath.length - 1] + ": " + bestPath[0];
                //if graph is all 0s then there is no tour.
                int noTour = 0;
                for (int i = 1; i < bestPath.length - 1; i++) {
                    //Count number of 0s
                    if (bestPath[i] == 0) {
                        noTour++;
                    }
                    tsp += "-> " + bestPath[i];
                }
                if (noTour + 1 == bestPath.length - 1) {
                    System.out.println("Error: Graph has no tour.");
                } else {
                    tsp += "-> " + bestPath[0];
                    System.out.println(tsp);
                }

                //Approximate TSP
            } else if (choice == 7) {
                //Checks if it is metric.
                if (!graph.isMetric()) {
                    System.out.println("Error: Graph is not metric.");
                }
                else {
                    int[] bestPath = graph.approTsp();
                    //Organize the given array into a string then print it out.
                    String tsp = bestPath[bestPath.length - 1] + ": 0";
                    for (int i = 1; i < bestPath.length - 1; i++) {

                        tsp += "-> " + bestPath[i];
                    }
                    System.out.println(tsp);
                }
            }

        }
    }

}
