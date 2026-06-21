package DSA;

import java.util.Stack;

public class LargestRectangleInHistogram {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Stack<Integer> st = new Stack<>();

        for(int i = 1; i < n; i++) {
            // pop the stack until we find next smaller element in the stack
            while(!st.isEmpty() && heights[st.peek()] > heights[i]) {
                st.pop();
            }

            // set the stack top to the index array
            left[i] = st.isEmpty() ? 0 : st.peek();

            st.push(i);
        }

        // Clear the stack for reuse
        st.clear();

        for(int i = n - 1; i >= 0 ; i--) {
            // pop the stack until we find next smaller element in the stack
            while(!st.isEmpty() && heights[st.peek()] > heights[i]) {
                st.pop();
            }

            // set the stack top to the index array
            right[i] = st.isEmpty() ? n : st.peek();
            
            st.push(i);
        }

        int ans = 1;
        for(int i = 0; i < n; i++) {
            ans = Math.max(ans, (right[i] - left[i] - 1) * heights[i]);
        }

        return ans;
    }
}
