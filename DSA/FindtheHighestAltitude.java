package DSA;

public class FindtheHighestAltitude {
    public int largestAltitude(int[] gain) {
        int prefixSum = 0;
        int ans = 0;
        for(int x : gain) {
            prefixSum += x;
            ans = Math.max(ans, prefixSum);
        }
        return ans;
    }
}
