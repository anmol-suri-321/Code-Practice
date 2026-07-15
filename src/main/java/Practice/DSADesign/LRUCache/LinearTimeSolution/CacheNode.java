package Practice.DSADesign.LRUCache.LinearTimeSolution;

public class CacheNode {
    private int key;
    private int value;

    public CacheNode(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
