package DSA;

import java.util.LinkedList;
import java.util.Queue;

public class RottenOranges {
    class Pair {
        int a;
        int b;

        Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    public int orangesRotting(int[][] g) {
        int m = g.length;
        int n = g[0].length;
        Queue<Pair> q = new LinkedList<>();

        // Add all the {i,j} rotten oranges to the Queue
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (g[i][j] == 2) {
                    q.offer(new Pair(i, j));
                }
            }
        }

        int time = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            boolean isFreshOrange = false;
            for (int i = 0; i < size; i++) {
                Pair p = q.poll();
                int x = p.a;
                int y = p.b;
                
                if (isSafe(x, y - 1, g, m, n)) {
                    g[x][y - 1] = 2;
                    q.offer(new Pair(x, y - 1));
                    isFreshOrange = true;
                }
                if (isSafe(x, y + 1, g, m, n)) {
                    g[x][y + 1] = 2;
                    q.offer(new Pair(x, y + 1));
                    isFreshOrange = true;
                }
                if (isSafe(x - 1, y, g, m, n)) {
                    g[x - 1][y] = 2;
                    q.offer(new Pair(x - 1, y));
                    isFreshOrange = true;
                }
                if (isSafe(x + 1, y, g, m, n)) {
                    g[x + 1][y] = 2;
                    q.offer(new Pair(x + 1, y));
                    isFreshOrange = true;
                }
            }
            // if in the current iteration of the queue, atleast one orange should be fresh to be rotten
            if (isFreshOrange) {
                time++;
            }
        }

        // check if the array contains any fresh orange
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (g[i][j] == 1) {
                    return -1;
                }
            }
        }

        return time;
    }

    // This method checks if the current index {x,y} is a fresh orange,
    boolean isSafe(int x, int y, int[][] g, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n && g[x][y] == 1;
    }
}
