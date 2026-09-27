package Practice.DSADesign.LFUCache;

public class CacheNode {
    int key;
    int val;
    CacheNode next;
    CacheNode prev;
    int freq = 1;

    public CacheNode(int key, int val) {
        this.key = key;
        this.val = val;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }
}
