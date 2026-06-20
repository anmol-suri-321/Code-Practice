package DSA.BuyAndSellStock;

public class BestTimeToBuyAndSellStockWithCoolDown {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        if(n == 1) {
            return 0;
        }
        int hold = -prices[0];  // Maximum profit if you're holding a stock at the end of day i
        int notHoldOrCoolDown = 0;  // Maximum profit if you are not holding a stock on day i
        int sell = 0;  // Maximum profit if you sold the stock on day i

        for(int i = 1; i < n; i++) {
            int prevHold = hold;

            // hold a current stock, or buy today
            hold = Math.max(hold, notHoldOrCoolDown - prices[i]); 

            // still not hold the stock or sold it on previous day(stay in cooldown)
            notHoldOrCoolDown = Math.max(notHoldOrCoolDown, sell);

            // sell the stock today
            sell = prevHold + prices[i];
        }

        // return the max of not holding a stock(cooldown state on ith day) or sold on ith day
        return Math.max(notHoldOrCoolDown, sell);
    }
}