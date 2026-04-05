package leetcode;

import java.util.ArrayList;
import java.util.List;

public class L_1382_Balance_a_Binary_Search_Tree {
    static List<TreeNode> sortedList = new ArrayList<>();
    public static TreeNode balanceBST(TreeNode root) {
        inorder(root, sortedList);
        return dfs(0, sortedList.size() - 1);
    }

    
    private static TreeNode dfs(int l, int r) {
        if (l > r) return null;
        int mid = l - (l - r) / 2;
        TreeNode root = sortedList.get(mid);
        root.left = dfs(l, mid - 1);
        root.right = dfs(mid + 1, r);
        return root;
    }

    private static void inorder(TreeNode root, List<TreeNode> sortedList) {
        if (root == null) return;
        inorder(root.left, sortedList);
        sortedList.add(root);
        inorder(root.right, sortedList);
    }


}
