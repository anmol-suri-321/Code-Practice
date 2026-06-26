package Practice.DSA.HouseRobber;

public class HouseRobberIV {
    public int minCapability(int[] nums, int k) {
        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;

        for(int x : nums) {
            low = Integer.min(low, x);
            high = Integer.max(high, x);
        }

        while(low <= high) {
            // mid value means the we will rob the non-adj houses having value less than this
            int mid = low + (high-low)/2;
            if(canRob(nums, mid, k)) {
            // if k house are robbed with this mid value, search for lesser value in left array
                high = mid - 1;
            } else {
            // otherwise increase the mid value, and search for min value to rob k houses in right
                low = mid + 1;
            }
        }
        return low;
    }

    boolean canRob(int[] nums, int maxValue, int k) {
        int count = 0;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] <= maxValue) {
                count++;
                i++;
            }
        // if k houses are robbed all having values less than maxValue 
            if(count == k) {
                return true;
            }
        }
        // not able to rob k houses
        return false;
    }
}
