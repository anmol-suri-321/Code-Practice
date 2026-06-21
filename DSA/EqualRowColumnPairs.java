package DSA;

import java.util.ArrayList;
import java.util.List;

public class EqualRowColumnPairs {
    public int equalPairs(int[][] grid) {
        int n = grid.length;
        List<List<Integer>> row = new ArrayList<>();
        List<List<Integer>> col = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            row.add(new ArrayList<>());
            col.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                row.get(i).add(grid[i][j]);
                col.get(j).add(grid[i][j]);
            }
        }
        int count = 0;
        // Compare each row with each column
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (row.get(i).equals(col.get(j))) {
                    count++;
                }
            }
        }
        return count;
    }
}
