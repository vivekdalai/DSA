# Dynamic Programming Notes

## 30 - Edit Distance (LC 72) — Min Operations to Convert word1 → word2

**Generated on:** 2026-02-24 20:57:02 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `word1` and `word2`, return the minimum number of operations required to convert `word1` into `word2`.  
Allowed operations:
- Insert a character
- Delete a character
- Replace a character

Example:
- word1 = "horse", word2 = "ros" → answer = 3  
  horse → rorse (replace 'h'→'r') → rose (delete 'r') → ros (delete 'e')

Why DP:
- Overlapping subproblems on prefixes (i, j)
- Optimal substructure: locally optimal choices reduce to smaller prefix pairs

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = minimum operations to convert `word1[0..i-1]` → `word2[0..j-1]` (1-based dp size)

Answer:
- `dp[n][m]` where `n = word1.length`, `m = word2.length`

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Base:
- `dp[0][j] = j` (convert empty → first j chars by j inserts)
- `dp[i][0] = i` (convert first i chars → empty by i deletes)

Transition:
- If `word1[i-1] == word2[j-1]`:
  - `dp[i][j] = dp[i-1][j-1]` (no op needed)
- Else:
  - Insert:  `1 + dp[i][j-1]`      (insert word2[j-1] into word1)
  - Delete:  `1 + dp[i-1][j]`      (delete word1[i-1])
  - Replace: `1 + dp[i-1][j-1]`    (replace word1[i-1] with word2[j-1])
  - `dp[i][j] = min(Insert, Delete, Replace)`

------------------------------------------------------------------------

## 💻 4A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class EditDistanceMemo {
    public int minDistance(String word1, String word2) {
        int n = word1.length(), m = word2.length();
        Integer[][] memo = new Integer[n + 1][m + 1];
        return solve(word1, word2, n, m, memo);
    }

    private int solve(String a, String b, int i, int j, Integer[][] memo) {
        // dp dimension uses counts of prefixes (i chars of a, j chars of b)
        if (i == 0) return j; // insert j chars
        if (j == 0) return i; // delete i chars

        if (memo[i][j] != null) return memo[i][j];

        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return memo[i][j] = solve(a, b, i - 1, j - 1, memo);
        }

        int insert  = 1 + solve(a, b, i, j - 1, memo);
        int delete  = 1 + solve(a, b, i - 1, j, memo);
        int replace = 1 + solve(a, b, i - 1, j - 1, memo);

        return memo[i][j] = Math.min(insert, Math.min(delete, replace));
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(n · m) memo + O(n + m) recursion

------------------------------------------------------------------------

## 💻 4B. Bottom-Up Tabulation (1-based)

```java
class EditDistanceTab {
    public int minDistance(String word1, String word2) {
        int n = word1.length(), m = word2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i; // deletions
        for (int j = 0; j <= m; j++) dp[0][j] = j; // insertions

        for (int i = 1; i <= n; i++) {
            char ca = word1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = word2.charAt(j - 1);
                if (ca == cb) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert  = 1 + dp[i][j - 1];
                    int delete  = 1 + dp[i - 1][j];
                    int replace = 1 + dp[i - 1][j - 1];
                    dp[i][j] = Math.min(insert, Math.min(delete, replace));
                }
            }
        }
        return dp[n][m];
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(n · m)

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (O(m)) using rolling rows

```java
class EditDistanceSpace {
    public int minDistance(String word1, String word2) {
        int n = word1.length(), m = word2.length();
        // prev[j] = dp[i-1][j], curr[j] = dp[i][j]
        int[] prev = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            int[] curr = new int[m + 1];
            curr[0] = i;
            char ca = word1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = word2.charAt(j - 1);
                if (ca == cb) {
                    curr[j] = prev[j - 1];
                } else {
                    int insert  = 1 + curr[j - 1]; // dp[i][j-1]
                    int delete  = 1 + prev[j];     // dp[i-1][j]
                    int replace = 1 + prev[j - 1]; // dp[i-1][j-1]
                    curr[j] = Math.min(insert, Math.min(delete, replace));
                }
            }
            prev = curr;
        }
        return prev[m];
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(m)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

word1 = "intention", word2 = "execution"  
Answer = 5  
One optimal sequence (not unique):
- intention → inention (delete 't')
- inention → enention (replace 'i'→'e')
- enention → exention (replace 'n'→'x')
- exention → exection (replace 'n'→'c')
- exection → execution (insert 'u')

dp[n][m] = 5.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Edit Distance / Levenshtein Distance
- Family: DP on strings (edit/compare)
- Triggers:
  - Allowed operations: insert, delete, replace
  - Minimize number of edits to transform A → B
- Relatives:
  - LCS (no replace op; derive min deletions+insertions via LCS)
  - Damerau–Levenshtein (adds transposition op)
  - Min insertions/deletions to make strings equal via: `n + m - 2*LCS`

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty strings: dp handles gracefully (cost = length of other string)
- Case-sensitivity: treat as per problem statement
- Operation costs are uniform (all cost 1) in LC72; weighted variants require minor changes

------------------------------------------------------------------------

## 🔁 8. Variants

- Minimum deletions to make two strings equal (without replace) → `n + m - 2*LCS`
- Transform cost matrix (different costs for insert/delete/replace) → adjust transitions
- Edit distance with only insert/delete (not replace) → remove replace option

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Standard 2D DP on prefixes with base cases for empty prefixes
- Replace allowed makes it Levenshtein; removing it maps to LCS-related costs
- Space optimization to O(m) is simple and interview-friendly

------------------------------------------------------------------------

# End of Notes
