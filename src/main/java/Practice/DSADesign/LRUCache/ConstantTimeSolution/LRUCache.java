package Practice.DSADesign.LRUCache.ConstantTimeSolution;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
    private final int capacity;
    // DummyNode : nodes insert after this
    private CacheNode head;
    // DummyNode : nodes delete before this
    private CacheNode tail;
    private final Map<Integer, CacheNode> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.head = new CacheNode(0, 0);
        this.tail = new CacheNode(0, 0);
        head.next = tail;
        tail.prev = head;
        this.cache = new HashMap<>();
    }

    public int get(int key) {
        if(cache.containsKey(key)) {
            CacheNode node = cache.get(key);

            // remove node
            removeNode(node);

            // insert after head
            insertNodeAtTheHead(node);

            return node.getValue();
        }

        return -1;
    }

    public void put(int key, int value) {
        if(cache.containsKey(key)) {
            CacheNode node = cache.get(key);
            removeNode(node);

            node.setValue(value);

            insertNodeAtTheHead(node);
        } else {
            if(cache.size() == capacity) {
                CacheNode node = tail.prev;
                // remove just prev node from tail
                removeNode(node);
                cache.remove(node.getKey());
            }
            CacheNode newNode = new CacheNode(key, value);
            insertNodeAtTheHead(newNode);
            cache.put(key, newNode);
        }
    }

    private void insertNodeAtTheHead(CacheNode node) {
        CacheNode headNext = head.next;
        node.prev = head;
        head.next = node;
        node.next = headNext;
        headNext.prev = node;
    }

    private static void removeNode(CacheNode node) {
        CacheNode nodePrev = node.prev;
        CacheNode nodeNext = node.next;

        nodePrev.next = nodeNext;
        nodeNext.prev = nodePrev;
    }
}

