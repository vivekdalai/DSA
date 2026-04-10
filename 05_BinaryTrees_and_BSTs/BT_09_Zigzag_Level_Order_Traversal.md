# Binary Trees / BST Notes

## 09 - Zigzag Level Order Traversal

**Generated on:** 2026-04-02 00:38:31 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return the zigzag level order traversal.

Zigzag means:
- level 0: left to right
- level 1: right to left
- level 2: left to right
- and so on

Example:

```text
    3
   / \
  9  20
    /  \
   15   7
```

Answer:
- `[[3], [20, 9], [15, 7]]`

------------------------------------------------------------------------

## 🪜 2. Core Idea

This is level order traversal with one extra rule:
- alternate insertion direction at each level

Maintain:
- queue for BFS
- boolean `leftToRight`

For each level:
- if `leftToRight`, append normally
- otherwise insert at front

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- do normal BFS level by level
- for each popped node:
  - if current direction is left-to-right, add at end
  - else add at front
- after each level, flip direction

------------------------------------------------------------------------

## 💻 4. Java Implementation

This is almost the same as normal level order traversal.

The only extra idea is:
- for one level, store values from left to right
- for the next level, store values from right to left

The traversal itself does not change:
- we still use BFS
- we still push children in normal left-then-right order

Only the way we place values into the current level list changes.
That is why a `LinkedList` is convenient:
- `addLast()` for left-to-right
- `addFirst()` for right-to-left

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

class ZigzagLevelOrderTraversal {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        boolean leftToRight = true;

        while (!queue.isEmpty()) {
            LinkedList<Integer> levelList = new LinkedList<>();
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.remove();

                if (leftToRight) {
                    levelList.addLast(node.val);
                } else {
                    levelList.addFirst(node.val);
                }

                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }

            result.add(levelList);
            leftToRight = !leftToRight;
        }

        return result;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(w)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Tree:

```text
    1
   / \
  2   3
 / \ / \
4  5 6  7
```

Levels:
- level 0, left-to-right -> `[1]`
- level 1, right-to-left -> `[3, 2]`
- level 2, left-to-right -> `[4, 5, 6, 7]`

Answer:
- `[[1], [3, 2], [4, 5, 6, 7]]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is BFS + direction toggle.

Related problems:
- level order traversal
- right view / left view
- vertical order traversal
- bottom-up level order

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty tree -> empty answer
- Direction must flip after every level, not every node
- `LinkedList<Integer>` is useful because it supports `addFirst` and `addLast`
- Still use normal child enqueue order: left first, then right

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Zigzag is just level order with alternating insertion direction
- Queue handles levels
- `leftToRight` controls front/end insertion
- `LinkedList` makes reversal per level efficient

------------------------------------------------------------------------

# End of Notes
