package Practice.DSA;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class mergeIntervals {
    public int[][] merge(int[][] intervals) {
        // Handle edge cases
        if (intervals == null || intervals.length == 0) {
            return new int[0][0];
        }

        // Sort intervals by the starting times
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);

        for (int i = 1; i < intervals.length; i++) {
            int[] nextInterval = intervals[i];
            // If the current interval overlaps with the next one, merge them
            if (currentInterval[1] >= nextInterval[0]) {
                currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
            } else {
                // If they don't overlap, add the next interval to the list
                currentInterval = nextInterval;
                merged.add(currentInterval);
            }
        }

        // Convert List<int[]> to int[][]
        return merged.toArray(new int[merged.size()][]);
    }
}
