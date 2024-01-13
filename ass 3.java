import java.util.*;

public class Graph {
    private Map<Integer, List<Integer>> graph = new HashMap<>();

    public void addEdge(int u, int v) {
        graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
    }

    public void dfs(int node, boolean[] visited, List<Integer> path) {
        visited[node] = true;
        path.add(node);
        System.out.print(node + " ");

        List<Integer> neighbors = graph.getOrDefault(node, Collections.emptyList());

        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited, path);
            } else if (path.contains(neighbor)) {
                System.out.println("\nCycle: " + getCycle(path, neighbor));
            }
        }

        path.remove(path.size() - 1);
    }

    private String getCycle(List<Integer> path, int start) {
        StringBuilder cycle = new StringBuilder();
        boolean startAdding = false;

        for (int node : path) {
            if (node == start) {
                startAdding = true;
            }

            if (startAdding) {
                cycle.append(node).append(" ");
            }
        }

        cycle.append(start);
        return cycle.toString();
    }

    public void bfs(int startNode) {
        boolean[] visited = new boolean[graph.size() + 1];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(startNode);
        visited[startNode] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.print(node + " ");

            List<Integer> neighbors = graph.getOrDefault(node, Collections.emptyList());

            for (int neighbor : neighbors) {
                if (!visited[neighbor]) {
                    queue.offer(neighbor);
                    visited[neighbor] = true;
                }
            }
        }
    }

    public boolean hasCycle() {
        boolean[] visited = new boolean[graph.size() + 1];
        boolean[] recStack = new boolean[graph.size() + 1];

        for (int node : graph.keySet()) {
            if (!visited[node] && hasCycleUtil(node, visited, recStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycleUtil(int node, boolean[] visited, boolean[] recStack) {
        visited[node] = true;
        recStack[node] = true;

        List<Integer> neighbors = graph.getOrDefault(node, Collections.emptyList());

        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                if (hasCycleUtil(neighbor, visited, recStack)) {
                    return true;
                }
            } else if (recStack[neighbor]) {
                return true;
            }
        }

        recStack[node] = false;
        return false;
    }

    public boolean isBipartite() {
        int[] color = new int[graph.size() + 1];
        Arrays.fill(color, -1);

        for (int node : graph.keySet()) {
            if (color[node] == -1 && !bipartiteUtil(node, color)) {
                return false;
            }
        }

        return true;
    }

    private boolean bipartiteUtil(int node, int[] color) {
        color[node] = 1;

        List<Integer> neighbors = graph.getOrDefault(node, Collections.emptyList());

        for (int neighbor : neighbors) {
            if (color[neighbor] == -1) {
                color[neighbor] = 1 - color[node];
                if (!bipartiteUtil(neighbor, color)) {
                    return false;
                }
            } else if (color[neighbor] == color[node]) {
                return false;
            }
        }

        return true;
    }

    public boolean isTree() {
        boolean[] visited = new boolean[graph.size() + 1];
        dfs(1, visited, new ArrayList<>());

        for (int node : graph.keySet()) {
            if (!visited[node]) {
                return false;
            }
        }

        int edgeCount = 0;
        for (List<Integer> neighbors : graph.values()) {
            edgeCount += neighbors.size();
        }

        return edgeCount / 2 == graph.size() - 1;
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        int[][] edges = {{1, 3}, {1, 4}, {2, 1}, {2, 3}, {3, 4}, {4, 1}, {4, 2}};

        for (int[] edge : edges) {
            g.addEdge(edge[0], edge[1]);
        }

        System.out.println("DFS: ");
        g.dfs(1, new boolean[g.graph.size() + 1], new ArrayList<>());

        System.out.println("\nBFS: ");
        g.bfs(1);

        System.out.println("\nCycle: ");
        if (g.hasCycle()) {
            System.out.println("Graph has cycles.");
        } else {
            System.out.println("Graph does not have cycles.");
        }

        System.out.println("Bipartite: ");
        if (g.isBipartite()) {
            System.out.println("Graph is bipartite.");
        } else {
            System.out.println("Graph is not bipartite.");
        }

        System.out.println("Tree: ");
        if (g.isTree()) {
            System.out.println("Graph is a tree.");
        } else {
            System.out.println("Graph is not a tree.");
        }
    }
}
