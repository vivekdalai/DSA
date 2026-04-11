# Binary Trees / BST Notes

## 05 - Height of Binary Tree

**Generated on:** 2026-04-02 00:36:13 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return its height / maximum depth.

Height means:
- number of nodes in the longest root-to-leaf path

Example:

```text
    1
   / \
  2   3
 /
4
```

Height:
- `3`

------------------------------------------------------------------------

## 🪜 2. Core Idea

Two common approaches:
- DFS recursion
- BFS level order counting

The recursive DFS version is the cleanest:
- height of a node = `1 + max(height(left), height(right))`

------------------------------------------------------------------------

## 🔁 3. Recurrence

Base case:
- `height(null) = 0`

Transition:
- `height(node) = 1 + max(height(node.left), height(node.right))`

------------------------------------------------------------------------

## 💻 4A. Recursive Java Implementation

The recursive solution works bottom-up.

The idea is:
- first find the height of the left subtree
- then find the height of the right subtree
- the current node's height is `1 + max(leftHeight, rightHeight)`

So each node asks its children for their heights, and then builds its own answer from them.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class HeightOfBinaryTreeDFS {
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h), O(n) worst case

------------------------------------------------------------------------

## 💻 4B. Level Order Java Implementation

The BFS approach counts how many levels exist in the tree.

The idea is:
- use a queue for level order traversal
- process all nodes of one level together
- after one full level is processed, increment height by 1

So height becomes the total number of levels we were able to process.

```java
import java.util.LinkedList;
import java.util.Queue;

class HeightOfBinaryTreeBFS {
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int height = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            while (size-- > 0) {
                TreeNode curr = queue.remove();

                if (curr.left != null) {
                    queue.add(curr.left);
                }
                if (curr.right != null) {
                    queue.add(curr.right);
                }
            }

            height++;
        }

        return height;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(w)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Tree:

```text
    1
   / \
  2   3
 /
4
```

DFS:
- height(4) = 1
- height(2) = 1 + max(1, 0) = 2
- height(3) = 1
- height(1) = 1 + max(2, 1) = 3

Answer:
- `3`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This pattern appears in:
- height / depth of tree
- balanced tree checks
- diameter of tree
- maximum path style bottom-up tree DP

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> height `0`
- Single node -> height `1`
- Clarify whether the problem measures height in:
  - nodes
  - edges
- Most interview versions use node count for depth, edge count for diameter

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Height = longest root-to-leaf path
- Recursive formula is `1 + max(left, right)`
- DFS is the cleanest standard solution
- BFS level counting also works

------------------------------------------------------------------------

# End of Notes
