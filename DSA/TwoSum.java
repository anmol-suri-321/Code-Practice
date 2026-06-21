package DSA;


import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            if(m.containsKey(target - nums[i])) {
                return new int[]{i, m.get(nums[target - nums[i]])};
            }
            m.put(nums[i], i);
        }
        return new int[]{0,1};
    }
}
