package Practice.DSA;

import java.util.Arrays;

public class NumberOfLongestIncreasingSubsequence {
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        if (n == 0)
            return 0;

        int[] dp = new int[n]; // Length of LIS ending at each index
        int[] count = new int[n]; // Count of LIS ending at each index
        Arrays.fill(dp, 1); // Each element is at least an LIS of length 1
        Arrays.fill(count, 1); // Each element starts with one LIS

        int maxLength = 1; // Length of the longest increasing subsequence
        int totalLIS = 0; // Total number of LIS

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        count[i] = count[j]; // Reset count because we found a longer LIS
                    } else if (dp[j] + 1 == dp[i]) {
                        count[i] += count[j]; // Add count because this is another LIS
                    }
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        // Sum up the counts of all LIS of length `maxLength`
        for (int i = 0; i < n; i++) {
            if (dp[i] == maxLength) {
                totalLIS += count[i];
            }
        }

        return totalLIS;
    }
}
