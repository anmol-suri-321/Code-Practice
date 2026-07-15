package Practice.DSA;


public class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int i = 0; int j = height.length - 1;
        int ans = 0;
        while(i < j) {
            ans = Integer.max(ans, Integer.min(height[i], height[j])*(j-i));
            if(height[i] <= height[j]) {
                i++;
            } else {
                j--;
            }
        }
        return ans;
    }
}
