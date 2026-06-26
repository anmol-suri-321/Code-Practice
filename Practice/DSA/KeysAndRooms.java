package Practice.DSA;

import java.util.List;

public class KeysAndRooms {
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] vis = new boolean[n];

        dfs(0, rooms, vis);

        for(int i = 0; i < n; i++) {
            if(!vis[i]) {
                return false;
            }
        }
        return true;
    }

    private void dfs(int i, List<List<Integer>> rooms, boolean[] vis) {
        vis[i] = true;

        for(int j : rooms.get(i)) {
            if(!vis[j]) {
                dfs(j, rooms, vis);
            }
        }
    }
}
