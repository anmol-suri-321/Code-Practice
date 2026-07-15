package Practice.DSADesign.LRUCache.LinearTimeSolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class LRUCache {
    private final int capacity;
    private LinkedList<CacheNode> linkedList;
    private final Map<Integer, CacheNode> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.linkedList = new LinkedList<>();
        this.cache = new HashMap<>();
    }

    public void put(int key, int value) {
        CacheNode newNode = new CacheNode(key, value);

        if(cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            node.setValue(value);
            linkedList.remove(node);
            linkedList.addFirst(node);
        } else {
            if(cache.size() == capacity) {
                CacheNode removedNode = linkedList.removeLast();
                cache.remove(removedNode.getKey());
            }
            linkedList.addFirst(newNode);
            cache.put(key, newNode);
        }
    }

    public int get(int key) {
        if(cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            // remove the node from the linked list
            linkedList.remove(node);
            // since its recently used, put it in the front part of the linked list
            linkedList.addFirst(node);

            return node.getValue();
        }

        return -1;
    }
}
