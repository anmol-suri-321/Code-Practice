package Practice.DSA.CourseSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CourseScheduleII {
    public int[] findOrder(int n, int[][] pre) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }

        // Build the graph with the correct direction (prerequisite to course)
        for (int[] edge : pre) {
            g.get(edge[1]).add(edge[0]);
        }
        boolean[] vis = new boolean[n];
        boolean[] curStack = new boolean[n];
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < n; i++) {
            if (!vis[i])
                if (dfs(i, g, vis, curStack, st))
                    return new int[] {};
        }
        int[] ans = new int[n];
        int index = 0;
        while (!st.isEmpty()) {
            ans[index++] = st.pop();
        }
        return ans;
    }

    private boolean dfs(int i, List<List<Integer>> g, boolean[] vis,
            boolean[] curStack, Stack<Integer> st) {
        vis[i] = true;
        curStack[i] = true;
        for (int j : g.get(i)) {
            if (!vis[j]) {
                if (dfs(j, g, vis, curStack, st))
                    return true;    
            } else if (curStack[j]) {
                return true;
            }
        }
        curStack[i] = false;
        st.push(i);
        return false;
    }
}
