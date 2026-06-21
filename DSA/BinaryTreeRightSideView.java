package DSA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeRightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if(root == null) return ans;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while(!q.isEmpty()) {
            int size = q.size();

            for(int i = 0; i < size; i++) {
                TreeNode front = q.poll();
                if(i == size - 1) {
                    ans.add(front.val);
                }
                if(front.left != null) {
                    q.offer(front.left);
                }
                if(front.right != null) {
                    q.offer(front.right);
                }
            }
        }

        return ans;
    }
}
