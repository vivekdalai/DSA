# Binary Trees / BST Notes

## 06 - Check if Binary Tree is Balanced

**Generated on:** 2026-04-02 00:36:53 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

A binary tree is balanced if for every node:

- `abs(height(left) - height(right)) <= 1`

Return whether the tree is balanced.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Two approaches:

### Brute force
- compute left height
- compute right height
- recursively check subtrees
- this repeats height computation many times

### Optimized
- compute height bottom-up
- if any subtree is unbalanced, return `-1`
- otherwise return its height

The optimized method is the standard interview solution.

------------------------------------------------------------------------

## 🔁 3. Recurrence

For optimized helper:

- if node is `null`, return `0`
- get `leftHeight`
- if `leftHeight == -1`, propagate `-1`
- get `rightHeight`
- if `rightHeight == -1`, propagate `-1`
- if `abs(leftHeight - rightHeight) > 1`, return `-1`
- else return `1 + max(leftHeight, rightHeight)`

------------------------------------------------------------------------

## 💻 4A. Brute Force Java Implementation

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class BalancedBinaryTreeBrute {
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }

        int leftHeight = height(root.left);
        int rightHeight = height(root.right);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }

        return isBalanced(root.left) && isBalanced(root.right);
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }

        return 1 + Math.max(height(node.left), height(node.right));
    }
}
```

Complexity:
- Time: O(n^2) in worst case
- Space: O(h)

------------------------------------------------------------------------

## 💻 4B. Optimized Java Implementation

```java
class BalancedBinaryTreeOptimal {
    public boolean isBalanced(TreeNode root) {
        return checkHeight(root) != -1;
    }

    private int checkHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = checkHeight(node.left);
        if (leftHeight == -1) {
            return -1;
        }

        int rightHeight = checkHeight(node.right);
        if (rightHeight == -1) {
            return -1;
        }

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return 1 + Math.max(leftHeight, rightHeight);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Unbalanced tree:

```text
    1
   /
  2
 /
3
```

At node `3`:
- height = 1

At node `2`:
- left = 1, right = 0
- balanced, height = 2

At node `1`:
- left = 2, right = 0
- difference = 2
- return `-1`

Answer:
- `false`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is a bottom-up tree DP pattern.

Use it when:
- each node depends on left and right subtree information
- unbalanced / invalid state can be propagated upward
- one traversal should both validate and compute

Related problems:
- diameter of binary tree
- max path sum
- validate BST with range
- subtree property checks

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree is balanced
- Single node is balanced
- Checking only the root is not enough
- Brute force often passes small tests but is suboptimal
- `-1` sentinel is a clean way to propagate failure

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Balanced means every node must satisfy height difference <= 1
- Brute force recomputes heights
- Optimal solution computes height and balance together
- Returning `-1` for unbalanced subtree is the key trick

------------------------------------------------------------------------

# End of Notes
