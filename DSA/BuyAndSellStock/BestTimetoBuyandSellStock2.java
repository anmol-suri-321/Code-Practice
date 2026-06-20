package DSA.BuyAndSellStock;


public class BestTimetoBuyandSellStock2 {
    public int maxProfit(int[] prices) {
        int ans = 0;
        for(int i = 0; i< prices.length; i++) {
            if(prices[i] > prices[i-1]) {
                ans += prices[i] - prices[i-1];
            }
        }
        return ans;
    }
}
