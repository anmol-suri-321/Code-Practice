package DSA;

import java.util.LinkedList;
import java.util.Queue;

public class MaximumLevelSumofBinaryTree {
    public int maxLevelSum(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        int ans = 1;
        int level = 1;
        int maxSum = Integer.MIN_VALUE;
        while (!q.isEmpty()) {
            int size = q.size();
            int sum = 0;
            for (int i = 0; i < size; i++) {
                TreeNode front = q.poll();
                sum += front.val;
                if (front.left != null) {
                    q.offer(front.left);
                }
                if (front.right != null) {
                    q.offer(front.right);
                }
            }

            if (sum > maxSum) {
                ans = level;
                maxSum = sum;
            }
            level++;
        }

        return ans;
    }
}
