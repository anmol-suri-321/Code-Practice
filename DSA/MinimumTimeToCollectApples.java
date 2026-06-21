package DSA;

import java.util.ArrayList;
import java.util.List;

public class MinimumTimeToCollectApples {
    public int minTime(int n, int[][] edges, List<Boolean> hasApple) {
        // Create Adj List
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // Add empty list for
            g.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            // Since its a undirected graph, u -> v and v -> u should be added
            g.get(edge[0]).add(edge[1]);
            g.get(edge[1]).add(edge[0]);
        }

        // dfs with vertex 0 and its parent as -1
        return dfs(0, -1, g, hasApple);
    }

    private int dfs(int node, int parent, List<List<Integer>> g, List<Boolean> hasApple) {
        int totalTime = 0;

        // Traverse all child nodes of the current node
        for (int child : g.get(node)) {
            if (child == parent)
                continue; // Avoid revisiting the parent node

            int childTime = dfs(child, node, g, hasApple); // Recursive DFS for the child

            // If child subtree has apples (childTime > 0) or the child itself has an apple
            if (childTime > 0 || hasApple.get(child)) {
                // Add 2 to total time (1 for going and 1 for coming back)
                totalTime += childTime + 2;
            }
        }

        return totalTime;
    }
}
