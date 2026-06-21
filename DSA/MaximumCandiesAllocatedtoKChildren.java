package DSA;


import java.util.Arrays;

public class MaximumCandiesAllocatedtoKChildren {
    public int maximumCandies(int[] cand, long k) {
        int l = 1;
        int r = Arrays.stream(cand).max().getAsInt();
        int ans = 0;
        while(l <= r) {
            int mid = l + (r - l)/2;
            long childCount = 0; // store the number of child satisfied with k candles
            for(int val : cand) {
                childCount += val/mid;
            }
            if(childCount >= k) {
                // if children count greater than k, means still look for more candles per child
                ans = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return ans;
    }
}
