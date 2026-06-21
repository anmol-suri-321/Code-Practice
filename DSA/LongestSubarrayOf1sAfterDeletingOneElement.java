package DSA;

public class LongestSubarrayOf1sAfterDeletingOneElement {
    public int longestSubarray(int[] nums) {
        int count = 0;
        int ans = 0;
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            // increase the count of zeros
            if (nums[j] == 0) {
                count++;
            }
            // Start contracting the sliding window from left, as soon as the count of zero more than 1
            if (count > 1) {
                if (nums[i] == 0) {
                    count--;
                }
                i++;
            }
            ans = Math.max(ans, j - i);
        }

        return ans;
    }
}
