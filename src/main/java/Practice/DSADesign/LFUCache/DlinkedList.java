package Practice.DSADesign.LFUCache;

public class DlinkedList {
    private CacheNode head;
    private CacheNode tail;
    private int size = 0;

    public DlinkedList() {
        this.head = new CacheNode(0, 0);
        this.tail = new CacheNode(0, 0);

        head.next = tail;
        tail.prev = head;
    }

    public CacheNode getHead() {
        return head;
    }

    public CacheNode getTail() {
        return tail;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    void remove(CacheNode node) {
        CacheNode nodeNext = node.next;
        CacheNode nodePrev = node.prev;

        nodePrev.next = nodeNext;
        nodeNext.prev = nodePrev;
        size--;
    }

    void insertAtHead(CacheNode node) {
        CacheNode headNext = head.next;

        node.next = headNext;
        node.prev = head;

        headNext.prev = node;
        head.next = node;
        size++;
    }
}
