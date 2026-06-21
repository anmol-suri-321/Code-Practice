package DSA;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        // deque is designed to always have the smallest element at the front
        Deque<Integer> dq = new LinkedList<>();
        List<Integer> ans = new ArrayList<>();
        for(int i = 0; i < nums.length; i++) {
            // remove indexes from the deque, which are out of window
            while(!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }

            // Larger eles indexes than current are removed from the deque 
            while(!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) {
                dq.pollLast();
            }

            dq.offerLast(i);

            if(i >= k - 1) {
                ans.add(nums[dq.peekFirst()]);
            }
        }
        int[] result = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            result[i] = ans.get(i);  // Copy elements to the array
        }

        return result;
    }
}
