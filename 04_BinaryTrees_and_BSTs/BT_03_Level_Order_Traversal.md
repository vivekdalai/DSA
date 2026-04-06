# Binary Trees / BST Notes

## 03 - Level Order Traversal

**Generated on:** 2026-04-02 00:35:01 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return the level order traversal of its nodes.

Level order means:
- visit nodes level by level from top to bottom
- within each level, visit from left to right

Example:

```text
    3
   / \
  9  20
    /  \
   15   7
```

Answer:
- `[[3], [9, 20], [15, 7]]`

------------------------------------------------------------------------

## 🪜 2. Core Idea

This is BFS on a tree.

Two common ways:
- iterative using a queue
- recursive by passing current level index

The iterative queue-based version is the standard approach.

------------------------------------------------------------------------

## 🔁 3. Traversal Logic

### Iterative BFS
- push root into queue
- for each level:
  - note current queue size
  - pop exactly that many nodes
  - collect their values
  - push their children

### Recursive
- recurse with `level`
- if the answer list does not yet have this level, create a new inner list
- add current node to `ans.get(level)`

------------------------------------------------------------------------

## 💻 4A. Iterative Java Implementation

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

class LevelOrderTraversalIterative {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelCount = queue.size();
            List<Integer> levelNodes = new ArrayList<>();

            while (levelCount != 0) {
                TreeNode curr = queue.remove();
                levelNodes.add(curr.val);

                if (curr.left != null) {
                    queue.add(curr.left);
                }
                if (curr.right != null) {
                    queue.add(curr.right);
                }

                levelCount--;
            }

            ans.add(levelNodes);
        }

        return ans;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(w), where `w` is maximum width of tree

------------------------------------------------------------------------

## 💻 4B. Recursive Java Implementation

```java
import java.util.ArrayList;
import java.util.List;

class LevelOrderTraversalRecursive {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        traverseTree(root, 0, ans);
        return ans;
    }

    private void traverseTree(TreeNode node, int level, List<List<Integer>> ans) {
        if (node == null) {
            return;
        }

        if (ans.size() <= level) {
            ans.add(new ArrayList<>());
        }

        ans.get(level).add(node.val);
        traverseTree(node.left, level + 1, ans);
        traverseTree(node.right, level + 1, ans);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(h) recursion stack + output

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Tree:

```text
    3
   / \
  9  20
    /  \
   15   7
```

Queue flow:
- start: `[3]`
- level 0 -> collect `[3]`, push `9, 20`
- level 1 -> collect `[9, 20]`, push `15, 7`
- level 2 -> collect `[15, 7]`

Answer:
- `[[3], [9, 20], [15, 7]]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use level order when:
- the question asks level-wise traversal
- shortest path in unweighted tree is involved
- BFS / queue processing is natural
- zigzag / right view / left view starts from level logic

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> return empty list
- Queue size must be captured before processing a level
- Do not mix nodes from the next level into the current level list
- Recursive version needs `ans.size() <= level` check

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Level order = BFS on tree
- Queue-based approach is the cleanest
- Process one queue-size batch per level
- Recursive level-index solution is also useful

------------------------------------------------------------------------

# End of Notes
