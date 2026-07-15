package Practice.DSA;

public class CountGoodNodesBinaryTree {
    public int goodNodes(TreeNode root) {
        return preOrderTraversal(root, Integer.MIN_VALUE);
    }

    private int preOrderTraversal(TreeNode root, int maxSoFar) {
        if(root == null) {
            return 0;
        }
        int count = 0;
        if(root.val >= maxSoFar) {
            count = 1;
            maxSoFar = root.val;
        }

        return count + preOrderTraversal(root.left, maxSoFar) + preOrderTraversal(root.right, maxSoFar);

    }
}
