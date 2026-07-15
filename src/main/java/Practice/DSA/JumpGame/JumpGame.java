package Practice.DSA.JumpGame;

public class JumpGame {
    public boolean canJump(int[] nums) {
        int n = nums.length;
        if(n == 1) return true;
        int maxIndex = 0;
        for(int i = 0; i < n - 1; i++) {
            //store the max index till jump can be made
            maxIndex = Integer.max(maxIndex, i + nums[i]);

            // this means, we cannot go further
            if(i >= maxIndex) return false;
        }
        return true;
    }
}
