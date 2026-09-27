package Practice.DSADesign.LFUCache;

import java.util.HashMap;
import java.util.Map;

public class LFUCache {
    private int capacity;

    // This stores the cache node per key
    Map<Integer, CacheNode> cacheMap;

    // This stores the Dlinked list per freq
    Map<Integer, DlinkedList> freqMap;

    // This is used to track the minimum freq in the LFU cache
    private int minFreq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 0;
    }

    /**
      Gets the value of the key if the key exists in the cache. Otherwise, returns -1.
     **/
    int get(int key) {
        if(cacheMap.containsKey(key)) {
            CacheNode existingNode = cacheMap.get(key);
            incrementFrequency(existingNode);
            return existingNode.getVal();
        }

        return -1;
    }

    /** Bumps a node's frequency by 1 and relocates it to the new frequency's list. */
    private void incrementFrequency(CacheNode node) {
        int oldFreq = node.getFreq();
        DlinkedList oldList = freqMap.get(oldFreq);
        oldList.remove(node);

        if (oldList.isEmpty() && minFreq == oldFreq) {
            minFreq++;
        }

        node.setFreq(oldFreq + 1);
        freqMap.computeIfAbsent(oldFreq + 1, f -> new DlinkedList()).insertAtHead(node);
    }

    /**
     * Update the value of the key if present, or inserts the key if not already present.
     * When the cache reaches its capacity, it should invalidate and remove the least frequently used key before inserting a new item.
     * For this problem, when there is a tie (i.e., two or more keys with the same frequency), the least recently used key would be invalidated.
     */
    void put(int key, int val) {
        if(cacheMap.containsKey(key)) {
            CacheNode existing = cacheMap.get(key);

            existing.setVal(val);
            incrementFrequency(existing);
            return;
        }

        if(cacheMap.size() == capacity) {
            // find the LFU node, and evict it from the map and freq map
            DlinkedList minFreqList = freqMap.get(minFreq);
            CacheNode evictNode = minFreqList.getTail().prev;

            // This removes the node which is least recently used
            minFreqList.remove(evictNode);
            cacheMap.remove(evictNode.getKey());
        }

        CacheNode newNode = new CacheNode(key, val);
        cacheMap.put(key, newNode);

        freqMap.computeIfAbsent(1, freq -> new DlinkedList()).insertAtHead(newNode);
        minFreq = 1;
    }
}
