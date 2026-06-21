package DSA;

public class MaximumProductSubarray {
    public int maxProduct(int[] nums) {
        int n = nums.length;

        int ans = nums[0];

        // tracks of the minimum product ending at the current element
        int minProd = nums[0];
        // tracks the maximum product ending at the current element
        int maxProd = nums[0];
        for(int i = 1; i < n; i++) {
            // swap them to handle the sign change
            if(nums[i] < 0) {
                int temp = minProd;
                minProd = maxProd;
                maxProd = temp;
            }

            // minimum of either the current element alone (i.e., starting a new subarray) or the product
            minProd = Math.min(minProd*nums[i], nums[i]);
            // maximum of the current element or the product of maxProd and nums[i]
            maxProd = Math.max(maxProd*nums[i], nums[i]);
            ans = Math.max(ans, maxProd);
        }

        return ans;
    }
}
