package DSA.HouseRobber;

public class HouseRobberII {
    public int rob(int[] nums) {
        //We will solve this problem without usinf extra space DP array
        int n = nums.length;
        if(n == 1) return nums[0];
        HouseRobberI robber1 = new HouseRobberI();
        // Implement house robber problem from 0 to n-2
        int ans1 = robber1.houseRobberI(nums, 0, n-2);
        //Implement house robber problem from index 1 to n-1
        int ans2 = robber1.houseRobberI(nums, 1, n-1);

        return Integer.max(ans1, ans2);
    }
}
