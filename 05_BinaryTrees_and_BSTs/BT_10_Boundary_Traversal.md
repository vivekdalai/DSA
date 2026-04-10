# Binary Trees / BST Notes

## 10 - Boundary Traversal of Binary Tree

**Generated on:** 2026-04-02 00:39:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Return the boundary traversal of a binary tree in anti-clockwise order.

Boundary includes:
1. root
2. left boundary (excluding leaf nodes)
3. all leaf nodes from left to right
4. right boundary in reverse order (excluding leaf nodes)

------------------------------------------------------------------------

## 🪜 2. Core Idea

Break the work into three independent parts:

- left boundary
- leaf nodes
- right boundary

Important:
- do not duplicate leaf nodes
- root should not be repeated
- right boundary must be reversed at the end

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- if root is not a leaf, add it
- walk down left boundary:
  - prefer left child
  - otherwise go right
  - skip leaves
- collect all leaves using DFS
- walk down right boundary:
  - prefer right child
  - otherwise go left
  - skip leaves
- reverse right boundary before adding

------------------------------------------------------------------------

## 💻 4. Java Implementation

This problem is easier if we do not try to build the answer in one complicated traversal.

Instead, split it into clear parts:
- root
- left boundary
- leaf nodes
- right boundary

Each part has a separate role:
- left boundary gives the outer path on the left side
- leaves give the bottom boundary from left to right
- right boundary gives the outer path on the right side, but it must be added in reverse

The main care point is avoiding duplicates:
- leaf nodes should not be added again in left or right boundary
- root should not be repeated

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    TreeNode(int data) {
        this.data = data;
    }
}

class BoundaryTraversal {
    public List<Integer> traverseBoundary(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        if (!isLeaf(root)) {
            result.add(root.data);
        }

        fetchLeftBoundary(root.left, result);
        fetchLeafNodes(root, result);

        List<Integer> rightBoundary = new ArrayList<>();
        fetchRightBoundary(root.right, rightBoundary);
        Collections.reverse(rightBoundary);

        result.addAll(rightBoundary);
        return result;
    }

    private boolean isLeaf(TreeNode node) {
        return node.left == null && node.right == null;
    }

    private void fetchLeftBoundary(TreeNode node, List<Integer> result) {
        while (node != null) {
            if (!isLeaf(node)) {
                result.add(node.data);
            }

            node = (node.left != null) ? node.left : node.right;
        }
    }

    private void fetchLeafNodes(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }

        if (isLeaf(node)) {
            result.add(node.data);
            return;
        }

        fetchLeafNodes(node.left, result);
        fetchLeafNodes(node.right, result);
    }

    private void fetchRightBoundary(TreeNode node, List<Integer> result) {
        while (node != null) {
            if (!isLeaf(node)) {
                result.add(node.data);
            }

            node = (node.right != null) ? node.right : node.left;
        }
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h) recursion stack + right boundary temp list

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Tree:

```text
        1
      /   \
     2     3
    / \     \
   4   5     6
      / \   /
     7   8 9
```

Boundary:
- root = `1`
- left boundary = `2`
- leaves = `4, 7, 8, 9`
- right boundary reversed = `6, 3`

Answer:
- `[1, 2, 4, 7, 8, 9, 6, 3]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- problem explicitly asks for tree boundary
- anti-clockwise perimeter of tree is needed
- left edge, leaves, and right edge must be combined carefully

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Single-node tree -> answer should be just root
- Do not add leaves in left/right boundary functions
- Right boundary must be reversed
- Root should not be duplicated when it is also a leaf

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Boundary traversal = root + left boundary + leaves + reversed right boundary
- Split the problem into helper functions
- Excluding leaves from left/right boundaries avoids duplicates
- Right side must be added in reverse order

------------------------------------------------------------------------

# End of Notes
