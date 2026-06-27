package Practice.DSA.LRUCache.BruteForce;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

// Brute Force LRU Cache — O(n) removal
// Structure: LinkedList (MRU at front, LRU at back) + HashMap for O(1) lookup
// head of list = most recently used, tail = least recently used
public class LRUCache {
    private final int capacity;
    private LinkedList<CacheNode> linkedList; // front=MRU, back=LRU
    private final Map<Integer, CacheNode> cache; // key → node for O(1) lookup

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.linkedList = new LinkedList<>();
        this.cache = new HashMap<>();
    }

    public void put(int key, int value) {
        CacheNode newNode = new CacheNode(key, value);

        if (cache.containsKey(key)) {
            // key exists: update value, move to front (most recently used)
            CacheNode node = cache.get(key);
            node.setValue(value);
            linkedList.remove(node); // O(n) scan to find node
            linkedList.addFirst(node);
        } else {
            if (cache.size() == capacity) {
                // cache full: evict LRU node (back of list)
                CacheNode removedNode = linkedList.removeLast();
                cache.remove(removedNode.getKey());
            }
            // insert new node at front (most recently used)
            linkedList.addFirst(newNode);
            cache.put(key, newNode);
        }
    }

    public int get(int key) {
        if (cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            // mark as most recently used: move to front
            linkedList.remove(node); // O(n) — must scan list to find node
            linkedList.addFirst(node);
            return node.getValue();
        }
        return -1; // key not found
    }
}
