package DSA;

import java.util.HashMap;
import java.util.Map;

public class MaximumNumberOfVowelsInK {
    public int maxVowels(String s, int k) {
        int n = s.length();
        Map<Character, Integer> hash = new HashMap<>();
        int i = 0;
        int ans = 0;
        for(int j = 0; j < n; j++) {
            char iChar = s.charAt(i);
            char jChar = s.charAt(j);
            if(isVowel(jChar)) {
                // put the char int the map, if its is vowel
                hash.put(jChar, hash.getOrDefault(jChar, 0) + 1);
            }
            // As soon as we reach the first window size k, start incrementing i and j both
            if(j >= k - 1) {
                // calculate the number of vowels present per window
                ans = Integer.max(ans, calVowels(hash));
                if(hash.containsKey(iChar)) {
                    // check if char exists ie.. is a vowel, decrement the count
                    hash.put(iChar, hash.get(iChar) - 1);
                }
                i++;
            }
        }
        return ans;
    }

    private boolean isVowel(char j) {
        return j == 'a' || j == 'e' || j == 'i' || j == 'o' || j == 'u';
    }

    private int calVowels(Map<Character, Integer> hash) {
        int count = 0;
        for(Map.Entry<Character, Integer> e : hash.entrySet()) {
            count+= e.getValue();
        }
        return count;
    }

public class InnerMaximumNumberOfVowelsInK {
    public int maxVowels2(String s, int k) {
        int n = s.length();
        int i = 0;
        int ans = 0;
        int vowels = 0;
        for(int j = 0; j < n; j++) {
            char iChar = s.charAt(i);
            char jChar = s.charAt(j);
            if(isVowel(jChar)) {
                vowels++;
            }
            // As soon as we reach the first window size k, start incrementing i and j both
            if(j >= k - 1) {
                // calculate the number of vowels present per window
                ans = Integer.max(ans, vowels);
                if(isVowel(iChar)) {
                    vowels--;
                }
                i++;
            }
        }
        return ans;
    }
}
}



