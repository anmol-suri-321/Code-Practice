package Practice.DSA;

import java.util.Arrays;

public class KthLargestIntegerinStringArray {
    public String kthLargestNumber(String[] nums, int k) {
        Arrays.sort(nums, (s1,s2) -> customCompare(s1, s2));

        return nums[k - 1];
    }

    private int customCompare(String s1, String s2) {
        int len1 = s1.length();
            int len2 = s2.length();
            
            if(len1 - len2 == 0) {
                for(int i = 0; i < len1; i++) {
                    char c1 = s1.charAt(i);
                    char c2 = s2.charAt(i);
                    if(c1 != c2) {
                        return (c2 - '0') - (c1 - '0');
                    }
                }
            }

        return len2 - len1;
    }
}
