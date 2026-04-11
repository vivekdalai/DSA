# Binary Trees / BST Notes

## 08 - Maximum Path Sum in Binary Tree

**Generated on:** 2026-04-02 00:38:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return the maximum path sum.

A path:
- can start and end at any nodes
- must follow parent-child edges
- cannot revisit a node

Important:
- the path does not have to pass through the root
- negative node values are allowed

------------------------------------------------------------------------

## 🪜 2. Core Idea

At each node there are two related quantities:

### Global candidate
Path passing through current node:
- left contribution
- current node
- right contribution

### Return value to parent
A parent can only take one side:
- node + max(left contribution, right contribution)

Negative subtree sums are harmful, so ignore them using:
- `Math.max(0, subtreeSum)`

------------------------------------------------------------------------

## 🔁 3. Recurrence

For each node:
- `left = max(0, solve(node.left))`
- `right = max(0, solve(node.right))`

Update global answer:
- `maxSum = max(maxSum, left + right + node.val)`

Return upward:
- `node.val + max(left, right)`

------------------------------------------------------------------------

## 💻 4. Java Implementation

This problem becomes easier if we separate two different questions at every node.

Question 1:
- what is the best path sum that can be extended upward to the parent?

Question 2:
- what is the best complete path whose highest point is the current node?

These are not the same:
- for the parent, we can return only one side
- for the global answer, we are allowed to use both left and right sides together through the current node

That is why the solution:
- returns one-branch gain upward
- updates the full path answer at every node

Negative contributions are ignored because taking them would only reduce the path sum.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class BinaryTreeMaximumPathSum {
    public int maxPathSum(TreeNode root) {
        int[] maxSum = {Integer.MIN_VALUE};
        dfs(root, maxSum);
        return maxSum[0];
    }

    private int dfs(TreeNode node, int[] maxSum) {
        if (node == null) {
            return 0;
        }

        int leftTreeSum = Math.max(0, dfs(node.left, maxSum));
        int rightTreeSum = Math.max(0, dfs(node.right, maxSum));

        maxSum[0] = Math.max(maxSum[0], leftTreeSum + rightTreeSum + node.val);
        return node.val + Math.max(leftTreeSum, rightTreeSum);
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
      -10
      /  \
     9   20
        /  \
       15   7
```

At node `15`:
- left = 0, right = 0
- candidate = 15
- return = 15

At node `7`:
- candidate = 7
- return = 7

At node `20`:
- left = 15, right = 7
- candidate = 15 + 7 + 20 = 42
- return = 20 + max(15, 7) = 35

At node `-10`:
- left = 9, right = 35
- candidate = 34

Maximum:
- `42`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is a classic tree DP pattern:
- one value returned upward
- one global answer updated at every node

Related problems:
- diameter of binary tree
- longest univalue path
- house robber III
- tree path problems with include/exclude logic

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- All node values may be negative
  - initialize answer with `Integer.MIN_VALUE`
- Do not always include left and right children
  - ignore negative contributions
- Returned value and global candidate are different
- Parent can only take one branch, not both

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Ignore negative subtree sums using `max(0, ...)`
- Global answer checks full path through each node
- Returned value is one-branch gain for parent
- This separation is the key to tree path sum problems

------------------------------------------------------------------------

# End of Notes
