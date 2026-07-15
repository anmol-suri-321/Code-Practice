package Practice.DSA.BuyAndSellStock;


public class BestTimetoBuyandSellStock {
    public int maxProfit(int[] prices) {
        int ans = Integer.MIN_VALUE;
        int minTillNow = Integer.MAX_VALUE;
        for (int i = 0; i < prices.length; i++) {
            minTillNow = Integer.min(minTillNow, prices[i]);
            ans = Integer.max(ans, prices[i] - minTillNow);
        }

        if(ans == Integer.MIN_VALUE) {
            return 0;
        } else {
            return ans;
        }
    }
}
