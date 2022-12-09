/**
 * This class holds representation of a graph and methods that allow you to find and manipulate certain properties of a
 * given graph. Methods in this class consist of making a graph, making an empty graph of a given size, finding if a
 * graph is connected, DFS, Shortest path, find if every node is connected to all other nodes, find if a graph is metric
 * , make a graph metric, brute force tst,Approximate tsp, MST, convert a graph into a string in a graph format, edit
 * the array of a graph.
 * Approximate TSP, converting
 * <p>
 * Author:	Johnthan Kneice,Tony Nguyen
 * Course:	COMP 2100
 * Assignment:	Project 4
 * Date:	12/03/2021
 */

import java.util.Scanner;

//Takes a scanner and turns it into a graph or array called edges.
public class Graph {
    private int[][] edges;

    public Graph(Scanner scanner) {

        int size = scanner.nextInt();
        edges = new int[size][size];
        for (int i = 0; i < size; i++) {
            int edges = scanner.nextInt();
            for (int j = 0; j < edges; j++) {
                int node = scanner.nextInt();
                this.edges[i][node] = scanner.nextInt();
            }
        }
    }

    /*
    depth-first search
     */
    private void dfs(int node, int[] number) {
        number[edges.length]++;
        number[node] = number[edges.length];
        for (int i = 0; i < edges.length; i++) {
            if (edges[node][i] != 0 && number[i] == 0) {
                dfs(i, number);
            }
        }
    }

    /**
     * @param node
     * @return
     */
    public int[] dfs(int node) {
        int[] number = new int[edges.length + 1];
        dfs(node, number);
        return number;
    }

    /** This method would check if the graph is connected
     * @return
     */
    public boolean isConnected() {
        int[] number = dfs(0);
        return number[edges.length] == edges.length;
    }

