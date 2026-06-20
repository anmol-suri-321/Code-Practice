package DSA;

class TrieNode {
    TrieNode[] links = new TrieNode[26];
    boolean flag;

    // Check if there is a link for the given character
    boolean containsChar(char ch) {
        return links[ch - 'a'] != null;
    }

    // Put a new node for the given character
    void put(char ch) {
        links[ch - 'a'] = new TrieNode();
    }

    // Get the node associated with the given character
    TrieNode get(char ch) {
        if(containsChar(ch)) {
            return links[ch - 'a'];
        }
        return null;
    }

    // Mark this node as the end of a word
    void setEnd() {
        this.flag = true;
    }

    // Check if this node represents the end of a word
    boolean getEnd() {
        return flag;
    }
}

class ImplementTrie {
    TrieNode root;

    public ImplementTrie() {
        this.root = new TrieNode();
    }
    
    // Insert a word into the Trie
    public void insert(String word) {
        TrieNode node = root;
        for(char ch : word.toCharArray()) {
            if(!node.containsChar(ch)) {
                node.put(ch);
            }
            node = node.get(ch);
        }
        node.setEnd();
    }
    
    // Search for a word in the Trie
    public boolean search(String word) {
        TrieNode node = root;
        for(char ch : word.toCharArray()) {
            if(!node.containsChar(ch)) {
                return false;
            }
            node = node.get(ch);
        }

        return node.getEnd();
    }
    
    // Check if any word in the Trie starts with the given prefix
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for(char ch : prefix.toCharArray()) {
            if(!node.containsChar(ch)) {
                return false;
            }
            node = node.get(ch);
        }
        return true;
    }
}
