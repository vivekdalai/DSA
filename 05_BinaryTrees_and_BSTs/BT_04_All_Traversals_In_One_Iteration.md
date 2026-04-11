# Binary Trees / BST Notes

## 04 - Preorder, Inorder, and Postorder in One Iteration

**Generated on:** 2026-04-02 00:35:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return:
- preorder traversal
- inorder traversal
- postorder traversal

The goal is to compute all three in a single iterative pass.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use a stack of pairs:
- node
- visit count / state

Interpretation:
- state `1` -> preorder time
- state `2` -> inorder time
- state `3` -> postorder time

This mirrors how recursion revisits a node multiple times conceptually.

------------------------------------------------------------------------

## 🔁 3. State Transition

For a node:
- on state `1`
  - add to preorder
  - push same node back with state `2`
  - push left child with state `1`
- on state `2`
  - add to inorder
  - push same node back with state `3`
  - push right child with state `1`
- on state `3`
  - add to postorder

------------------------------------------------------------------------

## 💻 4. Java Implementation

Before the code, think of recursion as visiting the same node in three phases:
- first time we reach the node -> preorder position
- after finishing left subtree -> inorder position
- after finishing right subtree -> postorder position

The iterative solution simulates these three phases explicitly using a stack.

For each stack entry, we store:
- the node
- the current state telling us which phase we are in

So instead of relying on the system call stack, we manually remember:
- which node we are processing
- whether it is time for preorder, inorder, or postorder work

That is why the same node may be pushed back multiple times with different states.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class MyPair {
    TreeNode node;
    int state;

    MyPair(TreeNode node, int state) {
        this.node = node;
        this.state = state;
    }
}

class AllTraversalsOneIteration {
    public List<List<Integer>> treeTraversal(TreeNode root) {
        List<Integer> preorder = new ArrayList<>();
        List<Integer> inorder = new ArrayList<>();
        List<Integer> postorder = new ArrayList<>();

        if (root == null) {
            return Arrays.asList(preorder, inorder, postorder);
        }

        Stack<MyPair> st = new Stack<>();
        st.push(new MyPair(root, 1));

        while (!st.isEmpty()) {
            MyPair curr = st.pop();

            if (curr.state == 1) {
                preorder.add(curr.node.val);
                curr.state = 2;
                st.push(curr);

                if (curr.node.left != null) {
                    st.push(new MyPair(curr.node.left, 1));
                }
            } else if (curr.state == 2) {
                inorder.add(curr.node.val);
                curr.state = 3;
                st.push(curr);

                if (curr.node.right != null) {
                    st.push(new MyPair(curr.node.right, 1));
                }
            } else {
                postorder.add(curr.node.val);
            }
        }

        return Arrays.asList(preorder, inorder, postorder);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h), O(n) worst case

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Tree:

```text
    1
   / \
  2   3
```

Flow:
- state 1 of `1` -> preorder add `1`
- state 1 of `2` -> preorder add `2`
- state 2 of `2` -> inorder add `2`
- state 3 of `2` -> postorder add `2`
- state 2 of `1` -> inorder add `1`
- state 1 of `3` -> preorder add `3`
- state 2 of `3` -> inorder add `3`
- state 3 of `3` -> postorder add `3`
- state 3 of `1` -> postorder add `1`

Results:
- preorder = `[1, 2, 3]`
- inorder = `[2, 1, 3]`
- postorder = `[2, 3, 1]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- interviewer asks for all traversals together
- you want to simulate recursion explicitly
- stack state machine techniques are useful

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> return three empty lists
- State meaning must be consistent
- Push current node back before pushing child
- Left child is processed before inorder state
- Right child is processed before postorder state

------------------------------------------------------------------------

## ✅ 8. Takeaway

- One stack + node state can simulate recursive traversal phases
- state 1 = preorder
- state 2 = inorder
- state 3 = postorder
- Clean way to get all three traversals in one pass

------------------------------------------------------------------------

# End of Notes
