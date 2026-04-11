import java.util.LinkedList;
import java.util.Queue;

public class BT_01_CreateBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Builds a binary tree from a level-order int[] representation.
     *
     * Convention:
     * - arr[i] = -1 means null / no node
     *
     * Example:
     * arr = {1, 2, 3, 4, -1, 5, 6}
     *
     * Tree:
     *         1
     *       /   \
     *      2     3
     *     /     / \
     *    4     5   6
     */
    public static TreeNode createBinaryTree(int[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == -1) {
            return null;
        }

        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int i = 1;

        while (!queue.isEmpty() && i < arr.length) {
            TreeNode current = queue.remove();

            // Left child
            if (i < arr.length && arr[i] != -1) {
                current.left = new TreeNode(arr[i]);
                queue.add(current.left);
            }
            i++;

            // Right child
            if (i < arr.length && arr[i] != -1) {
                current.right = new TreeNode(arr[i]);
                queue.add(current.right);
            }
            i++;
        }

        return root;
    }

    public static void printLevelOrder(TreeNode root) {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();

            while (size-- > 0) {
                TreeNode node = queue.remove();
                System.out.print(node.val + " ");

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, -1, 5, 6};

        TreeNode root = createBinaryTree(arr);
        printLevelOrder(root);
    }
}
