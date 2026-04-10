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

### Iterative Stack
- keep moving left and push nodes into stack
- when left becomes null, pop one node
- add that node
- move to its right subtree
- repeat until both current node is null and stack is empty

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

## 💻 4B. Iterative Stack Java Implementation

Before looking at Morris traversal, it helps to first understand the iterative stack approach.

In recursion, the call stack automatically remembers:
- which node we came from
- which nodes are waiting to be processed after their left subtree is done

The iterative approach does the same thing explicitly using our own stack.

The idea is:
- keep going left as much as possible because inorder wants left subtree first
- while going left, push nodes into the stack because we must come back to them later
- once we cannot go left anymore, pop the top node from the stack
- that popped node is now ready to be visited
- after visiting it, move to its right subtree and repeat the same process

Why the stack is needed:
- when we move from a node to its left child, we are postponing that node's visit
- the stack stores all such postponed nodes in the exact order in which we need to return to them

This gives the same Left -> Root -> Right order as recursion, just without using recursive function calls.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class InorderTraversalIterative {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            curr = stack.pop();
            ans.add(curr.val);
            curr = curr.right;
        }

        return ans;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h), O(n) worst case

------------------------------------------------------------------------

## 💻 4C. Morris Inorder Traversal

Before looking at the code, the main idea is to simulate the return path of recursion without using a stack.

In normal inorder traversal, after finishing the left subtree, we need a way to come back to the current node. Recursive traversal gets this "come back" behavior from the call stack. Morris traversal creates that return path temporarily inside the tree itself.

For any current node:
- if there is no left child, then nothing is pending on the left, so we can directly visit the current node and move to the right
- if there is a left child, then inorder says we must finish the entire left subtree first

So we go to the current node's left subtree and find its rightmost node. This node is the inorder predecessor of the current node, meaning it is the last node that will be visited before the current node in inorder traversal.

Now there are two possibilities:
- if that predecessor's right pointer is `null`, we make it point to the current node  
  - this temporary link is called a **thread**
  - it acts like: "after finishing the left subtree, return back to this current node"
  - then we move left
- if that predecessor's right pointer already points to the current node, it means the left subtree has already been processed and we have come back through the thread  
  - now we remove the thread to restore the original tree
  - visit the current node
  - move to the right subtree

Why this works:
- every temporary thread is created exactly once and removed exactly once
- each node is still processed in Left -> Root -> Right order
- we do not need recursion or an explicit stack
- the tree is restored fully at the end

The key intuition is:
- **first time** you reach a node with a left child -> create a way to come back and go left
- **second time** you reach that same node -> left work is done, so visit it and go right

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

## 🔎 6. Dry Run / Example

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

## 🏷 7. Pattern Recognition

Use inorder when:
- BST sorted order is useful
- validating BST property
- recovering BST
- finding kth smallest in BST

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Empty tree -> empty list
- In Morris traversal, add node on second visit when left child exists
- Always remove the temporary thread
- Do not confuse preorder Morris with inorder Morris timing of `add`

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Inorder = Left Root Right
- For BST, inorder is sorted
- Recursive solution is simplest
- Morris traversal gives O(1) extra space

------------------------------------------------------------------------

# End of Notes
