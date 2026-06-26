package Practice.DSA;

public class LongestZigZagPathBinaryTree {
    int maxLength = 0;
    public int longestZigZag(TreeNode root) {
        // Start from the left direction
        helper(root, true, 0);
        // Start from the right direction
        helper(root, false, 0);

        return maxLength;
    }

    void helper(TreeNode root, boolean isLeft, int length) {
        if(root == null) {
            return;
        }
        maxLength = Math.max(maxLength, length);

        if(isLeft) {
            // prev direction is left, go to right(opp direction) and incre length by 1
            helper(root.right, false, length + 1);
            // going into the same direction as prev, reset the length to 1
            helper(root.left, true, 1);
        } else {
            // prev direction is right, go to left(opp direction) and incre length by 1
            helper(root.left, true, length + 1);
            // going into the same direction as prev, reset the length to 1
            helper(root.right, false, 1);
        }
    }
}
