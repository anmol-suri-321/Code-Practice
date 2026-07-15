package Practice.DSA.JumpGame;

public class JumpGameII {
    public int jump(int[] nums) {
        int n = nums.length;
    
        int maxReach = 0;
        int currJumpEnd = 0;
        int ans = 0;

        for(int i = 0; i < n - 1; i++) {
            // This stores the max reach from index i
            maxReach = Integer.max(maxReach, i + nums[i]);

            // when index i is reached to the current jump end, jump count increases
            if(i == currJumpEnd) {
                ans++;
                currJumpEnd = maxReach;

                // This means that current jump can reach the end of the array, then return ans
                if(currJumpEnd >= n - 1) {
                    return ans;
                }
            }
        }
        return ans;
    }
}
