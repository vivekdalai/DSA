# Dynamic Programming Notes

## 39 - Frog Jump (LeetCode 403) — Can the Frog Cross?

**Generated on:** 2026-02-25 11:53:49 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given a list of stones’ positions in a river (strictly increasing, starting at 0).  
The frog starts on the first stone (position 0) and wants to reach the last stone.  

Rules:
- On the very first move, the frog must jump exactly 1 unit.
- If the frog’s last jump was k units, the next jump must be either k-1, k, or k+1 units.
- The frog can only land on stones (not in water).

Question: Return true if the frog can reach the last stone, else false.

Example:
- stones = [0,1,3,5,6,8,12,17] → true

------------------------------------------------------------------------

## 🪜 2. State Definition (Top-Down DP)

Observation:
- The state depends on two things:
  - Which stone index we’re at (currPos index i)
  - What the last jump length was (k)

Define:
- dp[i][k] = whether it is possible to reach the last stone starting from stone index i, given that the last jump was k units.

Transitions:
- From state (i, k), try next jumps j ∈ {k-1, k, k+1} with j > 0:
  - nextPosition = stones[i] + j
  - If nextPosition is a stone (we can find its index via a map), recurse to (nextIndex, j).

Base case:
- If i == n-1 (we’re on the last stone), return true.

Pruning:
- If stones[1] != 1, immediately return false (first jump must be 1).
- Optional: If gap between consecutive stones is too large relative to index (e.g., stones[i] - stones[i-1] > i), return false (coarse but effective check).

------------------------------------------------------------------------

## 💻 3. Provided Solution (Top-Down with Memo)

```java
import java.util.*;

class Solution {
    Map<Integer, Integer> stonesToIndexMap;
    Boolean[][] dp;
    // dp[i][j] => standing at stone index i and last jump was j units.
    public boolean canCross(int[] stones) {
        if (stones.length < 2) return true;
        if (stones[1] != 1) return false; // first jump must be 1

        int n = stones.length;
        stonesToIndexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            stonesToIndexMap.put(stones[i], i);
        }
        dp = new Boolean[n][n]; // last jump k never needs to exceed n-1

        return dfs(stones, 0, 0);
    }

    private boolean dfs(int[] stones, int currPos, int lastJump) {
        if (currPos == stones.length - 1) return true;
        if (dp[currPos][lastJump] != null) return dp[currPos][lastJump];

        for (int j = lastJump - 1; j <= lastJump + 1; j++) {
            if (j > 0) {
                int nextPos = stones[currPos] + j;
                Integer nextIdx = stonesToIndexMap.get(nextPos);
                if (nextIdx != null && dfs(stones, nextIdx, j)) {
                    return dp[currPos][lastJump] = true;
                }
            }
        }
        return dp[currPos][lastJump] = false;
    }
}
```

Complexity:
- Time: O(n^2) in the worst case (each (i,k) visited once; branching factor ≤ 3 but heavily pruned by map lookup).
- Space: O(n^2) for dp + O(n) recursion depth.

Why it works:
- The frog’s reachable states are fully determined by (stone index, last jump length).
- Memoization avoids re-exploring the same (i, k) state.

------------------------------------------------------------------------

## 🔧 4. Iterative Set-DP Alternative (HashSet per Stone)

Another common approach:
- For each stone position, keep a set of last jump lengths that can land the frog on that stone.
- Start with map[pos -> set of k], where map[0] = {0}.
- For each stone in order, propagate k ∈ set to next stones at pos + k - 1, pos + k, pos + k + 1 if they exist (> 0).
- If the last stone’s set is non-empty, return true.

```java
import java.util.*;

class FrogJump_SetDP {
    public boolean canCross(int[] stones) {
        int n = stones.length;
        if (stones[1] != 1) return false;

        Map<Integer, Set<Integer>> jumpsAt = new HashMap<>();
        for (int s : stones) jumpsAt.put(s, new HashSet<>());
        jumpsAt.get(0).add(0);

        for (int s : stones) {
            for (int k : jumpsAt.get(s)) {
                for (int step = k - 1; step <= k + 1; step++) {
                    if (step > 0) {
                        int next = s + step;
                        if (jumpsAt.containsKey(next)) {
                            jumpsAt.get(next).add(step);
                        }
                    }
                }
            }
        }
        return !jumpsAt.get(stones[n - 1]).isEmpty();
    }
}
```

Complexity:
- Time: O(n^2) worst-case (each stone may get up to O(n) distinct k’s).
- Space: O(n^2) in the worst case.

Pros:
- No explicit 2D Boolean dp; often simpler to reason iteratively.
- Good practical performance on constraints.

------------------------------------------------------------------------

## 🧪 5. Edge Cases and Checks

- stones[1] != 1 → false (first jump must be 1).
- Large gaps early make it impossible:
  - Optional pre-check: for i from 1..n-1, if (stones[i] - stones[i-1]) > i, return false.
- n small (e.g., n=2 with stones [0,1]) → true.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: “DP on index and last move”
- Family: Graph/DP hybrid (states: nodes = (i,k), edges from (i,k) → (j,k’))
- Triggers:
  - Movement constrained by relation to the last choice (k-1, k, k+1)
  - Need to remember last step size

------------------------------------------------------------------------

## 📌 7. Tips and Pitfalls

- Use a position-to-index map for O(1) stone existence checks.
- Memoize (i,k) results; k never needs to exceed n-1.
- In the Set-DP version, always check step > 0.

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Either memoized DFS over (i,k) or iterative map<position, set<k>> yields an O(n^2) solution.
- The provided top-down DP solution is clean and efficient for LeetCode 403.

------------------------------------------------------------------------

# End of Notes
