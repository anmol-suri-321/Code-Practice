package Practice.DSA;


import java.util.HashMap;
import java.util.Map;

public class SubArraySumK {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        int sum = 0;
        int ans = 0;
        for (int v : nums) {
            sum = sum + v;
            if (m.containsKey(k - v)) {
                ans += m.get(k - v);
            }
            int old = m.getOrDefault(sum, 0);
            m.put(sum, old + 1);
        }
        return ans;
    }
}
