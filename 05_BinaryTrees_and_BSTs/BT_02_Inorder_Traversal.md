# Binary Trees / BST Notes

## 02 - Inorder Traversal

**Generated on:** 2026-04-02 00:34:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return its inorder traversal.

Inorder order:
- Left
- Root
- Right

Example:

```text
    1
   / \
  2   3
 / \
4   5
```

Inorder:
- `[4, 2, 5, 1, 3]`

For BSTs, inorder traversal gives nodes in sorted order.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Inorder means:
- fully process left subtree
- then current node
- then right subtree

Common implementations:
- recursive DFS
- iterative stack
- Morris traversal for O(1) extra space

------------------------------------------------------------------------

## 🔁 3. Traversal Logic

### Recursive
- recurse left
- add current node
- recurse right

### Morris Inorder
If `left == null`:
- add current node
- move right

Else:
- find rightmost node of left subtree
- if thread not created:
  - create thread to current node
  - move left
- else:
  - remove thread
  - add current node
  - move right

------------------------------------------------------------------------

## 💻 4A. Recursive Java Implementation

```java
import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class InorderTraversalRecursive {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        dfs(root, ans);
        return ans;
    }

    private void dfs(TreeNode node, List<Integer> ans) {
        if (node == null) {
            return;
        }

        dfs(node.left, ans);
        ans.add(node.val);
        dfs(node.right, ans);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h), O(n) worst case

------------------------------------------------------------------------

## 💻 4B. Morris Inorder Traversal

```java
import java.util.ArrayList;
import java.util.List;

class InorderTraversalMorris {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                ans.add(curr.val);
                curr = curr.right;
            } else {
                TreeNode rightLeaf = curr.left;

                while (rightLeaf.right != null && rightLeaf.right != curr) {
                    rightLeaf = rightLeaf.right;
                }

                if (rightLeaf.right == null) {
                    rightLeaf.right = curr;
                    curr = curr.left;
                } else {
                    rightLeaf.right = null;
                    ans.add(curr.val);
                    curr = curr.right;
                }
            }
        }

        return ans;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1) extra

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

Inorder:
- left of `1` -> inorder of `2`
- left of `2` -> `4`
- visit `2`
- visit `5`
- visit `1`
- visit `3`

Answer:
- `[4, 2, 5, 1, 3]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use inorder when:
- BST sorted order is useful
- validating BST property
- recovering BST
- finding kth smallest in BST

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> empty list
- In Morris traversal, add node on second visit when left child exists
- Always remove the temporary thread
- Do not confuse preorder Morris with inorder Morris timing of `add`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Inorder = Left Root Right
- For BST, inorder is sorted
- Recursive solution is simplest
- Morris traversal gives O(1) extra space

------------------------------------------------------------------------

# End of Notes
