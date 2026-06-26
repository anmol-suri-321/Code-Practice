package Practice.DSA;


import java.util.HashMap;
import java.util.Map;

public class minOperationsToEmptyArray {
    public int minOperations(int[] nums) {
        Map<Integer, Integer> hash = new HashMap<>();
        for (int x : nums) {
            hash.put(x, hash.getOrDefault(x, 0) + 1);
        }
        int ans = 0;
        for (Map.Entry<Integer, Integer> x : hash.entrySet()) {
            int val = x.getValue();
            int groupOf3 = val / 3;
            int rem = val % 3;
            if (rem == 1) {
                // If there's a remainder of 1 after forming groups of 3, convert one group  of 3 into two groups of 2 for the odd element.
                if (groupOf3 > 0) {
                    groupOf3--;
                    rem += 3;
                } else {
                    // otherwise we cannot empty array
                    return -1;
                }
            }
            ans += groupOf3 + rem / 2;
        }
        return ans;
    }
}
