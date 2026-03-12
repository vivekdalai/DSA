# Dynamic Programming Notes

## 05 - Climbing Stairs (Count Ways) — Fibonacci DP

**Generated on:** 2026-02-24 19:07:46 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are climbing a staircase with `n` steps. Each time you can climb either 1 or 2 steps.  
How many distinct ways are there to reach the top?

Observation:
- The number of ways to reach step `i` = ways to reach `i-1` (then take 1 step) + ways to reach `i-2` (then take 2 steps)
- This is exactly the Fibonacci recurrence.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- ways[i] = number of distinct ways to reach step i (from step 0)

Goal: ways[n]

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

    ways[i] = ways[i - 1] + ways[i - 2]

Meaning:
- To reach i, you must come from i-1 (one jump) or i-2 (two jumps).

------------------------------------------------------------------------

## 🧱 4. Base Cases

- ways[0] = 1  (one way to stay at ground/start)
- ways[1] = 1  (only one way: take one 1-step)

Then build up for i = 2..n.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Only the last two states are needed:

- prev2 = ways[i-2]
- prev1 = ways[i-1]
- curr  = prev1 + prev2

Space: O(1)

------------------------------------------------------------------------

## 💻 6A. Clean Interview Version (No Modulo)

```java
class Solution {
    public int climbStairs(int n) {
        if (n == 0 || n == 1) return 1;

        int prev2 = 1; // ways[0]
        int prev1 = 1; // ways[1]

        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 💻 6B. With Modulo (Large Counts)

Use when problem asks to return ways modulo 1e9+7.

```java
class SolutionMod {
    private static final int MOD = 1_000_000_007;

    public int countDistinctWayToClimbStair(int n) {
        if (n == 0 || n == 1) return 1;

        long prev2 = 1; // ways[0]
        long prev1 = 1; // ways[1]

        for (int i = 2; i <= n; i++) {
            long curr = (prev1 + prev2) % MOD;
            prev2 = prev1;
            prev1 = curr;
        }
        return (int) prev1;
    }
}
```

------------------------------------------------------------------------

## 💻 6C. Matrix Exponentiation (O(log n))

For very large `n` (and optional modulo), use fast exponentiation of Fibonacci transition matrix:
- W(n) = Fibonacci(n+1) if F(0)=0, F(1)=1
- The matrix [[1,1],[1,0]]^k relates to F(k+1), F(k)

```java
class SolutionExpo {
    private static final long MOD = 1_000_000_007L;

    public int climbStairsExpo(int n) {
        // ways(n) = F(n+1) with F(0)=0,F(1)=1
        if (n == 0) return 1; // F(1)=1
        long[][] M = {{1,1},{1,0}};
        long[][] P = pow(M, n); // M^n
        // F(n+1) = P[0][0] (standard identity)
        return (int) (P[0][0] % MOD);
    }

    private long[][] pow(long[][] A, int e) {
        long[][] res = {{1,0},{0,1}}; // identity
        long[][] base = A;
        while (e > 0) {
            if ((e & 1) == 1) res = mul(res, base);
            base = mul(base, base);
            e >>= 1;
        }
        return res;
    }

    private long[][] mul(long[][] X, long[][] Y) {
        long[][] Z = new long[2][2];
        Z[0][0] = (X[0][0]*Y[0][0] + X[0][1]*Y[1][0]) % MOD;
        Z[0][1] = (X[0][0]*Y[0][1] + X[0][1]*Y[1][1]) % MOD;
        Z[1][0] = (X[1][0]*Y[0][0] + X[1][1]*Y[1][0]) % MOD;
        Z[1][1] = (X[1][0]*Y[0][1] + X[1][1]*Y[1][1]) % MOD;
        return Z;
    }
}
```

Complexity:
- Time: O(log n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input: n = 5

- ways[0]=1
- ways[1]=1
- ways[2]=2
- ways[3]=3
- ways[4]=5
- ways[5]=8

Answer: 8 distinct ways.

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Fibonacci DP / 1D Linear DP
- Triggers:
  - Count ways with small steps (1 or 2)
  - Recurrence depends only on last two states
- Relatives:
  - Tiling 2×n with 2×1 dominos
  - Decode Ways (different transition but similar counting-dp spirit)
  - Min cost climbing stairs (cost variant)

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- n = 0 → 1 (stay put)
- n = 1 → 1
- Beware of overflow if no modulo and very large n
- With modulo, use long during addition before casting to int

------------------------------------------------------------------------

## 🔁 10. Variants

- Min Cost Climbing Stairs: same graph but minimize sum of costs (different transition)
- Count ways with steps {1,2,3,...k}: generalized k-step Fibonacci
- Ways with forbidden steps: zero-out transitions for blocked indices

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Climbing stairs is the canonical Fibonacci DP.
- Master the O(1) space rolling approach.
- For huge `n`, matrix exponentiation computes the answer in O(log n).

------------------------------------------------------------------------

# End of Notes
