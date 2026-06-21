package DSA;

import java.util.Arrays;

public class NumberOfWaysToSelectBuildings {
    public long numberOfWays(String s) {
        int n = s.length();
        // This array stores the number of zeros till ith index
        long[] zeros = new long[n];
        // This array stores the number of ones till ith index 
        long[] ones = new long[n];
        Arrays.fill(zeros, 0);
        Arrays.fill(ones, 0);

        // Handles the edge case 0th index
        ones[0] = s.charAt(0) == '1' ? 1 : 0;
        zeros[0] = s.charAt(0) == '0' ? 1 : 0;

        for(int i = 1; i < n; i++) {
            if(s.charAt(i) == '1') {
                // increase the count of previous stored ones count ie.. (i-1)th index
                ones[i] = ones[i-1] + 1;
                // zeros count remain same
                zeros[i] = zeros[i-1];
            } else if(s.charAt(i) == '0') {
                // increase the count of previous stored zeros count ie.. (i-1)th index
                zeros[i] = zeros[i-1] + 1;
                // ones count remain same
                ones[i] = ones[i-1];
            }
        }

        long count = 0;
        for(int i = 1; i < n - 1; i++) {
            // case: 1 0 1
            if(s.charAt(i) == '0') {
                // number of ones on the left side * number of ones on the right side 
                count += ones[i-1] * (ones[n-1] - ones[i]);
            } 
            // case: 0 1 0
            else {
                // number of zeros on the left side * number of zeros on the right side 
                count += zeros[i-1] * (zeros[n-1] - zeros[i]);
            }
        }

        return count;
    }
}
