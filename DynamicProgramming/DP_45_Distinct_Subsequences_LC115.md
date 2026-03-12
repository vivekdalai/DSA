# Dynamic Programming Notes

## 45 - Distinct Subsequences (LC 115) — Count Ways S contains T

**Generated on:** 2026-02-25 23:17:17 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s` and `t`, return the number of distinct subsequences of `s` which equals `t`.

A subsequence is formed by deleting zero or more characters without disturbing the relative order.

Examples:
- s = "babgbag", t = "bag" → 5
- s = "rabbbit", t = "rabbit" → 3

Key: Count all ways, not just feasibility.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = number of distinct subsequences of `s[0..i-1]` that equal `t[0..j-1]`
  (1-based dp indices)

Goal:
- `dp[n][m]` where `n = s.length`, `m = t.length`

Base:
- For any i, `dp[i][0] = 1` (empty t can be formed by deleting all from s)
- `dp[0][j>0] = 0` (non-empty t cannot be formed from empty s)

Transition:
- If `s[i-1] == t[j-1]`:
  - `dp[i][j] = dp[i-1][j-1] + dp[i-1][j]`
    - Use this matching char (dp[i-1][j-1]) or skip it in s (dp[i-1][j])
- Else:
  - `dp[i][j] = dp[i-1][j]` (skip s[i-1])

Complexity:
- Time O(n*m), Space O(n*m) or O(m) with 1D optimization

------------------------------------------------------------------------

## 💻 3. Bottom-Up Tabulation (2D)

```java
import java.util.*;

public class DistinctSubsequences2D {
    public int numDistinct(String s, String t) {
        int n = s.length(), m = t.length();
        long[][] dp = new long[n + 1][m + 1]; // may require long to avoid overflow in intermediate sums

        for (int i = 0; i <= n; i++) dp[i][0] = 1; // empty t

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        // problem expects int, but result can be large; cast if constraints allow
        return (int) dp[n][m];
    }

    public static void main(String[] args) {
        DistinctSubsequences2D solver = new DistinctSubsequences2D();
        System.out.println(solver.numDistinct("babgbag", "bag"));   // 5
        System.out.println(solver.numDistinct("rabbbit", "rabbit")); // 3
    }
}
```

Notes:
- Use long in dp to avoid overflow during addition if inputs are large (LC returns int; usually fits).

------------------------------------------------------------------------

## ⚡ 4. Space-Optimized (1D)

We can compress to 1D by iterating j from m down to 1:
- `dp[j] += dp[j-1]` when characters match
- Backward j ensures we use dp[j-1] from previous i (not overwritten for current i)

```java
import java.util.*;

public class DistinctSubsequences1D {
    public int numDistinct(String s, String t) {
        int n = s.length(), m = t.length();
        long[] dp = new long[m + 1];
        dp[0] = 1; // empty t

        for (int i = 1; i <= n; i++) {
            // walk j backward
            for (int j = m; j >= 1; j--) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] += dp[j - 1];
                }
            }
        }
        return (int) dp[m];
    }

    public static void main(String[] args) {
        DistinctSubsequences1D solver = new DistinctSubsequences1D();
        System.out.println(solver.numDistinct("babgbag", "bag"));   // 5
        System.out.println(solver.numDistinct("rabbbit", "rabbit")); // 3
    }
}
```

Complexity:
- Time: O(n*m)
- Space: O(m)

------------------------------------------------------------------------

## 💡 5. Top-Down Memoization (Optional)

```java
import java.util.*;

class DistinctSubsequencesMemo {
    Long[][] memo;

    public int numDistinct(String s, String t) {
        memo = new Long[s.length() + 1][t.length() + 1];
        return (int) dfs(s, t, s.length(), t.length());
    }

    private long dfs(String s, String t, int i, int j) {
        if (j == 0) return 1;   // matched all of t
        if (i == 0) return 0;   // s exhausted before matching t

        if (memo[i][j] != null) return memo[i][j];

        long ways = dfs(s, t, i - 1, j); // skip s[i-1]
        if (s.charAt(i - 1) == t.charAt(j - 1)) {
            ways += dfs(s, t, i - 1, j - 1); // match s[i-1] with t[j-1]
        }
        return memo[i][j] = ways;
    }
}
```

------------------------------------------------------------------------

## 🧪 6. Dry Run (s = "babgbag", t = "bag")

Intuition:
- We enumerate all ways to pick indices i<j<k such that s[i] = 'b', s[j] = 'a', s[k] = 'g'
- 1D dp updates backward to ensure correct counting without reusing the same char twice in one iteration.

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Count Distinct Subsequences (Counting DP on LCS-like grid)
- Family: 2D DP (prefix-based) with space optimization to 1D
- Triggers:
  - Count ways to form T from S subsequences
  - Transitions mirror LCS but add counts instead of taking max

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Empty t → 1 way always
- Empty s with non-empty t → 0
- Character comparisons must be strict (`==`)
- Use backward j in 1D dp to prevent overwriting needed states
- Large counts may require 64-bit

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Canonical counting DP: dp[i][j] = dp[i-1][j] + (s[i-1]==t[j-1] ? dp[i-1][j-1] : 0)
- 1D optimization is simple and efficient using backward traversal.
- Very common interview problem; know both 2D and 1D forms.

------------------------------------------------------------------------

# End of Notes
