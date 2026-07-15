package Practice.DSA.HouseRobber;

import Practice.DSA.TreeNode;
import java.util.Arrays;
import java.util.List;

public class HouseRobberIII {
    public int rob(TreeNode root) {
        List<Integer> ans = robHelper(root);
        // 0th index store without root value, 1th index store with root value
        return Integer.max(ans.get(0), ans.get(1));
    }

    private List<Integer> robHelper(TreeNode root) {
        if(root == null) {
            return Arrays.asList(0,0);
        }
        List<Integer> leftSubtree = robHelper(root.left);
        List<Integer> rightSubtree = robHelper(root.right);

        // This stores that, Add leftSubtree Max value and right Subtree Max value
        int withoutRoot = Integer.max(leftSubtree.get(0), leftSubtree.get(1)) + Integer.max(rightSubtree.get(0), rightSubtree.get(1));

        // This stores value considering current root plus value from left and right subtrees excluding their root
        int withRoot = root.val + leftSubtree.get(0) + rightSubtree.get(0);
        

        return Arrays.asList(withoutRoot, withRoot);
    }
}


class RobResult {
    // Maximum amount when the root is not robbed
    int withoutRoot;
    // Maximum amount when the root is robbed
    int withRoot;

    RobResult(int withoutRoot, int withRoot) {
        this.withoutRoot = withoutRoot;
        this.withRoot = withRoot;
    }
}
class Solution2 {
    public int rob(TreeNode root) {
        RobResult result = robHelper(root);
        return Integer.max(result.withRoot, result.withoutRoot);
    }

    private RobResult robHelper(TreeNode root) {
        if(root == null) {
            return new RobResult(0, 0);
        }
        RobResult leftSubtree = robHelper(root.left);
        RobResult rightSubtree = robHelper(root.right);

        // This stores that, Add leftSubtree Max value and right Subtree Max value
        int withoutRoot = Integer.max(leftSubtree.withRoot, leftSubtree.withoutRoot) + Integer.max(rightSubtree.withRoot, rightSubtree.withoutRoot);

        // This stores value considering current root plus value from left and right subtrees excluding their root
        int withRoot = root.val + leftSubtree.withoutRoot + rightSubtree.withoutRoot;

        return new RobResult(withoutRoot, withRoot);
    }
}
