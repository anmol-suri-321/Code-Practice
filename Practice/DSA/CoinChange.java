package Practice.DSA;

import java.util.Arrays;

public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        int n = coins.length;
        int[] dp = new int[amount+1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for(int target = 1; target <= amount; target++) {
            for(int j = 0; j < n; j++) {
                // Left amount if the coin at index j is taken
                int leftAmount = target - coins[j];

                // Check if that amount is greater than 0 and pre-calculated in the DP
                if(leftAmount >= 0 && dp[leftAmount] != Integer.MAX_VALUE) {
                    dp[target] = Math.min(1 + dp[leftAmount], dp[target]);
                }
            }
        }
        if(dp[amount] == Integer.MAX_VALUE) {
            return -1;
        }
        return dp[amount];
    }
}