    /** This method used the dijkstra algorithm.
     * @return
     */
    public void shortestPaths(int[] predecessors, int[] distances, int startNode) {
        /*
        this would create an empty boolean array of S in which holds the node in which we know the shortest path from
        the start node.
         */
        boolean[] s = new boolean[edges.length];
        //This would set all the distances to the Infinity.
        for (int i = 0; i < edges.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        //sets the distance of the starting node to 0
        distances[startNode] = 0;
        for (int i = 0; i < edges.length; i++) {
            int smallestDistance = Integer.MAX_VALUE;
            int u = -1;
            for (int j = 0; j < distances.length; j++) {
                if (!s[j] && distances[j] < smallestDistance) {
                    smallestDistance = distances[j];
                    u = j;
                }
            }
            //If it can't find anything that is smaller than the smallest Distance which is Infinity.
            if (smallestDistance == Integer.MAX_VALUE) {
                return;
            }
            for (int j = 0; j < edges.length; j++) {
                if (edges[u][j] != 0 && distances[j] > distances[u] + edges[u][j]) {
                    distances[j] = distances[u] + edges[u][j];
                    predecessors[j] = u;
                }
            }
            s[u] = true;
        }
    }

    /**
     * Checks if all nodes are connected to all other nodes.
     * Returns true or false;
     * @return
     */
    public boolean completelyConnected() {
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                if (edges[i][j] == 0 && i != j) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks to see if graph is completely connected and obeys the triangle inequality.
     * @return
     */
    public boolean isMetric() {
        for (int i = 0; i < edges.length; i++) {
            for (int j = i + 1; j < edges.length; j++) {
                for (int k = 0; k < edges.length; k++) {
                    // Makes sure to not check one node with itself.
                    if (i != k && j != k) {
                        int a = edges[i][j];
                        int b = edges[j][k];
                        int c = edges[k][i];
                        //Checking if it obeys the triangle inequality.
                        if (a > b + c) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * make metric method
     * @return
     */
    public Graph makeMetric() {
        Graph metric = new Graph(edges.length);
        for (int i = 0; i < edges.length; i++) {
            int[] pred = new int[edges.length];
            int[] distance = new int[edges.length];
            //Uses the shortest path to find the distance to all nodes to make that distance be the edge weight.
            shortestPaths(pred, distance, i);
            //Adds in edges into the metric graph's edge array.
            for (int j = 0; j < edges.length; j++) {
                //metric.toEdge()[i][j] = distance[j];
                metric.edges[i][j] = distance[j];

            }
        }
        return metric;

    }

    /**
     * Brute force approach to the Traveling salesman problem
     * Finds the order of nodes to visit with the lowest edge weight.Each node can only be visited once.
     * @return
     */
    public void tsp(int[] path, int[] bestPath, int node, boolean[] hasBeenTo, int step) {
        //End case. If the last node visited is the node where you started at the total edge weight is less than
        // bestPath make path= bestPath. The last spot in the array is the edge weight.
        if (step == edges.length) {
            if (node == path[0] && bestPath[edges.length] > path[edges.length]) {
                for (int i = 0; i < path.length; i++) {
                    bestPath[i] = path[i];
                }
            }
            //If node has not already been visited. Add node to hasBeenTo.
        } else if (!hasBeenTo[node]) {
            hasBeenTo[node] = true;
            path[step] = node;
            for (int i = 0; i < edges.length; i++) {
                //If there is an edge.
                if (edges[node][i] != 0) {
                    path[edges.length] += edges[node][i];
                    //Recursively call TSP.
                    tsp(path, bestPath, i, hasBeenTo, step + 1);
                    path[edges.length] -= edges[node][i];
                }
            }
            hasBeenTo[node] = false;
        }
    }

    /**
     *Starts the recursion and returns the bestPath. Also sets up variables.
     * @return
     */
    public int[] tsp() {
        int[] path = new int[edges.length + 1];
        int[] bestPath = new int[edges.length + 1];
        bestPath[edges.length] = Integer.MAX_VALUE;
        //Next opened spot in array.
        //Already visited nodes.
        boolean[] hasBeenTo = new boolean[edges.length];
        //How many nodes have been visited.
        tsp(path, bestPath, 0, hasBeenTo, 0);
        return bestPath;
    }

    /**
     * Approximate approach to the Traveling salesman problem. only works if graph is metric.
     * @return
     */
    public int[] approTsp() {
        //Find the Minimum Spanning Tree of graph.
        Graph mst = mst();
        //Do a depth first search.
        int[] array = mst.dfs(0);
        //Arrange tspArray of nodes in the graph in order of the depth first search array.
        int[] tspArray = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            int position = array[i] - 1;
            tspArray[position] = array[i] - 1;
        }
        //Find the total edge weight of the tspArray and place it in the last spot of the array.
        int total = edges[0][tspArray[0]];
        for (int i = 0; i < edges.length; i++) {
            total += edges[tspArray[i]][tspArray[i + 1]];
        }
        tspArray[array.length] = total;
        return tspArray;
    }

    /**
     * Turns a graph into a string of the graph format.
     * @return
     */
    public String toString() {
        String output = "";
        output += edges.length + "\n";
        for (int i = 0; i < edges.length; i++) {
            int edges = 0;
            for (int j = 0; j < this.edges.length; j++) {
                if (this.edges[i][j] != 0) {
                    edges++;
                }
            }
            output += edges;
            for (int j = 0; j < this.edges.length; j++) {
                if (this.edges[i][j] != 0) {
                    output += " " + j + " " + this.edges[i][j];
                }
            }
            output += "\n";
        }
        return output;

    }

    /**
     * Makes a graph of a given size.
     * @param size
     */

    private Graph(int size) {
        edges = new int[size][size];
    }

    /**
     * returns the size of the graph's array
     * @return
     */
    public int getSize() {
        return edges.length;
    }

    /**
     * min spanning tree
     *
     * @return
     */
    public Graph mst() {
        //A new graph with the same size as graph but this graph is empty
        Graph s = new Graph(edges.length);
        //Array of already visited nodes.
        boolean[][] marked = new boolean[edges.length][edges.length];
        for (int i = 0; i < edges.length; i++) {
            int min = edges.length;
            for (int j = 0; j < edges.length; j++) {
                //Finds the smallest edge to a not already visited node.
                if (edges[i][j] < min && edges[i][j] != 0 && !marked[i][j]) {
                    min = j;
                }
            }
            //Add the edge to the new empty graph.
            s.edges[i][min] = edges[i][min];
            //marks that node as visited.
            marked[i][min] = true;
        }
        //Adds the marked nodes to the graph.
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                if (marked[j][i]) {
                    s.edges[i][j] = edges[i][j];
                }
            }
        }

        return s;

    }

}

