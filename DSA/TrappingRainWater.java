package DSA;


public class TrappingRainWater {
    public int trap(int[] height) {
        int n = height.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        int maxSeenTillNow = 0;
        for(int i = 0; i < n; i++) {
            maxSeenTillNow = Integer.max(maxSeenTillNow, height[i]);
            leftMax[i] = maxSeenTillNow;
        }
        maxSeenTillNow = 0;
        for(int j = n - 1; j >= 0; j--) {
            maxSeenTillNow = Integer.max(maxSeenTillNow, height[j]);
            rightMax[j] = maxSeenTillNow;
        }

        int ans = 0;
        for(int i = 0; i < n; i++) {
            ans += Integer.min(leftMax[i], rightMax[i]) - height[i];
        }

        return ans;
    }
}
