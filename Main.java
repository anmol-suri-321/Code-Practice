import LLD.DesignPatterns.FactoryDesignPattern.OS;
import LLD.DesignPatterns.FactoryDesignPattern.OSFactory;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}


public class Main {
    public static void main(String[] args) {
        PermutationInString obj = new PermutationInString();
        obj.checkInclusion("ab", "eidbaooo");
    }
}