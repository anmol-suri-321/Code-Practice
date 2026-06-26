package Practice.DSA;

public class MaximiseArrayRemoveKElements {
    public int maximumTop(int[] nums, int k) {
        // If there's only one element in the array
        if (nums.length == 1) {
            // If k is even, the element will remain at the top; if odd, it will be removed
            return (k % 2 == 0) ? nums[0] : -1;
        }
        int n = nums.length;
        int maxTillK = Integer.MIN_VALUE;
        // This is to find the max ele till k - 1th pos, so that last operation is used
        // to put the max value
        for (int i = 0; i < Math.min(n, k - 1); i++) {
            maxTillK = Math.max(maxTillK, nums[i]);
        }

        // But if element at kth index is greater than maxTillK and array size is
        // greater,
        // We need to check if the element at k-th position (if exists) is a valid
        // candidate
        if (k < n) {
            maxTillK = Math.max(maxTillK, nums[k]);
        }

        return maxTillK;
    }
}
