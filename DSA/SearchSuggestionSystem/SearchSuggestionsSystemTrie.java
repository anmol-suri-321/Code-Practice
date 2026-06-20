package DSA.SearchSuggestionSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SearchSuggestionsSystem {
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        Arrays.sort(products);
        CreateTrie trie = new CreateTrie();
        for(String word : products) {
            trie.insert(word);
        }
        
        String prefix = "";
        List<List<String>> result = new ArrayList<>();
        for(int i = 0; i < searchWord.length(); i++) {
            prefix += searchWord.charAt(i);
            result.add(trie.getWordsWithPrefix(prefix));
        }

        return result;
    }
}

// TrieNode class
class TrieNode {
    TrieNode[] links = new TrieNode[26];
    boolean flag;
    List<String> wordsWithPrefix = new ArrayList<>();

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
        if (containsChar(ch)) {
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

class CreateTrie {
    TrieNode root;

    public CreateTrie() {
        this.root = new TrieNode();
    }

    // Insert a word into the Trie
    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsChar(ch)) {
                node.put(ch);
            }
            node = node.get(ch);

            if(node.wordsWithPrefix.size() < 3) {
                node.wordsWithPrefix.add(word);
            }
        }
        node.setEnd();
    }

    // Check List of Words in the Trie starts with the given prefix
    public List<String> getWordsWithPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.containsChar(ch)) {
                return new ArrayList<>();
            }
            node = node.get(ch);
        }
        return node.wordsWithPrefix;
    }
}
