package DSA;


import java.util.Arrays;

public class KokoEatingBananas {
    public int minEatingSpeed(int[] piles, int h) {
        Arrays.sort(piles);
        int l=1;
        int r=piles[piles.length-1];
        int ans = r;
        while(l<=r) {
            int mid = l + (r-l)/2;
            int hours = 0;
            // Calculate hours required for the current speed `mid`
            for(int x:piles) {
                hours += Math.ceil((double)x/mid);
            }
            // If Koko can eat all bananas in `h` hours or less, try a smaller speed
            if(hours <= h) {
                ans = Math.min(ans, mid);
                r = mid-1; // Update answer
            } else {
                l = mid+1;  // Increase speed since current speed is too slow
            }
        }
        return ans;
    }
}
