# Backtracking Notes

## 01 - Subsets (Power Set)

**Generated on:** 2026-08-27 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array of `n` **unique** integers, return every possible subset (the power set) — including
the empty subset and the full array itself. Order of subsets in the output, and order of elements
within a subset, doesn't matter.

Example:

```text
Input:  nums = [1, 2, 3]
Output: [[], [3], [2], [2,3], [1], [1,3], [1,2], [1,2,3]]
```

There are always exactly `2^n` subsets — this is the first thing worth internalizing about
backtracking on subsets: the *shape* of the answer (how many results there'll be) is known before
you write a single line of code, which is a useful sanity check while learning.

------------------------------------------------------------------------

## 🪜 2. Core Idea

This is the canonical backtracking skeleton, usually summarized as **choose → explore → undo**:

- **Choose**: decide something about the current element (here: include it or exclude it).
- **Explore**: recurse into the rest of the problem with that choice locked in.
- **Undo**: before trying the *other* choice, undo whatever the first choice changed, so the shared
  `current` list is back to a clean state for the next branch.

For subsets specifically, walk through the array index by index (`0` to `n-1`), and at **every**
index make a binary decision: is `nums[index]` in this subset or not? Two choices per index, `n`
indices, gives exactly `2^n` leaves in the recursion tree — one leaf per subset.

The key mental model for a beginner: `current` is **one shared, mutable list** that gets built up
and torn down as the recursion explores the tree, not a fresh list per branch. That's *why*
backtracking needs an explicit "undo" step — without it, choices made in one branch would leak into
sibling branches that never wanted them.

------------------------------------------------------------------------

## 🔁 3. Recurrence / Transition

Base case:
- `index == nums.length` → the current path represents one complete subset. Record a **copy** of
  `current`, then return (nothing left to decide).

Transition, at each `index < nums.length`:
1. **Exclude** `nums[index]`: recurse to `index + 1` with `current` unchanged.
2. **Include** `nums[index]`: append it to `current`, recurse to `index + 1`, then remove it again
   (undo) before returning — so the caller sees `current` exactly as it was handed to us.

Order of the two branches (exclude-first vs include-first) only changes the *order* subsets appear
in the output, not which subsets get found.

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class FindSubsets {
    public static List<List<Integer>> findAllSubsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] nums, int index, List<Integer> current, List<List<Integer>> result) {
        if (index == nums.length) {
            result.add(new ArrayList<>(current));  // must copy — current keeps changing after this
            return;
        }

        // exclude nums[index]
        backtrack(nums, index + 1, current, result);

        // include nums[index]
        current.add(nums[index]);
        backtrack(nums, index + 1, current, result);
        current.remove(current.size() - 1);  // backtrack: undo the include before returning
    }
}
```

Complexity:
- Time: O(n · 2^n) — `2^n` subsets total, and copying each one into `result` costs up to O(n).
- Space: O(n) extra (recursion depth + the `current` list), not counting the O(n · 2^n) output
  itself.

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Input: `nums = [1, 2, 3]`. Tracing `backtrack(nums, index, current, result)` — this exclude-first
version visits the tree in this order:

```text
backtrack(0, [])
├─ exclude 1 → backtrack(1, [])
│  ├─ exclude 2 → backtrack(2, [])
│  │  ├─ exclude 3 → backtrack(3, [])        -> record []
│  │  └─ include 3 → backtrack(3, [3])       -> record [3]
│  └─ include 2 → backtrack(2, [2])
│     ├─ exclude 3 → backtrack(3, [2])       -> record [2]
│     └─ include 3 → backtrack(3, [2,3])     -> record [2,3]
└─ include 1 → backtrack(1, [1])
   ├─ exclude 2 → backtrack(2, [1])
   │  ├─ exclude 3 → backtrack(3, [1])       -> record [1]
   │  └─ include 3 → backtrack(3, [1,3])     -> record [1,3]
   └─ include 2 → backtrack(2, [1,2])
      ├─ exclude 3 → backtrack(3, [1,2])     -> record [1,2]
      └─ include 3 → backtrack(3, [1,2,3])   -> record [1,2,3]
```

8 leaves, 8 subsets: `[], [3], [2], [2,3], [1], [1,3], [1,2], [1,2,3]` — matches the traced output
exactly, and matches `2^3 = 8`.

Notice the **undo** happening concretely: after the `[2,3]` leaf is recorded, `current.remove(...)`
fires twice on the way back up (once undoing the `3`, once undoing the `2`) before the recursion
tries `include 1`'s branches — that's what makes `current` a valid `[]` again at the point where the
"include 1" branch starts, rather than leaking `2` or `3` into it.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the **binary choice per element** shape of backtracking — one of a few common shapes worth
recognizing on sight:

- **Binary choice per element** (this problem): Subsets, Partition Equal Subset Sum (as a
  decision problem rather than DP).
- **Choose one of several options per position, no reuse**: Permutations.
- **Choose zero-or-more from a candidate set, reuse allowed, prune when over target**: Combination
  Sum.
- **Constraint-satisfaction over a grid/board**: N-Queens, Sudoku Solver, Word Search.

Trigger phrases:
- "return all possible subsets/combinations/permutations"
- "generate every way to ..."
- small `n` in constraints (often `n <= 20` or so) — a strong hint that an exponential
  choose/explore/undo search is the *intended* approach, not a red flag that something smarter is
  required.

Related problems to build on next: Subsets II (LC90, same idea but the input has duplicates — needs
a sort + skip-adjacent-duplicates step to avoid duplicate subsets), Permutations (LC46), Combination
Sum (LC39).

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- **Forgetting to copy `current` before adding it to `result`.** `result.add(current)` (without
  `new ArrayList<>(...)`) would add a *reference* to the same mutable list object every time —
  since `current` keeps getting mutated afterward, every entry in `result` would end up pointing at
  the same final (probably empty, post-backtracking) list. This is the single most common bug for
  anyone learning backtracking for the first time. The comment already in the code above
  (`// must copy`) is flagging exactly this.
- **Forgetting the undo step** (`current.remove(current.size() - 1)`). Without it, the "include"
  choice would silently leak into every subsequent sibling branch that was supposed to explore
  *excluding* that element, corrupting the rest of the search.
- Empty input array (`nums = []`) → `backtrack` is called once with `index == 0 == nums.length`,
  immediately hits the base case, and records a single empty subset `[]`. Correct: the power set of
  the empty set is `{ {} }`, one element, matching `2^0 = 1`.
- This solution assumes **unique** elements in `nums` (as the problem guarantees). If duplicates
  were allowed, this exact code would produce duplicate subsets — see Subsets II in Pattern
  Recognition for the fix (sort first, then skip an "include" branch if it would restart on the
  same value as a sibling branch already tried).
- Order of `exclude` vs `include` in the recursion only changes output *order*, not correctness —
  don't confuse "my output is in a different order than the example" with "my code is wrong."

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Backtracking's core shape: **choose → explore → undo** — a shared mutable `current` state, built
  up and torn back down as the recursion tree is walked.
- For subsets: one binary choice (include/exclude) per array index gives `2^n` leaves, one per
  subset — the recursion tree's shape *is* the power set.
- Always `new ArrayList<>(current)` when recording a result — never store the live, still-mutating
  reference.
- Every `current.add(...)` before a recursive call needs a matching `current.remove(...)` after it,
  so sibling branches start clean.

------------------------------------------------------------------------

# End of Notes
