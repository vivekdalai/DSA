# Binary Trees / BST Notes

## 11 - Complete Binary Tree Trim

**Generated on:** 2026-07-25 (IST)

------------------------------------------------------------------------

## 1. Problem Understanding

Given the root of a binary tree, trim it so that the remaining tree satisfies the property of a complete binary tree.

A complete binary tree means:
- every level except possibly the last is completely filled
- the last level is filled from left to right
- after the first missing child in level order, no later node should have any child

When a subtree violates this rule:
- remove that subtree from the original tree
- add all removed node values into a `trashQueue`

Return:
- the modified root
- the list of removed values

------------------------------------------------------------------------

## 2. Core Idea

Use level order traversal because completeness is defined by level-order position.

The key rule:
- once we find the first missing child, all later child positions must also be empty

Maintain a boolean:
- `gapFound = false`

During BFS:
- if a child is missing, set `gapFound = true`
- if `gapFound` is already true and we find a non-null child, that child subtree is invalid
- collect all nodes from that invalid subtree into `trashQueue`
- cut the edge from the current node

------------------------------------------------------------------------

## 3. Step-by-Step Logic

- if root is null, return null root and empty trash list
- put root into a queue
- keep processing nodes in level order
- for each node:
  - check left child first
  - then check right child
- if child exists before any gap:
  - add it to the queue
- if child is missing:
  - mark `gapFound = true`
- if child exists after a gap:
  - collect the entire child subtree into trash
  - disconnect that child pointer

Why left before right matters:
- level order checks positions from left to right
- a right child without a left child is immediately invalid for a complete binary tree

------------------------------------------------------------------------

## 4. Java Implementation

The traversal is normal BFS, but it remembers whether a gap has appeared.

Once a gap appears, every later child slot should be empty. If it is not empty, that whole subtree cannot remain in the complete tree.

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

public class CompleteTreeTrimmer {

    public static class Result {
        public TreeNode root;
        public List<Integer> trashQueue;

        public Result(TreeNode root, List<Integer> trashQueue) {
            this.root = root;
            this.trashQueue = trashQueue;
        }
    }

    public static Result trimToCompleteBinaryTree(TreeNode root) {
        List<Integer> trashQueue = new ArrayList<>();
        if (root == null) {
            return new Result(null, trashQueue);
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        boolean gapFound = false;

        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();

            if (curr.left != null) {
                if (gapFound) {
                    collectAllSubtreeNodes(curr.left, trashQueue);
                    curr.left = null;
                } else {
                    queue.offer(curr.left);
                }
            } else {
                gapFound = true;
            }

            if (curr.right != null) {
                if (gapFound) {
                    collectAllSubtreeNodes(curr.right, trashQueue);
                    curr.right = null;
                } else {
                    queue.offer(curr.right);
                }
            } else {
                gapFound = true;
            }
        }

        return new Result(root, trashQueue);
    }

    private static void collectAllSubtreeNodes(TreeNode node, List<Integer> trash) {
        if (node == null) {
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(node);

        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            trash.add(curr.val);

            if (curr.left != null) {
                queue.offer(curr.left);
            }
            if (curr.right != null) {
                queue.offer(curr.right);
            }
        }
    }
}
```

Complexity:
- Time: O(n)
- Space: O(w)

Where:
- `n` is the number of nodes in the tree
- `w` is the maximum width of the tree, including the helper queue used while collecting trimmed subtrees

------------------------------------------------------------------------

## 5. Dry Run / Example

Tree:

```text
        1
      /   \
     2     3
    /       \
   4         7
```

Level order positions:
- `1` is valid
- `2` and `3` are valid
- `2.left = 4` is valid
- `2.right` is missing, so `gapFound = true`
- after this gap, `3.right = 7` is found

Since `7` appears after a missing child position:
- remove subtree rooted at `7`
- add `7` to `trashQueue`
- set `3.right = null`

Final tree:

```text
        1
      /   \
     2     3
    /
   4
```

Trash:
- `[7]`

------------------------------------------------------------------------

## 6. Pattern Recognition

Use this pattern when:
- the problem talks about complete binary tree validation
- the rule depends on level-order positions
- after the first null child, later non-null children are invalid

Related problems:
- check if a binary tree is complete
- serialize binary tree by level order
- trim or repair a tree based on BFS order

------------------------------------------------------------------------

## 7. Edge Cases and Pitfalls

- Empty tree should return empty trash
- A single-node tree is already complete
- A node with only right child is invalid because left child is missing first
- Once `gapFound` becomes true, it should never become false again
- When trimming a child, collect the whole subtree, not only the direct child
- Do not enqueue a subtree after deciding it must be trimmed

------------------------------------------------------------------------

## 8. Takeaway

- Complete binary tree rules are naturally checked with BFS
- `gapFound` represents the first missing child position
- Any child found after that gap must be removed
- Trimming must disconnect the parent pointer and collect the full removed subtree

------------------------------------------------------------------------

# End of Notes
