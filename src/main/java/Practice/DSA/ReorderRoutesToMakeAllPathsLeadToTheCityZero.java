package Practice.DSA;

import java.util.ArrayList;
import java.util.List;

public class ReorderRoutesToMakeAllPathsLeadToTheCityZero {
    int ans = 0;
    public int minReorder(int n, int[][] connections) {
        // Create an undirected graph, for DFS traversal from root
        List<List<Integer>> ug = new ArrayList<>();
        // Create a directed graph to store actual edges
        List<List<Integer>> dg = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            ug.add(new ArrayList<>());
            dg.add(new ArrayList<>());
        }

        for (int[] arr : connections) {
            dg.get(arr[0]).add(arr[1]);
            ug.get(arr[0]).add(arr[1]);
            ug.get(arr[1]).add(arr[0]);
        }

        boolean[] vis = new boolean[n];

        dfs(0, ug, dg, vis);

        return ans;
    }

    private void dfs(int i, List<List<Integer>> ug, List<List<Integer>> dg, boolean[] vis) {
        vis[i] = true;

        for (int j : ug.get(i)) {
            if (!vis[j]) {
                // calculate the forward edges, which needs to be reversed
                if (dg.get(i).contains(j)) {
                    ans++;
                }
                dfs(j, ug, dg, vis);
            }
        }
    }
}
