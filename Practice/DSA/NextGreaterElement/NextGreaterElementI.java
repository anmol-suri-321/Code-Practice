package Practice.DSA.NextGreaterElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NextGreaterElementI {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> hash = new HashMap<>();
        int n = nums2.length;
        Stack<Integer> st = new Stack<>();
        for(int i = n-1; i >= 0; i--) {
            // pop until element index if current element is greater than the top of stack
            while(!st.isEmpty() && st.peek() <= nums2[i]) {
                st.pop();
            }
            // After all popping is done, if stack is non-empty, its the answer for the index
            if(!st.isEmpty()) {
                hash.put(nums2[i], st.peek());
            } else {
                hash.put(nums2[i], -1);
            }
            st.push(nums2[i]);
        }

        for(int i = 0; i < nums1.length; i++) {
            nums1[i] = hash.getOrDefault(nums1[i], -1);
        }
        return nums1;
    }
}
