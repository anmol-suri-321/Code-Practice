package Practice.DSA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LIS {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        int lis = 1;
        for(int i = 1; i < n; i++) {
            for(int j = 0; j < i; j++) {
                if(nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    lis = Math.max(lis, dp[i]);
                }
            }
        }
        return lis;
    }



    // This is the binary search n*logn solution
    public int lengthOfLISBinarySearch(int[] nums) {
        int n = nums.length;
        List<Integer> temp = new ArrayList<>();
        temp.add(nums[0]);
        
        for(int i = 1; i < n; i++) {
            // find the index position in the temp array
            int index = findIndex(temp, nums[i]);

            // if at last it needs to be added
            if(index == -1) {
                temp.add(nums[i]);
            } else {
                // set the value at index
                temp.set(index, nums[i]);
            }
        }
        return temp.size();
    }

    private int findIndex(List<Integer> temp, int x) {
        int l = 0;
        int r = temp.size() - 1;

        while(l <= r) {
            int mid = l + (r - l)/2;
            if(x <= temp.get(mid)) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }

        return l == temp.size() ? -1 : l;
    }
}
