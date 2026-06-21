package DSA;

import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        Map<Character, Integer> hashT = new HashMap<>();
        for (char c : t.toCharArray()) {
            hashT.put(c, hashT.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> hashNew = new HashMap<>();
        int i = 0;
        int ansi = 0;
        int ansj = s.length() + 1;
        int matched = 0;
        for (int j = 0; j < s.length(); j++) {
            char rightChar = s.charAt(j);
            if (hashT.containsKey(rightChar)) {
                hashNew.merge(rightChar, 1, Integer::sum);

                if (hashNew.get(rightChar).intValue() 
                                == hashT.get(rightChar).intValue()) {
                    matched++;
                }
            }
            
            // Try to shrink the window when we have all required matches
            while (matched == hashT.size()) {
                char leftChar = s.charAt(i);
                if (j - i + 1 < ansj - ansi) {
                    ansi = i;
                    ansj = j + 1;
                }

                // Reduce the frequency of the leftmost character and shrink the window
                if (hashT.containsKey(leftChar)) {
                    if (hashNew.get(leftChar).intValue() 
                                    == hashT.get(leftChar).intValue()) {
                        matched--;
                    }
                    hashNew.put(leftChar, hashNew.get(leftChar) - 1);
                }
                i++;
            }
        }

        // Return the smallest window or an empty string if no valid window exists
        return ansj > s.length() ? "" : s.substring(ansi, ansj);
    }
}
