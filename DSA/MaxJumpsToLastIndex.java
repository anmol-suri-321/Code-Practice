package DSA;

import java.util.Arrays;

public class MaxJumpsToLastIndex {
    public int maximumJumps(int[] nums, int target) {
        int n = nums.length;
        // Create a dp array to store the max jumps till index i
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        for (int i = 0; i < n - 1; i++) {
            // this ensures index i from where jump is made to j should be reachable already
            if (dp[i] == -1)
                continue;
            for (int j = i + 1; j < n; j++) {
                int diff = Math.abs(nums[j] - nums[i]);
                // if diff is within the range, update the jth index with value store at i
                if (diff <= target) {
                    dp[j] = Math.max(dp[j], dp[i] + 1);
                }
            }
        }
        // returns value store at last index, if its -1, then it means last index was
        // unreachable
        return dp[n - 1];
    }
}
