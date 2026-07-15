package Practice.DSA;

import java.util.ArrayList;
import java.util.List;

public class NumberOfProvinces {
    public int findCircleNum(int[][] edges) {
        int n = edges.length;
        List<List<Integer>> g = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(edges[i][j] == 1) {
                    g.get(i).add(j);
                    g.get(j).add(i);
                }
            }
        }

        boolean[] vis = new boolean[n];
        int ans = 0;
        for(int i = 0; i < n; i++) {
            if(!vis[i]) {
                ans++;
                dfs(i, n, g, vis);
            }
        }
        return ans;
    }

    private void dfs(int i, int n, List<List<Integer>> g, boolean[] vis) {
        vis[i] = true;
        for(int j : g.get(i)) {
            if(!vis[j]) {
                dfs(j, n, g, vis);
            }
        }
        return;
    }
}
