package DSA.SearchSuggestionSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchSuggestionsSystemBinarySearch {
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        Arrays.sort(products);

        String prefix = "";
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < searchWord.length(); i++) {
            prefix += searchWord.charAt(i);

            int index = findFirstIndex(products, prefix);
            result.add(suggestions(products, prefix, index));
        }

        return result;
    }

    private int findFirstIndex(String[] products, String prefix) {
        int left = 0, right = products.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (products[mid].compareTo(prefix) < 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left; // left is the first index where the prefix can be inserted
    }

    private List<String> suggestions(String[] products, String prefix, int index) {
        List<String> ans = new ArrayList<>();
        for (int i = index; i < Math.min(index + 3, products.length); i++) {
            if (products[i].startsWith(prefix)) {
                ans.add(products[i]);
            }
        }
        return ans;
    }
}
