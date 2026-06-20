package DSA.HouseRobber;

public class HouseRobberI {
    public int rob(int[] nums) {
        int n = nums.length;
        if(n == 1) return nums[0]; 

        return houseRobberI(nums, 0, n-1);
    }

    protected int houseRobberI(int[] nums, int start, int end) {
        // Stores the value for i-1th position
        int n_1 = 0;
        // Stores the value for i-2th position
        int n_2 = 0;
        for(int i = start; i <= end; i++) {
            int curr = Integer.max(n_1, nums[i] + n_2);
            n_2 = n_1;
            n_1 = curr;
        }
        // return the n_1th value which stores the max loot
        return n_1;
    }
}
