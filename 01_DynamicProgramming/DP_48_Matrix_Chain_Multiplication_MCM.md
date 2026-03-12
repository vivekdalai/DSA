# Dynamic Programming Notes

## 48 - Matrix Chain Multiplication (MCM) — Min Multiplications + Parenthesization

**Generated on:** 2026-02-25 23:21:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a chain of matrices A1, A2, ..., An such that:
- Matrix Ai has dimensions p[i-1] x p[i] (so there are n = p.length - 1 matrices)

Goal:
- Determine the most efficient way (parenthesization) to multiply the chain
- Return the minimum number of scalar multiplications required
- Optionally reconstruct the optimal parenthesization

Example:
- p = [10, 20, 30, 40, 30] (matrices: 10x20, 20x30, 30x40, 40x30)
- Min multiplications = 30000 (e.g., ((A1 (A2 A3)) A4) or similar)

Why DP:
- The choice of “last multiplication split” between subchains leads to overlapping subproblems.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- dp[i][j] = minimum scalar multiplications to multiply matrices Ai..Aj (1-based on matrices)
- Dimensions for Ai..Ak..Aj:
  - Left chain (Ai..Ak): dimensions p[i-1] x p[k]
  - Right chain (Ak+1..Aj): dimensions p[k] x p[j]
  - Cost to combine = p[i-1] * p[k] * p[j]

Recurrence:
- dp[i][i] = 0
- dp[i][j] = min over k in [i..j-1]:
  - dp[i][k] + dp[k+1][j] + p[i-1] * p[k] * p[j]

Answer:
- dp[1][n] where n = p.length - 1

Complexity:
- Time: O(n^3)
- Space: O(n^2)

------------------------------------------------------------------------

## 💻 3A. Bottom-Up Tabulation (Min Multiplications)

```java
import java.util.*;

public class MatrixChainMultiplication {
    // p: dimensions array of length n+1, Ai is p[i-1] x p[i]
    public int minMultiplications(int[] p) {
        int n = p.length - 1; // number of matrices
        int[][] dp = new int[n + 1][n + 1];

        // chain length len from 2 to n
        for (int len = 2; len <= n; len++) {
            for (int i = 1; i + len - 1 <= n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE / 4;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                    }
                }
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        MatrixChainMultiplication mcm = new MatrixChainMultiplication();
        System.out.println(mcm.minMultiplications(new int[]{10, 20, 30, 40, 30})); // 30000
    }
}
```

------------------------------------------------------------------------

## 💻 3B. Reconstruct Optimal Parenthesization

We can store the split point `k` that achieves the minimum to later print the parenthesization.

```java
import java.util.*;

class MatrixChainMultiplicationPrint {
    public static class Result {
        int minCost;
        String parenthesization;
        Result(int c, String s) { minCost = c; parenthesization = s; }
    }

    public Result optimalOrder(int[] p) {
        int n = p.length - 1;
        int[][] dp = new int[n + 1][n + 1];
        int[][] split = new int[n + 1][n + 1]; // store optimal k

        for (int len = 2; len <= n; len++) {
            for (int i = 1; i + len - 1 <= n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE / 4;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        split[i][j] = k;
                    }
                }
            }
        }
        return new Result(dp[1][n], build(1, n, split));
    }

    private String build(int i, int j, int[][] split) {
        if (i == j) return "A" + i;
        int k = split[i][j];
        String left = build(i, k, split);
        String right = build(k + 1, j, split);
        return "(" + left + " x " + right + ")";
    }

    public static void main(String[] args) {
        MatrixChainMultiplicationPrint solver = new MatrixChainMultiplicationPrint();
        Result r = solver.optimalOrder(new int[]{10, 20, 30, 40, 30});
        System.out.println(r.minCost);           // 30000
        System.out.println(r.parenthesization);  // e.g., ((A1 x (A2 x A3)) x A4)
    }
}
```

------------------------------------------------------------------------

## 💡 4. Top-Down (Memoized) Alternative

```java
import java.util.*;

class MatrixChainMultiplicationMemo {
    private Integer[][] memo;
    private int[] p;

    public int minMultiplications(int[] p) {
        int n = p.length - 1;
        this.p = p;
        memo = new Integer[n + 1][n + 1];
        return solve(1, n);
    }

    private int solve(int i, int j) {
        if (i == j) return 0;
        if (memo[i][j] != null) return memo[i][j];

        int best = Integer.MAX_VALUE / 4;
        for (int k = i; k < j; k++) {
            int cost = solve(i, k) + solve(k + 1, j) + p[i - 1] * p[k] * p[j];
            best = Math.min(best, cost);
        }
        return memo[i][j] = best;
    }
}
```

------------------------------------------------------------------------

## 🔎 5. Dry Run Sketch

p = [10, 20, 30, 40, 30], n=4
- dp[i][i] = 0
- len=2:
  - dp[1][2] = 10*20*30 = 6000
  - dp[2][3] = 20*30*40 = 24000
  - dp[3][4] = 30*40*30 = 36000
- len=3, len=4: fill using splits; final dp[1][4] = 30000

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Matrix Chain Multiplication (MCM)
- Family: Interval DP (choose last split point)
- Triggers:
  - Parenthesization/partitioning of a linear chain
  - Cost of combining depends on boundaries and split

Related interval DP problems:
- Burst Balloons (choose last balloon)
- Boolean Parenthesization (ways to evaluate true/false)
- Palindrome Partitioning (min cuts)
- Optimal BST (expected cost; advanced)

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- n=1 (single matrix) → cost 0
- Large dimension products may exceed int if values are big; in Java int is often safe for typical constraints, but consider long if needed
- Ensure indices align: Ai is p[i-1] x p[i] (1-based matrices)

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Classic O(n^3) interval DP with simple recurrence
- For parenthesization, store split points and backtrack
- Template carries over to many “choose split/last” interval problems

------------------------------------------------------------------------

# End of Notes
