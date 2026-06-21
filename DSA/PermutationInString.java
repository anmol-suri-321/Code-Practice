package DSA;

public class PermutationInString {
    public boolean checkInclusion(String s1, String s2) {
        int[] hash1 = new int[26];
        for(char c : s1.toCharArray()) {
            hash1[c - 'a']++;
        }

        int i = 0; int j = 0;
        int windowSize = s1.length();
        int[] hash2 = new int[26];
        while(j < s2.length()) {
            hash2[s2.charAt(j) - 'a']++;
            if(j - i + 1 == windowSize) {
                if(isPresent(hash1, hash2)) {
                    return true;
                } else {
                    hash2[s2.charAt(i) - 'a']--;  // shrink the window from left and reduce the freq from the window map
                    i++;
                }
            }
            j++;
        }

        return false;
    }

    private boolean isPresent(int[] hash1, int[] hash2) {
        for (int k = 0; k < 26; k++) {
            if (hash1[k] != hash2[k]) return false;
        }
        return true;
    }
}
