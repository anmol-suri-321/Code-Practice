package DSA;


import java.util.Arrays;

public class LongestRepeatingCharacterReplacement {
    public int characterReplacement(String s, int k) {
        int[] hash = new int[26];
        Arrays.fill(hash, 0);

        int i = 0;
        hash[s.charAt(i) - 'A'] = 1;
        int ans = 1;
        int maxOccur = 1;
        for(int j = 1; j < s.length(); j++) {
            hash[s.charAt(j) - 'A']++;
            maxOccur = Integer.max(maxOccur, hash[s.charAt(j) - 'A']);
            // If the window size is > max occurrence + replaceable count k, shrink the window from the start and decrease the hash value of the ith character.
            if(j - i + 1 > maxOccur + k) {
                hash[s.charAt(i) - 'A']--;
                i++;
            }
            // on every iteration store the max size of the window
            ans = Integer.max(ans, j - i + 1);
        }
        return ans;
    }
}
