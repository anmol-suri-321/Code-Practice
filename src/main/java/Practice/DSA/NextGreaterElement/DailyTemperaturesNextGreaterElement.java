package Practice.DSA.NextGreaterElement;

import java.util.Stack;

public class DailyTemperaturesNextGreaterElement {
    public int[] dailyTemperatures(int[] t) {
        // This problem looks like the next greater element
        int n = t.length;
        int[] ans = new int[n];

        int nextgreater = t[n-1];
        // Stores the indexes of the next greater element
        Stack<Integer> st = new Stack<>();
        for(int i = n-1; i >= 0; i--) {
            // pop until element index if current element is greater than the top of stack
            while(!st.isEmpty() && t[st.peek()] <= t[i]) {
                st.pop();
            }
            // After all popping is done, if stack is non-empty, its the answer for the index
            if(!st.isEmpty()) {
                ans[i] = st.peek() - i;
            } else {
                ans[i] = 0;
            }
            st.push(i);
        }

        return ans;
    }
}
