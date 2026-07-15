package Practice.DSA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetermineTwoStringsAreClose {
    public boolean closeStrings(String w1, String w2) {
        int len1 = w1.length();
        int len2 = w2.length();
        if (len1 != len2)
            return false;

        Map<Character, Integer> m1 = new HashMap<>();
        Map<Character, Integer> m2 = new HashMap<>();

        for (int i = 0; i < len1; i++) {
            // if(m1.containsKey(w1.charAt(i))) {
            // m1.put(w1.charAt(i), m1.get(w1.charAt(i)) + 1);
            // } else {
            // m1.put(w1.charAt(i), 1);
            // }
            // Map merge method, new way to create a Hash Table
            m1.merge(w1.charAt(i), 1, Integer::sum);
        }
        for (int i = 0; i < len1; i++) {
            // Map merge method, new way to create a Hash Table
            m2.merge(w2.charAt(i), 1, Integer::sum);
        }

        // Check if the unique chars are same
        if (!m1.keySet().equals(m2.keySet())) {
            return false;
        }
        // Check if occurences, frequency of characters should match
        List<Integer> l1 = new ArrayList<>(m1.values());
        List<Integer> l2 = new ArrayList<>(m2.values());

        Collections.sort(l1);
        Collections.sort(l2);

        return l1.equals(l2);
    }
}
