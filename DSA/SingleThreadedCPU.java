package DSA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class SingleThreadedCPU {
    public int[] getOrder(int[][] tasks) {
        int n = tasks.length;
        // Priority queue to store index, enqueTime, processing time
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            // if the processing time is same, then smaller index will come first
            if(a[2] == b[2]) return a[0] - b[0];
            // otherwise, sort based on the processing time
            return a[2] - b[2];
        });


        int[][] arr = new int[n][3];
        for(int i = 0; i < n; i++) {
            arr[i][0] = i; // index of the task
            arr[i][1] = tasks[i][0]; // enqueue time
            arr[i][2] = tasks[i][1]; // processing time
        }

        Arrays.sort(arr, (a, b) -> a[1] - b[1]);

        int entryTime = arr[0][1];
        int i = 0;
        List<Integer> ans = new ArrayList<>();
        while(i < n) {
            System.out.println("index i: " + i);
            while(i < n && arr[i][1] <= entryTime) {
                // add all the tasks, which have enqueue time <= entryTime
                pq.offer(arr[i]);
                i++;
            }

            int[] processed = pq.poll();
            ans.add(processed[0]);
            entryTime += processed[2];
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}
