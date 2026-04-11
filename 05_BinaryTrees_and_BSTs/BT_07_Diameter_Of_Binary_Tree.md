# Binary Trees / BST Notes

## 07 - Diameter of Binary Tree

**Generated on:** 2026-04-02 00:37:19 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

The diameter of a binary tree is the length of the longest path between any two nodes.

In the common LeetCode version:
- diameter is measured in number of **edges**

Important:
- the longest path does not have to pass through the root

------------------------------------------------------------------------

## 🪜 2. Core Idea

At each node:
- compute height of left subtree
- compute height of right subtree
- path passing through current node has length:
  - `leftHeight + rightHeight` edges

Track a global maximum while returning subtree heights upward.

------------------------------------------------------------------------

## 🔁 3. Recurrence

Base case:
- `height(null) = 0`

For each node:
- `leftHeight = height(node.left)`
- `rightHeight = height(node.right)`
- update diameter:
  - `diameter = max(diameter, leftHeight + rightHeight)`
- return:
  - `1 + max(leftHeight, rightHeight)`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class DiameterOfBinaryTree {
    public int diameterOfBinaryTree(TreeNode root) {
        int[] result = new int[1];
        height(root, result);
        return result[0];
    }

    private int height(TreeNode node, int[] result) {
        if (node == null) {
            return 0;
        }

        int leftHeight = height(node.left, result);
        int rightHeight = height(node.right, result);

        result[0] = Math.max(result[0], leftHeight + rightHeight);
        return 1 + Math.max(leftHeight, rightHeight);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Tree:

```text
      1
     / \
    2   3
   / \
  4   5
```

At node `4`:
- height = 1

At node `5`:
- height = 1

At node `2`:
- left = 1, right = 1
- diameter candidate = 2
- height = 2

At node `3`:
- height = 1

At node `1`:
- left = 2, right = 1
- diameter candidate = 3

Answer:
- `3` edges

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is a bottom-up tree DP problem.

Pattern:
- return one value upward (height)
- update another answer globally (diameter)

Related problems:
- balanced binary tree
- maximum path sum
- longest univalue path

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> diameter `0`
- Single node -> diameter `0` edges
- Diameter is not always through root
- Clarify whether answer is in:
  - edges
  - nodes
- Here the formula `leftHeight + rightHeight` gives edges

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Diameter = longest path between any two nodes
- At each node, check path through that node
- Return height upward
- Maintain global maximum of `leftHeight + rightHeight`

------------------------------------------------------------------------

# End of Notes
