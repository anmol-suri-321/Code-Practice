package Practice.DSA;


import java.util.HashSet;

public class minOpToMakeArrayContinous {
    public int minOperations(int[] nums) {
        HashSet<Integer> hash = new HashSet<>();
        for(int x : nums) {
            hash.add(x);
        }
        int[] newNums = hash.stream().sorted().mapToInt(Number::intValue).toArray();
        int i = 0;
        int ans = 0;
        for (int j = 0; j< newNums.length; j++) {
            // for every j index, we need to find the max continous subarray of size j - i + 1
            while(nums[j] - nums[i] >= nums.length) {
                i++;
            }
            // The ith index marks the start of the continuous subarray until the jth index
            ans = Integer.max(ans, j - i + 1);
        }
        return nums.length - ans;
    }
}
