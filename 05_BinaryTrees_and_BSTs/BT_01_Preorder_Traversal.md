# Binary Trees / BST Notes

## 01 - Preorder Traversal

**Generated on:** 2026-04-02 00:33:18 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return its preorder traversal.

Preorder order:
- Root
- Left
- Right

Example:

```text
    1
   / \
  2   3
 / \
4   5
```

Preorder:
- `[1, 2, 4, 5, 3]`

------------------------------------------------------------------------

## 🪜 2. Core Idea

Preorder means:
- process current node first
- then go to left subtree
- then go to right subtree

Common implementations:
- recursive DFS
- iterative using stack
- Morris traversal for O(1) extra space

------------------------------------------------------------------------

## 🔁 3. Traversal Logic

### Recursive
- if node is `null`, return
- add current node
- recurse left
- recurse right

### Iterative
- use a stack
- push root
- pop node, add it to answer
- push right child first
- push left child next

Why push right first:
- stack is LIFO
- left should be processed before right

------------------------------------------------------------------------

## 💻 4A. Recursive Java Implementation

The recursive approach is the most natural for preorder traversal.

The idea is:
- visit the current node immediately
- then solve the same problem for the left subtree
- then solve it for the right subtree

This matches preorder exactly because recursion lets us process the root first, then fully explore left, then right.

```java
import java.util.ArrayList;

class BinaryTreeNode<T> {
    T data;
    BinaryTreeNode<T> left;
    BinaryTreeNode<T> right;

    BinaryTreeNode(T data) {
        this.data = data;
    }
}

class PreorderTraversalRecursive {
    public ArrayList<Integer> preorderTraversal(BinaryTreeNode<Integer> root) {
        ArrayList<Integer> list = new ArrayList<>();
        dfs(root, list);
        return list;
    }

    private void dfs(BinaryTreeNode<Integer> root, ArrayList<Integer> list) {
        if (root == null) {
            return;
        }

        list.add(root.data);
        dfs(root.left, list);
        dfs(root.right, list);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h) recursion stack, O(n) worst case

------------------------------------------------------------------------

## 💻 4B. Iterative Java Implementation

The iterative version uses a stack to simulate recursion.

The idea is:
- pop the current node and visit it
- then push its children so they can be processed later

Since stack is LIFO, we push the **right child first** and the **left child second**.
This ensures the left child comes out first on the next step, which preserves Root -> Left -> Right order.

```java
import java.util.ArrayList;
import java.util.Stack;

class PreorderTraversalIterative {
    public ArrayList<Integer> preorderTraversal(BinaryTreeNode<Integer> root) {
        ArrayList<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Stack<BinaryTreeNode<Integer>> st = new Stack<>();
        st.push(root);

        while (!st.isEmpty()) {
            BinaryTreeNode<Integer> node = st.pop();
            list.add(node.data);

            if (node.right != null) {
                st.push(node.right);
            }
            if (node.left != null) {
                st.push(node.left);
            }
        }

        return list;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h), O(n) worst case

------------------------------------------------------------------------

## 💻 4C. Morris Preorder Traversal

Morris preorder removes the need for recursion or an explicit stack by creating temporary threads in the tree.

The key idea is:
- if there is no left child, visit the current node and move right
- if there is a left child, find the inorder predecessor in the left subtree
- on the first visit, record the current node, create a temporary thread, and move left
- on the second visit, remove the thread and move right

The important preorder difference is:
- we visit the node the **first time** we encounter it, because preorder wants Root first

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

class PreorderTraversalMorris {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                ans.add(curr.val);
                curr = curr.right;
            } else {
                TreeNode predecessor = curr.left;

                while (predecessor.right != null && predecessor.right != curr) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    ans.add(curr.val);
                    predecessor.right = curr;
                    curr = curr.left;
                } else {
                    predecessor.right = null;
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

Recursive flow:
- visit `1`
- go left -> visit `2`
- go left -> visit `4`
- backtrack
- visit `5`
- backtrack
- go right -> visit `3`

Answer:
- `[1, 2, 4, 5, 3]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use preorder when:
- you need root-first processing
- tree cloning / serialization starts from root
- expression/tree reconstruction uses root-first order
- you want to create a copy preserving parent-before-children logic

Related traversals:
- inorder = Left Root Right
- postorder = Left Right Root
- level order = BFS

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> return empty list
- In iterative version, push right before left
- In Morris traversal, always remove the temporary thread
- Do not forget null checks before stack push

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Preorder = Root Left Right
- Recursive solution is the most direct
- Iterative solution uses stack with right pushed before left
- Morris traversal gives O(1) extra space

------------------------------------------------------------------------

# End of Notes
