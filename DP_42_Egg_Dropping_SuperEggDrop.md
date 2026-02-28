# Dynamic Programming Notes

## 42 - Egg Dropping / Super Egg Drop (LC 887)

**Generated on:** 2026-02-25 23:09:25 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You have `K` eggs and a building with `N` floors (1..N). An egg breaks if dropped from a floor higher than a fixed threshold `F`, and doesn’t break otherwise.  
Your goal is to determine `F` using the minimum number of moves (drops) in the worst case.

Classical variants:
- Min number of moves (drops) needed for given `K` eggs and `N` floors (this is LC 887).
- Or, for a fixed number of moves `M`, how many floors can we check with `K` eggs? (dual form that leads to a very clean DP).

------------------------------------------------------------------------

## 🪜 2. Two Canonical DP Formulations

A) DP by floors (slower O(K*N*logN) or O(K*N^2))  
- `dp[k][n]` = minimum number of moves (drops) needed with k eggs and n floors.
- Transition (try dropping from floor `x` and take worst case between break/no-break):
  - `dp[k][n] = 1 + min over x ( max( dp[k-1][x-1], dp[k][n-x] ) )`
  - Egg breaks → search below (x-1 floors) with k-1 eggs
  - Egg survives → search above (n-x floors) with k eggs
- Use binary search on `x` due to convexity/monotonicity to optimize to O(K*N*logN).  
  Otherwise naive min over x is O(K*N^2) (too slow for large N).

B) DP by moves (clean and fast; recommended)  
- `dp[m][k]` = maximum number of floors we can check with `m` moves and `k` eggs.
- Recurrence:
  - `dp[m][k] = dp[m-1][k-1] + dp[m-1][k] + 1`
    - Explanation: in one move, drop from floor x:
      - if egg breaks → we can check `dp[m-1][k-1]` floors below
      - if egg survives → we can check `dp[m-1][k]` floors above
      - plus the current floor = +1
- Then for given `K, N`, find the smallest `m` such that `dp[m][K] >= N`.
- Complexity: O(K * M), where `M` is the minimal answer; `M` is typically O(log N) to O(N) depending on K.

This moves-based DP is the standard accepted solution for LC 887.

------------------------------------------------------------------------

## 🔁 3. DP by Moves (Recommended)

- Initialize dp[0][k] = 0 for all k (0 moves → 0 floors)
- For m from 1.. until dp[m][K] >= N
  - For k from 1..K
    - dp[m][k] = dp[m-1][k-1] + dp[m-1][k] + 1
- The minimal `m` when dp[m][K] >= N is the answer.

Intuition:
- Each extra move expands our coverage by combining break and survive branches.

------------------------------------------------------------------------

## 💻 4A. Java Implementation (Moves-based)

```java
import java.util.*;

public class SuperEggDropMovesDP {
    public int superEggDrop(int K, int N) {
        // dp[m][k] = max floors we can check with m moves and k eggs
        // We only need the previous row, can use 1D dp updated right-to-left (or left-to-right with temp)
        long[] dp = new long[K + 1]; // use long to avoid overflow on large N
        int m = 0;
        while (dp[K] < N) {
            m++;
            // update in reverse to use dp[k] (prev row) and dp[k-1] (prev row)
            for (int k = K; k >= 1; k--) {
                dp[k] = dp[k] + dp[k - 1] + 1;
            }
        }
        return m;
    }

    public static void main(String[] args) {
        SuperEggDropMovesDP solver = new SuperEggDropMovesDP();
        System.out.println(solver.superEggDrop(1, 2));  // 2
        System.out.println(solver.superEggDrop(2, 6));  // 3
        System.out.println(solver.superEggDrop(3, 14)); // 4
    }
}
```

Complexity:
- Time: O(K * answer), Space: O(K)
- For large `N` and `K`, this scales very well compared to floor-based DP.

------------------------------------------------------------------------

## 💡 4B. Floor-based DP with Binary Search (Educational)

```java
import java.util.*;

class SuperEggDropFloorsDP {
    // dp[k][n] = min moves to find F with k eggs and n floors
    // Transition with binary search on x:
    // dp[k][n] = 1 + min_x max( dp[k-1][x-1], dp[k][n-x] )

    public int superEggDrop(int K, int N) {
        int[][] dp = new int[K + 1][N + 1];
        // Base:
        // dp[0][*] = INF (or unused)
        // dp[1][n] = n (linear scan)
        for (int n = 1; n <= N; n++) dp[1][n] = n;

        for (int k = 2; k <= K; k++) {
            for (int n = 1; n <= N; n++) {
                int lo = 1, hi = n, best = n; // upper bound n
                while (lo <= hi) {
                    int x = (lo + hi) / 2;
                    int broken = dp[k - 1][x - 1];   // egg breaks
                    int survive = dp[k][n - x];      // egg survives
                    int worst = 1 + Math.max(broken, survive);
                    best = Math.min(best, worst);
                    // Move by comparing broken vs survive to reduce range
                    if (broken < survive) { 
                        lo = x + 1;
                    } else {
                        hi = x - 1;
                    }
                }
                dp[k][n] = best;
            }
        }
        return dp[K][N];
    }
}
```

Complexity:
- Time: O(K * N * log N), Space: O(K * N)
- Much slower than the moves-based DP; useful to understand the original recurrence.

------------------------------------------------------------------------

## 🔎 5. Example Checks

- K=1, N=2 → Answer=2 (drop from 1, then from 2)
- K=2, N=6 → Answer=3 (classic)
- K=3, N=14 → Answer=4

Moves-based dp progression example (K=2):
- m=1: dp[1][1]=1, dp[1][2]=1 → max floors = 1
- m=2: dp[2][1]=2, dp[2][2]=3 → max floors = 3
- m=3: dp[3][1]=3, dp[3][2]=6 → max floors = 6 → answer 3 for N=6

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Egg Dropping / Super Egg Drop
- Family: Interval/Matrix DP (floor-based) vs Moves DP (coverage-based)
- Triggers:
  - Minimax of worst-case outcomes across two branches (break vs survive)
  - Monotonicity enables binary search optimization in floor-based DP
  - Moves DP provides a clean linear-time (in K and answer) solution

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- N = 0 → 0 moves
- K = 1 → N moves
- Use long in moves-DP to prevent overflow when summing dp[k] + dp[k-1] + 1 for large N
- Floor-based DP can TLE for large N, prefer moves-based for LC 887

------------------------------------------------------------------------

## ✅ 8. Takeaway

- For interviews/LC 887, the moves-based DP is the clearest and fastest approach.
- Floor-based DP teaches the fundamental recurrence and can be optimized with binary search.
- Know both formulations; implement moves-based under time constraints.

------------------------------------------------------------------------

# End of Notes
