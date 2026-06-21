package DSA;


import java.util.*;

public class FourSum {
    public List<List<Integer>> fourSum(int[] nums, int target) {
            Set<List<Integer>> ans = new HashSet<>();
            Arrays.sort(nums);
            for(int i = 0; i < nums.length - 2; i++) {
                for(int j = i + 1; j < nums.length - 1; j++) {
                    int k = j + 1;
                    int l = nums.length - 1;
                    while(k < l) {
                        int sum = nums[i] + nums[j] + nums[k] + nums[l];
                        if(sum < target) {
                            k++;
                        } else if (sum > target) {
                            l--;
                        } else {
                            ans.add(List.of(nums[i],nums[j],nums[k],nums[l]));
                            k++;
                            l--;
                        }
                    }
                }
            }
            return new ArrayList<>(ans);
    }
}
