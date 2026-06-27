package Practice.DSA.LRUCache.Optimal;

import java.util.HashMap;
import java.util.Map;

// Optimal LRU Cache — all operations O(1)
// Structure: custom doubly-linked list + HashMap
//
// List layout:
//   head(dummy) ↔ [MRU] ↔ [...] ↔ [LRU] ↔ tail(dummy)
//
// head and tail are sentinel nodes — never removed, never hold real data.
// They exist so every real node always has a prev and next — no null checks needed.
//
// Rules:
//   - put/get any key → move its node right after head (most recently used)
//   - cache full → evict tail.prev (least recently used)
public class LRUCache {
    private final int capacity;
    private final CacheNode head; // dummy sentinel — MRU side
    private final CacheNode tail; // dummy sentinel — LRU side
    private final Map<Integer, CacheNode> cache; // key → node for O(1) lookup

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.head = new CacheNode(0, 0);
        this.tail = new CacheNode(0, 0);
        // wire sentinels together — empty list: head ↔ tail
        head.next = tail;
        tail.prev = head;
        this.cache = new HashMap<>();
    }

    public int get(int key) {
        if (cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            // accessed → promote to MRU position
            removeNode(node);
            insertNodeAtTheHead(node);
            return node.getValue();
        }
        return -1; // key not found
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // key exists: update value in place, promote to MRU
            CacheNode node = cache.get(key);
            node.setValue(value);
            removeNode(node);
            insertNodeAtTheHead(node);
        } else {
            if (cache.size() == capacity) {
                // cache full: evict LRU node (node just before tail sentinel)
                CacheNode lru = tail.prev;
                removeNode(lru);
                cache.remove(lru.getKey());
            }
            // insert new node as MRU
            CacheNode newNode = new CacheNode(key, value);
            insertNodeAtTheHead(newNode);
            cache.put(key, newNode);
        }
    }

    // Insert node immediately after head sentinel → becomes MRU
    // Before: head ↔ [X] ↔ ...
    // After:  head ↔ [node] ↔ [X] ↔ ...
    private void insertNodeAtTheHead(CacheNode node) {
        CacheNode headNext = head.next;
        node.prev = head;
        head.next = node;
        node.next = headNext;
        headNext.prev = node;
    }

    // Unlink node from its current position — works anywhere in list
    // Before: [prev] ↔ [node] ↔ [next]
    // After:  [prev] ↔ [next]
    private static void removeNode(CacheNode node) {
        CacheNode nodePrev = node.prev;
        CacheNode nodeNext = node.next;
        nodePrev.next = nodeNext;
        nodeNext.prev = nodePrev;
    }
}
