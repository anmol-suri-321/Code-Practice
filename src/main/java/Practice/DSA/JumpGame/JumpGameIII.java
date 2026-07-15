package Practice.DSA.JumpGame;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class JumpGameIII {
    public boolean canReach(int[] arr, int start) {
        int n = arr.length;
        Queue<Integer> bfs = new LinkedList<>();
        // Array to mark the visited indexes
        int[] visited = new int[n];
        Arrays.fill(visited, 0);

        // Put the first element in the queue to start BFS
        bfs.add(start);

        while(!bfs.isEmpty()) {
            int top = bfs.remove();
            // Check if the top element of the queue is desired value '0', return true
            if(arr[top] == 0) return true;

            // mark the top index in visited array to 1
            visited[top] = 1;
            int left = top - arr[top];
            int right = top + arr[top];

            // if the left index is valid, put into the visited array
            if(isValidIndex(left, arr, n, visited)) {
                bfs.add(left);
            }
            // if the right index is valid, put into the visited array
            if(isValidIndex(right, arr, n, visited)) {
                bfs.add(right);
            }
        }
        return false;
    }

    // Check for the valid index in the array
    private boolean isValidIndex(int index, int[] arr, int n, int[] visited) {
        if(index >= 0 && index < n && visited[index] == 0) return true;
        return false;
    }
}
