package DSA.BuyAndSellStock;


import java.util.Arrays;

public class BestTimetoBuyandSellStock3 {
    /**
      The first transaction happens in the first part (from day 0 to day i).
      The second transaction happens in the second part (from day i to day n-1)
    **/
    public int maxProfit(int[] prices) {
        int n = prices.length;
        if (n == 0 || n == 1) {
            return 0;
        }
        int[] leftDp = new int[n];
        int[] rightDp = new int[n];

        Arrays.fill(leftDp, 0);
        Arrays.fill(rightDp, 0);
        int minTillNow = prices[0];
        for (int i = 1; i < n; i++) {
            minTillNow = Integer.min(minTillNow, prices[i]);
            leftDp[i] = Integer.max(leftDp[i - 1], prices[i] - minTillNow);
        }

        int maxTillNow = prices[n - 1];
        for (int j = n - 2; j >= 0; j--) {
            maxTillNow = Integer.max(maxTillNow, prices[j]);
            rightDp[j] = Integer.max(rightDp[j + 1], maxTillNow - prices[j]);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Integer.max(ans, leftDp[i] + rightDp[i]);
        }

        return ans;
    }

    public static int maxProfitSecondSolution(int[] prices) {
        int n = prices.length;

        // Base case initialization
        int firstBuy = Integer.MIN_VALUE, firstSell = 0;
        int secondBuy = Integer.MIN_VALUE, secondSell = 0;

        for (int i = 0; i < n; i++) {
            // Update the states in reverse order (second transaction first)
            secondSell = Math.max(secondSell, secondBuy + prices[i]);  // Sell the second stock
            secondBuy = Math.max(secondBuy, firstSell - prices[i]);    // Buy the second stock
            firstSell = Math.max(firstSell, firstBuy + prices[i]);     // Sell the first stock
            firstBuy = Math.max(firstBuy, -prices[i]);                 // Buy the first stock
        }

        // The final answer is the maximum profit from two sells (secondSell will be larger)
        return secondSell;
    }
}
