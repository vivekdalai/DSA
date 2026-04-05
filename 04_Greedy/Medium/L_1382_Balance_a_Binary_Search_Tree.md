# Greedy Notes

## Balance a Binary Search Tree

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given the root of a binary search tree, return a balanced BST containing the same node values.

If there is more than one answer, return any of them.

**Example 1**

    Input: root = [1,null,2,null,3,null,4,null,null]
    Output: [2,1,3,null,null,null,4]

**Example 2**

    Input: root = [2,1,3]
    Output: [2,1,3]


<!-- leetcode-images-start -->
**Official LeetCode Images**

![LeetCode image 1](https://assets.leetcode.com/uploads/2021/08/10/balance1-tree.jpg)

![LeetCode image 2](https://assets.leetcode.com/uploads/2021/08/10/balanced2-tree.jpg)

<!-- leetcode-images-end -->
------------------------------------------------------------------------

## 2. Why Inorder Solves Half The Problem

For a BST, inorder traversal returns nodes in sorted order.

So the problem reduces to:

1. extract the sorted order
2. rebuild a balanced BST from the middle outward

That is the full idea in the file.

------------------------------------------------------------------------

## 3. Rebuild Logic

Given a sorted list:

- choose the middle node as root
- recursively build the left subtree from the left half
- recursively build the right subtree from the right half

Choosing the middle keeps heights close on both sides, which makes the tree balanced.

------------------------------------------------------------------------

## 4. Walkthrough

Suppose inorder gives:

    [1, 2, 3, 4, 5]

Build:

- root = `3`
- left half `[1,2]` -> root `2`
- right half `[4,5]` -> root `5` or `4`, depending on midpoint choice

The resulting tree height becomes close to logarithmic instead of skewed.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
static List<TreeNode> sortedList = new ArrayList<>();

public static TreeNode balanceBST(TreeNode root) {
    inorder(root, sortedList);
    return build(0, sortedList.size() - 1);
}

private static TreeNode build(int l, int r) {
    if (l > r) return null;

    int mid = l + (r - l) / 2;
    TreeNode root = sortedList.get(mid);
    root.left = build(l, mid - 1);
    root.right = build(mid + 1, r);
    return root;
}

private static void inorder(TreeNode root, List<TreeNode> sortedList) {
    if (root == null) return;
    inorder(root.left, sortedList);
    sortedList.add(root);
    inorder(root.right, sortedList);
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(n)`

Pattern:

- inorder to sorted sequence
- divide and conquer on the middle element

------------------------------------------------------------------------

## End of Notes
