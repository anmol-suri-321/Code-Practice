package DSA;

public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        int n = nums.length;

        int maxSubArraySum = nums[0];
        // this tracks the current subArray sum
        int currSum = nums[0];
        for(int i = 1; i < n; i++) {
            currSum += nums[i];
            // It means, current index is the start of the subarray
            if(nums[i] > currSum) {
                currSum = nums[i];
            }
            // update the maxSum on every iteration
            maxSubArraySum = Math.max(maxSubArraySum, currSum);
        }
        
        return maxSubArraySum;
    }
}
