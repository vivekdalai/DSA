# Dynamic Programming Notes

## 51 - Binary Tree Maximum Path Sum (LC 124) — Tree DP with Global Max

**Generated on:** 2026-02-25 23:26:40 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the root of a binary tree, return the maximum path sum.  
A path is any sequence of nodes where each pair of adjacent nodes has an edge connecting them.  
A path does not need to pass through the root, and it must contain at least one node.

Nodes may contain negative values.

Example:
- Input: [-10,9,20,null,null,15,7] → Output: 42  
  Explanation: Path 15 → 20 → 7

------------------------------------------------------------------------

## 🪜 2. Key Insight (Post-order DP + Global Answer)

For each node, compute:
- “Max gain up” = maximum sum we can contribute to our parent from a path ending at this node and going up (choose best child or none if negative).

Let:
- leftGain = max(0, gain from left subtree)
- rightGain = max(0, gain from right subtree)

Then:
- Path “through” current node as peak: node.val + leftGain + rightGain
  - Update globalMax with this value (since a max path may pass through node taking both sides)
- Return to parent: node.val + max(leftGain, rightGain) (we can only extend one side upwards)

This is a classic tree DP where each node returns a single-branch contribution and also updates a global two-branch candidate.

Complexity:
- Time: O(n), visit each node once
- Space: O(h) recursion stack (h = height)

------------------------------------------------------------------------

## 💻 3. Clean Java Implementation

```java
// Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val; this.left = left; this.right = right;
    }
}

public class BinaryTreeMaxPathSum {
    private int globalMax;

    public int maxPathSum(TreeNode root) {
        globalMax = Integer.MIN_VALUE;
        dfs(root);
        return globalMax;
    }

    // returns max gain to parent from this node (single path upward)
    private int dfs(TreeNode node) {
        if (node == null) return 0;

        int leftGain = Math.max(0, dfs(node.left));
        int rightGain = Math.max(0, dfs(node.right));

        // best path "through" this node (as a peak) using both sides
        int through = node.val + leftGain + rightGain;
        globalMax = Math.max(globalMax, through);

        // return single-branch gain to parent
        return node.val + Math.max(leftGain, rightGain);
    }

    // Example
    public static void main(String[] args) {
        TreeNode root = new TreeNode(-10,
            new TreeNode(9),
            new TreeNode(20, new TreeNode(15), new TreeNode(7))
        );
        System.out.println(new BinaryTreeMaxPathSum().maxPathSum(root)); // 42
    }
}
```

------------------------------------------------------------------------

## 🔎 4. Dry Run Sketch

Tree:
-10
/   \
9    20
    /  \
   15   7

- For 15/7: leftGain=rightGain=0, through=15/7 → globalMax updated
- For 20: leftGain=15, rightGain=7, through=20+15+7=42 → globalMax=42; return up = 20+max(15,7)=35
- For -10: leftGain = max(0, gain(9)) = 9, rightGain = max(0, 35)=35
  - through = -10 + 9 + 35 = 34 (global remains 42)
  - return up = -10 + max(9,35) = 25
Answer = 42.

------------------------------------------------------------------------

## 🏷 5. Pattern Recognition

- Name: Binary Tree Max Path Sum
- Family: Tree DP with “choose max child gains” and a global peak candidate
- Triggers:
  - Paths can start/end anywhere; allow combining both children at a node
  - Need both a return value (single branch) and a global aggregator (two-branch)

Related:
- Diameter of a Binary Tree (count edges/nodes; very similar shape)
- Max sum path in DAGs (generalization)
- House Robber III (tree DP with two states per node; related structure)

------------------------------------------------------------------------

## 🔄 6. Edge Cases and Pitfalls

- All negative nodes: globalMax should track the maximum node value (handled by initializing to -INF and allowing node.val alone as through)
- Do not allow negative child gains to reduce a path: clamp with max(0, gain)
- Beware integer overflow for very large sums; Java int is typically fine for LC constraints

------------------------------------------------------------------------

## ✅ 7. Takeaway

- Post-order compute left/right gains clamped at zero.
- Update a global maximum using a “through node” candidate with both sides.
- Return single-branch gain to parent to keep the path simple upward.

------------------------------------------------------------------------

# End of Notes
