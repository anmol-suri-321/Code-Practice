package Practice.DSA;

import java.util.PriorityQueue;

public class TotalCostToHireKWorkers {
    public long totalCost(int[] costs, int k, int candidates) {
        int i = 0;
        int j = costs.length - 1;

        // Create two min Heaps for left and right part of array of size `candidates`
        PriorityQueue<Integer> leftHeap = new PriorityQueue<>();
        PriorityQueue<Integer> rightHeap = new PriorityQueue<>();

        // Fill the left Heap, until it reaches the jth index
        while (i <= j && leftHeap.size() < candidates) {
            leftHeap.offer(costs[i]);
            i++;
        }

        // Fill the right heap, keeping constraint that both might not oeverlapped indexes
        while (i <= j && rightHeap.size() < candidates) {
            rightHeap.offer(costs[j]);
            j--;
        }

        long cost = 0;
        while (k > 0) {
            // if min of left heap value is lesser or equal to min of right heap, or if right heap is empty
            if (!leftHeap.isEmpty() && (rightHeap.isEmpty() || leftHeap.peek() <= rightHeap.peek())) {
                cost += leftHeap.poll();
                // this cheak ensures that, no overlapped values are pushed to the heap
                if (i <= j) {
                    leftHeap.offer(costs[i]);
                    i++;
                }
            } else {
                cost += rightHeap.poll();
                // this cheak ensures that, no overlapped values are pushed to the heap
                if (i <= j) {
                    rightHeap.offer(costs[j]);
                    j--;
                }
            }
            k--;
        }

        return cost;
    }
}
