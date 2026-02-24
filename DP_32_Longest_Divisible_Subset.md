# Dynamic Programming Notes

## 33 - Longest Divisible Subset (LDS)

**Generated on:** 2026-02-24 21:15:49 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a set/array of distinct positive integers `nums`, return the largest subset `answer` such that for every pair `(answer[i], answer[j])`, one divides the other:
- `answer[i] % answer[j] == 0` or `answer[j] % answer[i] == 0`

If there are multiple answers, return any of them.

Example:
- nums = [1, 2, 3] → answer = [1, 2] or [1, 3]
- nums = [1, 2, 4, 8] → answer = [1, 2, 4, 8]

Why DP:
- After sorting, the divisibility relation becomes monotonic-friendly (if `a | b` and `b | c`, then `a | c`).
- The problem reduces to an LIS-like DP with a different transition check (divisibility instead of `<`).

------------------------------------------------------------------------

## 🪜 2. Key Insight and Setup

- Sort `nums` ascending.
- Define:
  - `dp[i]` = length of the longest divisible subset that ends at index `i`.
  - `parent[i]` = predecessor index to reconstruct the subset.
- Transition:
  - For `j < i`, if `nums[i] % nums[j] == 0` and `dp[j] + 1 > dp[i]`, extend from `j` to `i`.

Track the best `(maxLen, lastIdx)` while computing, then backtrack via `parent[]` to reconstruct the subset.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Initialization:
- For all `i`: `dp[i] = 1`, `parent[i] = i` (each number by itself)

Transition:
- For `i` from `0..n-1`:
  - For `j` from `0..i-1`:
    - If `nums[i] % nums[j] == 0` and `dp[j] + 1 > dp[i]`:
      - `dp[i] = dp[j] + 1`
      - `parent[i] = j`

Keep `(maxLen, lastIdx)` as the global best.

Reconstruction:
- Starting from `lastIdx`, follow `parent[lastIdx]` until index repeats, pushing `nums[index]` into a list, then reverse.

------------------------------------------------------------------------

## 💻 4. Clean Interview Version (Java)

```java
import java.util.*;

class LongestDivisibleSubset {
    public List<Integer> largestDivisibleSubset(int[] nums) {
        int n = nums.length;
        if (n == 0) return new ArrayList<>();

        Arrays.sort(nums); // crucial

        int[] dp = new int[n];      // length ending at i
        int[] parent = new int[n];  // predecessor for reconstruction

        int maxLen = 1;
        int lastIdx = 0;

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            parent[i] = i;
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                lastIdx = i;
            }
        }

        // reconstruct
        List<Integer> res = new ArrayList<>();
        while (parent[lastIdx] != lastIdx) {
            res.add(nums[lastIdx]);
            lastIdx = parent[lastIdx];
        }
        res.add(nums[lastIdx]);
        Collections.reverse(res);
        return res;
    }
}
```

Complexity:
- Time: O(n^2) due to the double loop
- Space: O(n) for `dp` and `parent`

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

nums = [1, 16, 4, 7, 8]  
Sorted: [1, 4, 7, 8, 16]

- i=0 (1): dp=1, parent=0, best=1@[0]
- i=1 (4): 4%1==0 → dp[1]=2, parent[1]=0, best=2@[1]
- i=2 (7): 7%1==0 → dp[2]=2, parent[2]=0; 7%4!=0
- i=3 (8): 8%1==0 → dp[3]=2, parent[3]=0; 8%4==0 → dp[3]=3, parent[3]=1, best=3@[3]
- i=4 (16): 16%1==0 → dp[4]=2, parent[4]=0; 16%4==0 → dp[4]=3, parent[4]=1; 16%8==0 → dp[4]=4, parent[4]=3, best=4@[4]

Backtrack from idx=4 → 16 ← 8 ← 4 ← 1 → result [1, 4, 8, 16].

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: LIS with Divisibility
- Family: Sequence DP with reconstruction (parent pointers)
- Triggers:
  - “Largest subset where pairwise relation holds: a|b or b|a”
  - Sort + O(n^2) DP with custom compatibility check

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Distinct positive integers are assumed by the classic problem.
  - If duplicates exist, you can keep them; `x % x == 0` would allow chains with equals. Typically de-duplicate.
  - If zeros are present, decide on handling; since division by zero is invalid, filter out or special-case 0 (commonly the input avoids zeros).
- Sorting ascending is required; otherwise dp check is invalid.
- Very large arrays may require O(n^2) → consider pruning or heuristic grouping, but standard constraints fit.

------------------------------------------------------------------------

## 🔁 8. Variants

- Longest chain with a custom relation (e.g., `a | b` or `b | a`) → same template with a different comparator/check.
- Maximum sum divisible subset subject to divisibility chain (replace length with sum DP).
- Longest chain of strings by divisibility of lengths (or other transitive relation).

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Sort, then run LIS-style O(n^2) DP using divisibility as the extend condition.
- Track parents to reconstruct one optimal subset.
- Simple, robust, and interview-friendly.

------------------------------------------------------------------------

# End of Notes
