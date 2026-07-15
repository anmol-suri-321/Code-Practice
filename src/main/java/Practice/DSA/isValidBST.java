package Practice.DSA;

public class isValidBST {
    public boolean isValidBSTQ(TreeNode root) {
        return isBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    boolean isBST(TreeNode root, int min, int max) {
        if(root == null) return true;

        if(root.val > min && root.val < max) {
            return isBST(root.left, min, root.val) && isBST(root.right, root.val, max);
        } else {
            return false;
        }
    }
}
