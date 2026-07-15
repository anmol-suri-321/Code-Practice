package Practice.DSA.BuyAndSellStock;

public class BestTimeToBuyAndSellStockWithTransactionFee {
    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;
        if (n == 0) {
            return 0;
        }

        // Initial states
        int hold = -prices[0];  // The maximum profit if you are holding a stock
        int notHold = 0;        // The maximum profit if you are not holding a stock

        for (int i = 1; i < n; i++) {
            // hold is updated, max of holding previous day, or buying today
            hold = Math.max(hold, notHold - prices[i]);
            // notHold is updated, max of not holding on previous day, or selling today
            notHold = Math.max(notHold, hold + prices[i] - fee);
        }

        return notHold; // Maximum profit when we do not hold a stock at the last day
    }
}
