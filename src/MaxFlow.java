import java.lang.*;
import java.util.ArrayList;
import java.util.LinkedList;


// This code was written with help from this source https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
public class MaxFlow {
    //global variables
    int V;
    double delta;
    int path_flow = 100000;

    void setVertices(int V) {
        this.V = V;
    }
    //search to augment paths found
    boolean augment(int[][] rGraph, int s, int t, int parent[]) {
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; ++i)
            visited[i] = false;

        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (queue.size() != 0) {
            int u = queue.poll();
            for (int v = 0; v < V; v++) {
                if (visited[v] == false && rGraph[u][v] >= delta) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return (visited[t] == true);
    }

    // method for printing stuff
    void printOut(int[] parent) {
        System.out.print("Capacity Scaling" + delta + " ");
        int[] path = new int[6];
        int first = 5;
        int j = 0;
        while (first != -1) {
            path[j] = parent[first];
            first = parent[first];
            j++;
        }
        for (int i = 5; i > -1; i--) {
            if (path[i] != 0) {
                if (path[i] == -1)
                    System.out.print("s->");
                if (path[i] == 1)
                    System.out.print("a->");
                if (path[i] == 2)
                    System.out.print("b->");
                if (path[i] == 3)
                    System.out.print("c->");
                if (path[i] == 4)
                    System.out.print("d->");
                if (path[i] == 5)
                    System.out.print("t->");
            }

        }
        System.out.println("t  bottle neck is: " + path_flow);


    }
    // the main capacity scaling algorithm
    int Capacity_Scaling(int graph[][], int s, int t) {
        int max_flow = 0;
        int[] flow = new int[V];
        for (int a = 0; a < V; a++) {
            flow[a] = 0;
        }
        int maxInt = 0;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (graph[i][j] > maxInt)
                    maxInt = graph[i][j];
            }
        }
        int power = 1;
        while (true) {
            double val = Math.pow(2, power);
            if (val < maxInt)
                power++;
            else
                break;
        }
        delta = Math.pow(2, (power - 1));
        int u, v;
        int rGraph[][] = new int[V][V];

        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];

        int parent[] = new int[V];
        while (delta >= 1) {
            while (augment(rGraph, s, t, parent)) {

                for (v = t; v != s; v = parent[v]) {
                    u = parent[v];
                    path_flow = Math.min(path_flow, rGraph[u][v]);
                }
                for (v = t; v != s; v = parent[v]) {
                    u = parent[v];
                    rGraph[u][v] -= path_flow;
                    rGraph[v][u] += path_flow;
                }
                printOut(parent);
                // Add path flow to overall flow
                max_flow += path_flow;
            }
            System.out.println("Capacity Scaling" + delta + " : no augmenting path found");
            delta = delta / 2;
        }

        // Return the overall flow
        boolean[] isVisited = new boolean[V];
        for (int y = 0; y < V; y++) {
            isVisited[y] = false;
        }
        ArrayList<Integer> left = new ArrayList<>();
        left.add(0);
        ArrayList<Integer> right = new ArrayList<>();
        right.add(5);
        dfs(rGraph, s, isVisited);
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (graph[i][j] > 0 && isVisited[i] && !isVisited[j] && (!(left.contains(i)) && !(right.contains(j)))) {
                    left.add(i);
                    right.add(j);
                }
            }
        }
        System.out.print("The minimum cut is { ( ");
        for (int w = 0; w < left.size(); w++) {

            if (left.get(w) == 0)
                System.out.print("s ");
            if (left.get(w) == 1)
                System.out.print("a ");
            if (left.get(w) == 2)
                System.out.print("b ");
            if (left.get(w) == 3)
                System.out.print("c ");
            if (left.get(w) == 4)
                System.out.print("d ");
            if (left.get(w) == 5)
                System.out.print("t ");


        }
        System.out.print(" ),");
        System.out.print(" ( ");
        for (int p = 0; p < right.size(); p++) {

            if (right.get(p) == 0)
                System.out.print("s ");
            if (right.get(p) == 1)
                System.out.print("a ");
            if (right.get(p) == 2)
                System.out.print("b ");
            if (right.get(p) == 3)
                System.out.print("c ");
            if (right.get(p) == 4)
                System.out.print("d ");
            if (right.get(p) == 5)
                System.out.print("t ");


        }
        System.out.println(")}");
        return max_flow;
    }

    private static void dfs(int[][] rGraph, int s, boolean[] visited) {
        visited[s] = true;
        for (int i = 0; i < rGraph.length; i++) {
            if (rGraph[s][i] > 0 && !visited[i]) {
                dfs(rGraph, i, visited);
            }
        }
    }


    public static void main(String[] args) {
        // indexes of the graph. s=0, a=1,b=2,c=3,d=4,t=5
        int graph[][] = new int[][]{
                {0, 15, 10, 0, 0, 0},
                {0, 0, 3, 3, 9, 0},
                {0, 0, 0, 0, 7, 0},
                {0, 0, 0, 0, 0, 10},
                {0, 0, 0, 3, 0, 15},
                {0, 0, 0, 0, 0, 0}
        };
        MaxFlow m = new MaxFlow();
        m.setVertices(6);

        System.out.println("The maximum possible flow is " +
                m.Capacity_Scaling(graph, 0, 5));

    }
}



