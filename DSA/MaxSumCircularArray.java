package DSA;

public class MaxSumCircularArray {
    public int maxSubarraySumCircular(int[] nums) {
        int n = nums.length;
        int curMinSum = 0;
        int minSum = Integer.MAX_VALUE;

        int curMaxSum = 0;
        int maxSum = Integer.MIN_VALUE;
        int totalSum = 0;

        for (int i = 0; i < n; i++) {
            curMinSum += nums[i];
            curMaxSum += nums[i];

            curMaxSum = Math.max(nums[i], curMaxSum);
            curMinSum = Math.min(nums[i], curMinSum);

            maxSum = Math.max(maxSum, curMaxSum);
            minSum = Math.min(minSum, curMinSum);

            totalSum += nums[i];
        }

        // If maxSum is less than 0, it means all elements are negative
        if (maxSum < 0) {
            return maxSum;
        }

        return Math.max(maxSum, totalSum - minSum);
    }
}
