package Practice.DSA.NextGreaterElement;

import java.util.Stack;

public class NextGreaterElementII {
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];

        Stack<Integer> st = new Stack<>();
        for (int i = 2 * n - 1; i >= 0; i--) {
            int index = i % n;
            while (!st.isEmpty() && nums[st.peek()] <= nums[index]) {
                st.pop();
            }

            ans[index] = st.isEmpty() ? -1 : nums[st.peek()];
            st.push(index);
        }

        return ans;
    }
}
