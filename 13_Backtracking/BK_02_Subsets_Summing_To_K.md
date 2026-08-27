# Backtracking Notes

## 02 - Subsets Summing to K

**Generated on:** 2026-08-27 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `nums` (may contain duplicates and negative numbers) and a target `k`, return every
**distinct** subset whose elements sum to `k`.

Example:

```text
Input:  nums = [1, 1, 2], k = 1
Output: [[1]]
```

------------------------------------------------------------------------

## 🪜 2. Core Idea

Same `choose → explore → undo` skeleton as `BK_01_Subsets.md`: sort, then include/exclude each
index, tracking a running sum. Two things this adds on top of plain Subsets:

- **Skip duplicate values correctly.** After sorting, only allow *including* `nums[index]` if it's
  a new value, or the identical previous element was also included in this path
  (`nums[index] != nums[index-1] || visited[index-1]`) — otherwise "skip an earlier copy, take a
  later identical one" duplicates the reverse choice.
- **Record a match exactly once, right when it's created** — check `newSum == k` immediately after
  adding an element, not unconditionally at the top of every call. Don't return early after a
  match either: with negatives or zeros in `nums`, a matched subset can still be validly extended
  (e.g. `{-1}` and `{-1, 0}` are both valid answers) — stopping early would silently drop those.

------------------------------------------------------------------------

## 🔁 3. Recurrence / Transition

Base case: `index == nums.length` → return. Handle `k == 0` (the empty subset) once, upfront,
since the recursion below only records a match when an element is added.

At each index:
1. **Include** `nums[index]` if allowed (see duplicate-guard above); if the new sum equals `k`,
   record a copy of `currentList` right there, then keep recursing regardless.
2. **Exclude** `nums[index]` — always allowed.

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

public class Solution {
    public static List<List<Integer>> getKSumSubsets(int[] nums, int k) {
        Arrays.sort(nums);
        List<List<Integer>> results = new ArrayList<>();
        if (k == 0) {
            results.add(new ArrayList<>());
        }
        generate(nums, results, 0, new ArrayList<>(), 0, k, new boolean[nums.length]);
        return results;
    }

    private static void generate(int[] nums, List<List<Integer>> results, int index,
                                  List<Integer> currentList, int currSum, int k, boolean[] visited) {
        if (index == nums.length) {
            return;
        }

        boolean canInclude = index == 0 || nums[index] != nums[index - 1] || visited[index - 1];
        if (canInclude) {
            visited[index] = true;
            currentList.add(nums[index]);
            int newSum = currSum + nums[index];
            if (newSum == k) {
                results.add(new ArrayList<>(currentList));
            }
            generate(nums, results, index + 1, currentList, newSum, k, visited);
            currentList.remove(currentList.size() - 1);
            visited[index] = false;
        }

        generate(nums, results, index + 1, currentList, currSum, k, visited);
    }
}
```

Complexity:
- Time: O(n · 2^n) worst case.
- Space: O(n) extra beyond the output.

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

`nums = [1, 1, 2], k = 1` (sorted):

```text
generate(0, [], 0)
├─ include idx0 (1) → sum=1==k -> RECORD [1]
│  ├─ include idx1 (1, allowed: visited[0]=true) → sum=2 -> generate(2, [1,1], 2) -> no more matches
│  └─ exclude idx1 → generate(2, [1], 1) -> no more matches (already recorded once)
└─ exclude idx0 → generate(1, [], 0)
   ├─ include idx1 (1, forbidden: nums[1]==nums[0] and visited[0]=false)  -- skipped
   └─ exclude idx1 → generate(2, [], 0) -> include idx2 (2), sum=2 -> no match
```

Result: `[[1]]` — recorded exactly once, and the duplicate "exclude idx0, include idx1" path never
runs.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Same include/exclude skeleton as `BK_01_Subsets.md`, plus a sum target and duplicate-value
  handling.
- "Stop once the goal is met" is a monotonicity-dependent optimization (safe only when every
  remaining candidate is strictly positive) — not a free correctness win.
- Related: Combination Sum / Combination Sum II (loop-based sibling shape for duplicate values),
  Subsets II (LC90).

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- **Don't `return` immediately after a match** — breaks the moment `nums` can contain a `0` or a
  negative number, since a matched subset can still be validly extended. Check-and-record on add
  instead.
- `k == 0` must be handled explicitly upfront — the recursion only records on element-add, and the
  empty subset never adds anything.
- `nums` must be sorted for the duplicate-value guard (`nums[index] != nums[index-1]`) to mean
  "adjacent equal values."
- `visited[index-1]` is only meaningful because it's reset immediately after its include-subtree
  returns — moving that reset breaks the guard silently.

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Duplicate values → guard the include branch with `nums[index] != nums[index-1] || visited[index-1]`.
- Record a match on add, not by checking-then-returning — early return silently drops valid
  extended answers whenever negatives or zeros are possible.
- `k == 0` needs its own explicit upfront check.

------------------------------------------------------------------------

# End of Notes
