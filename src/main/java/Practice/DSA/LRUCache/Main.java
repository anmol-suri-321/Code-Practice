package Practice.DSA.LRUCache;

import Practice.DSA.LRUCache.BruteForce.LRUCache;

public class Main {
    public static void main(String[] args) {
        String[] inputKey = new String[]{"LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"};
        int[][] inputValue = new int[][] {{2}, {1, 1}, {2, 2}, {1}, {3, 3}, {2}, {4, 4}, {1}, {3}, {4}};

        LRUCache cache = new LRUCache(inputValue[0][0]);
        for(int i = 0; i < inputKey.length; i++) {
            String s = inputKey[i];
            int[] input = inputValue[i];
            if(s.equals("put")) {
                cache.put(input[0], input[1]);
            } else if(s.equals("get")) {
                cache.get(input[0]);
            }
        }
    }
}
