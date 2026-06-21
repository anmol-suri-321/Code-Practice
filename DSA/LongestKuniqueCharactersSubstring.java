package DSA;


import java.util.HashMap;
import java.util.Map;

public class LongestKuniqueCharactersSubstring {
    public int longestkSubstr(String s, int k) {
        int n = s.length();
        int i = 0; int j = 0;
        Map<Character, Integer> hash = new HashMap<>();

        int ans = -1;
        while(j < n) {
            char ch = s.charAt(j);
            hash.put(ch, hash.getOrDefault(ch, 0) + 1);

            // If the map size exceeds k, shrink the window from the left
            while (hash.size() > k) {
                char leftChar = s.charAt(i);
                hash.put(leftChar, hash.get(leftChar) - 1);
                if (hash.get(leftChar) == 0) {
                    hash.remove(leftChar);
                }
                i++;
            }

            if(hash.size() == k) {
                ans = Integer.max(ans, j - i + 1);
            }
            j++;
        }
        return ans;
    }
}
