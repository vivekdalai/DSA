# Dynamic Programming Notes

## 53 - Word Break (LC 139) — Prefix Boolean DP

**Generated on:** 2026-08-03 01:10:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s` and a dictionary of strings `wordDict`, return `true` if `s` can
be segmented into a space-separated sequence of one or more dictionary words.

- The same dictionary word may be reused any number of times.
- Order matters (it's a segmentation of `s` left to right), but which word is picked
  at each cut point is a free choice — that's the DP.

Example: `s = "leetcode"`, `wordDict = ["leet", "code"]` → `true` (`"leet" + "code"`).

Reported directly in 2026 Amazon interview breakdowns as a common starter, often
extended with follow-ups (return the actual segmentations — Word Break II; or use a
Trie when the dictionary is large).

------------------------------------------------------------------------

## 🪜 2. State Definition

- `dp[i]` = `true` if the prefix `s[0..i)` (first `i` characters) can be fully
  segmented into dictionary words.

Goal: `dp[n]` where `n = s.length()`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For each `i` from `1..n`, look back at every split point `j < i`:

    dp[i] = OR over all j in [0, i)  of  ( dp[j] AND wordSet.contains(s.substring(j, i)) )

Meaning: if the prefix ending at some earlier cut `j` is already breakable, and the
remaining chunk `s[j..i)` is itself a dictionary word, then `i` is reachable too.

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- `dp[0] = true` (empty prefix is trivially "segmented" — this is what lets the first
  real word match).
- All other `dp[i]` start `false` until proven reachable.

Pitfalls:
- Forgetting `dp[0] = true` makes every answer `false`.
- Using a `List<String>.contains(...)` instead of a `HashSet<String>` turns the
  substring check into O(m) per lookup instead of O(1) — quietly blows up complexity.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

`dp[i]` genuinely depends on many earlier `dp[j]` (not just a fixed window), so this
stays a full 1D boolean array of size `n + 1` — O(n) space, no further compression.
The lookup structure (`HashSet` or `Trie`) is the real lever for speed, not the dp
array itself.

------------------------------------------------------------------------

## 💻 6A. Top-Down (Memoized Recursion)

```java
import java.util.*;

class SolutionTopDown {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        Boolean[] memo = new Boolean[s.length() + 1];
        return canBreak(s, 0, wordSet, memo);
    }

    private boolean canBreak(String s, int start, Set<String> wordSet, Boolean[] memo) {
        if (start == s.length()) return true;
        if (memo[start] != null) return memo[start];

        for (int end = start + 1; end <= s.length(); end++) {
            if (wordSet.contains(s.substring(start, end)) &&
                canBreak(s, end, wordSet, memo)) {
                return memo[start] = true;
            }
        }
        return memo[start] = false;
    }
}
```

Complexity:
- Time: O(n^2) states x O(n) substring cost ≈ O(n^3) worst case (substring copy is O(n))
- Space: O(n) recursion + memo

------------------------------------------------------------------------

## 💻 6B. Bottom-Up (1D Tabulation) — Interview-Preferred

```java
import java.util.*;

class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // no need to keep checking once i is reachable
                }
            }
        }
        return dp[n];
    }
}
```

Complexity:
- Time: O(n^2) split points x O(n) substring/hash cost ≈ O(n^3) worst case
- Space: O(n)

------------------------------------------------------------------------

## 💻 6C. Variant Pointer — Word Break II (Return All Sentences)

Same `dp[i]` reachability check first (prune impossible prefixes), then backtrack
only through indices where `dp[i]` is true, collecting words into sentences. Running
plain backtracking without the reachability prune first can go exponential on inputs
with no valid segmentation (classic LC139 "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
style adversarial case).

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input: `s = "leetcode"`, `wordDict = ["leet", "code"]`

- `dp[0] = true`
- `i=4`: `j=0`, `dp[0]=true`, `s[0..4)="leet"` ∈ dict → `dp[4] = true`
- `i=8`: `j=4`, `dp[4]=true`, `s[4..8)="code"` ∈ dict → `dp[8] = true`

Answer: `dp[8] = true`

Negative example: `s = "catsandog"`, `wordDict = ["cats","dog","sand","and","cat"]`
→ every prefix up to `"catsand"` breaks fine, but nothing bridges `"...and" → "og"`
(`"og"` isn't a word and `"dog"` doesn't align with the remaining `o`) → `dp[9] = false`.

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Prefix-Boolean Segmentation DP
- Family: 1D DP over string prefixes, unbounded reuse of dictionary "pieces"
  (same reuse spirit as Unbounded Knapsack / Coin Change — see [DP_04](DP_04_Coin_Change_Min_Coins.md))
- Triggers: "can this string/sequence be split into valid pieces from a set/dictionary"
- Scaling up: large dictionary → store `wordDict` in a Trie and walk it while scanning
  forward from each `dp[j]=true` index, instead of generating every substring.

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- `s` empty → `true` vacuously (`dp[0]` already `true`)
- `wordDict` empty and `s` non-empty → `false`
- A dictionary word longer than `s` → simply never matches, harmless
- Duplicate words in `wordDict` → harmless once put in a `HashSet`

Pitfalls:
- Missing `dp[0] = true` base case
- Linear-scan dictionary lookup instead of `HashSet`/`Trie` (correctness is fine,
  performance isn't)
- Confusing this with Word Break II and trying to collect sentences without first
  pruning with the boolean `dp` — causes TLE/exponential blowup on adversarial inputs

------------------------------------------------------------------------

## ✅ 10. Takeaway

- `dp[i]` = "can the first `i` characters be fully segmented" — classic prefix DP.
- Base case `dp[0] = true` is what seeds every subsequent match.
- Same unbounded-reuse spirit as Coin Change; dictionary lookup structure is the
  main performance lever, not the DP itself.

------------------------------------------------------------------------

# End of Notes
