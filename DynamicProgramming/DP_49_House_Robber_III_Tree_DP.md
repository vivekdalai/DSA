# Dynamic Programming Notes

## 49 - House Robber III (LC 337) — Tree DP

**Generated on:** 2026-02-25 23:22:57 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given the root of a binary tree where each node contains some money.  
If two directly-linked houses (parent and child) are both robbed on the same night, the police will be alerted.

Goal:
- Return the maximum amount of money the thief can rob without alerting the police.

Example:
- Input: [3,2,3,null,3,null,1] → Output: 7  
  Explanation: Rob 3 (root) and 3 + 1 in the grandchildren: 3 + 3 + 1 = 7

------------------------------------------------------------------------

## 🪜 2. Key Insight (Tree DP with two states per node)

For each node, we compute two values:
- rob = max money if we rob this node (then we CANNOT rob its children)
- notRob = max money if we do NOT rob this node (then we CAN rob or not rob each child optimally)

Transitions:
- If we rob node u:
  - rob(u) = u.val + notRob(left) + notRob(right)
- If we do not rob node u:
  - notRob(u) = max(rob(left), notRob(left)) + max(rob(right), notRob(right))

Answer at root:
- max(rob(root), notRob(root))

Complexity:
- Time: O(n) (visit each node once)
- Space: O(h) recursion stack (h = tree height), O(1) extra per node

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

public class HouseRobberIII {
    // returns int[]{rob, notRob} for a node
    private int[] dfs(TreeNode node) {
        if (node == null) return new int[]{0, 0};

        int[] L = dfs(node.left);
        int[] R = dfs(node.right);

        int rob = node.val + L[1] + R[1]; // if we rob this, children cannot be robbed
        int notRob = Math.max(L[0], L[1]) + Math.max(R[0], R[1]); // pick best for each child

        return new int[]{rob, notRob};
    }

    public int rob(TreeNode root) {
        int[] res = dfs(root);
        return Math.max(res[0], res[1]);
    }

    // Example usage (simple)
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3,
            new TreeNode(2, null, new TreeNode(3)),
            new TreeNode(3, null, new TreeNode(1))
        );
        System.out.println(new HouseRobberIII().rob(root)); // 7
    }
}
```

Alternative (memoization by node reference)
- A Map<TreeNode, int[]> can memoize the pair per node in languages lacking easy tuple returns, but here a single DFS returning an array is clean and O(n).

------------------------------------------------------------------------

## 🔎 4. Dry Run Sketch

For node 3 (root):
- Left subtree returns [robL, notRobL] for node 2
- Right subtree returns [robR, notRobR] for node 3
- rob(root) = 3 + notRobL + notRobR
- notRob(root) = max(robL, notRobL) + max(robR, notRobR)
Choose the larger at the root.

------------------------------------------------------------------------

## 🏷 5. Pattern Recognition

- Name: Tree DP with “take vs skip” states
- Family: DP on trees (post-order combining children states)
- Triggers:
  - Constraint disallows selecting adjacent (parent-child)
  - Need two values per node representing inclusion vs exclusion

Related:
- Max Weight Independent Set on a tree (generalization)
- Robbing on general graphs becomes NP-hard (tree structure makes it DP-friendly)

------------------------------------------------------------------------

## 🔄 6. Edge Cases and Pitfalls

- Null root → 0
- Single node → that node’s value
- Beware of using global memo keyed by node value only (values not unique) — always key by node reference if memoizing
- Ensure post-order evaluation to have both children states before computing parent

------------------------------------------------------------------------

## ✅ 7. Takeaway

- Post-order DFS that returns {rob, notRob} per node is the cleanest approach.
- Final answer is max at the root between rob and notRob.
- Linear time and space proportional to tree height.

------------------------------------------------------------------------

# End of Notes
