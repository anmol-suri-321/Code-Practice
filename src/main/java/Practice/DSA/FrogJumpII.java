package Practice.DSA;


public class FrogJumpII {
    public int maxJump(int[] stones) {
        if(stones.length == 2) {
            return stones[1];
        }
        int ansOdd = 0;
        for(int i = 0; i < stones.length - 2; i+=2) {
            ansOdd = Integer.max(ansOdd, stones[i + 2] - stones[i]);
        }

        int ansEven = 0;
        for(int i = 1; i < stones.length - 2; i+=2) {
            ansEven = Integer.max(ansEven, stones[i + 2] - stones[i]);
        }
        return Integer.max(ansOdd, ansEven);
    }


    /**
     * second solution with better space complexity optimised calculations
     * **/
    public int maxJump2(int[] stones) {
        int n = stones.length;
        if (n == 2) {
            return stones[1]; // The frog jumps directly from stone[0] (position 0) to stone[1]
        }
        // Initialize variables for tracking max jumps in two strategies
        int maxJumpOdd = 0;
        int maxJumpEven = 0;

        // Check jumps when skipping stones alternatively
        for (int i = 1; i < n - 1; i++) {
            if (i % 2 == 0) {
                maxJumpEven = Math.max(maxJumpEven, stones[i + 1] - stones[i - 1]);
            } else {
                maxJumpOdd = Math.max(maxJumpOdd, stones[i + 1] - stones[i - 1]);
            }
        }
        return Integer.max(maxJumpEven, maxJumpOdd);
    }
}
