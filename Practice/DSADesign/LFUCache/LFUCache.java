package Practice.DSADesign.LFUCache;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class LFUCache {
    private final int capacity;
    Map<Integer, Integer> keyToVal;       // key → value
    Map<Integer, Integer> keyToFreq;      // key → frequency
    Map<Integer, LinkedHashSet<Integer>>  freqToKeys; // freq → keys (LRU order)
    int minFreq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        return 0;
    }

    public void put(int key, int value) {
        if(keyToVal.containsKey(key)) {
            int val = keyToVal.get(key);

        } else {
            if(keyToVal.size() == capacity) {
                Set<Integer> minKeys = freqToKeys.get(minFreq);
            } else {
                keyToVal.put(key, value);

            }
        }
    }
}
