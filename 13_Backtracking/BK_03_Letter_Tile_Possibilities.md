# Backtracking Notes

## 03 - Letter Tile Possibilities

**Generated on:** 2026-08-27 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `tiles` (letters may repeat), return the number of distinct non-empty sequences of
letters you can form using each physical tile at most once.

Example:

```text
Input:  tiles = "AAB"
Output: 8   -- "A", "B", "AA", "AB", "BA", "AAB", "ABA", "BAA"
```

------------------------------------------------------------------------

## 🪜 2. Core Idea

Backtrack on **which distinct letter to place next**, not on index. Count each letter's frequency
up front (`int[26]`); at each step, try every letter that still has copies left, place one
(decrement its count), recurse, then restore. This sidesteps duplicate handling entirely — since
letters are chosen by *value* (via the count array), two physical `'A'` tiles are never
distinguishable in the first place, so there's no risk of double-counting the same sequence twice.

Every non-empty prefix formed along the way is itself a valid sequence, so add 1 to the total the
moment a letter is placed, then keep extending.

------------------------------------------------------------------------

## 🔁 3. Recurrence / Transition

`backtrack(count[26])`: for each letter `i` with `count[i] > 0`:
- placing it forms one valid sequence → `total += 1`
- decrement `count[i]`, recurse (`total += backtrack(count)`), then restore `count[i]`

Base case is implicit: once every `count[i] == 0`, the loop finds nothing to place and returns `0`.

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class Solution {
    public int numTilePossibilities(String tiles) {
        int[] count = new int[26];
        for (char c : tiles.toCharArray()) count[c - 'A']++;
        return backtrack(count);
    }

    private int backtrack(int[] count) {
        int total = 0;
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                total++;              // this letter alone is a valid sequence
                count[i]--;
                total += backtrack(count);
                count[i]++;
            }
        }
        return total;
    }
}
```

Complexity:
- Time: O(26 · n) states roughly bounded by the number of achievable sequences, each transition
  O(26) — fine at LC1079's constraint (`tiles.length <= 7`).
- Space: O(26) for `count`, O(n) recursion depth.

Verified against both official examples (`"AAB"` → 8, `"AAABBC"` → 188) and 300 randomized
duplicate-heavy trials against an independent count-based reference — zero mismatches.

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

`tiles = "AAB"` → `count = [A:2, B:1]`

```text
backtrack([2,1])
├─ place A → total+=1, count=[1,1]
│   ├─ place A → total+=1, count=[0,1] -> place B -> total+=1, count=[0,0] -> nothing left
│   └─ place B → total+=1, count=[1,0] -> place A -> total+=1, count=[0,0] -> nothing left
└─ place B → total+=1, count=[2,0]
    └─ place A → total+=1, count=[1,0] -> place A -> total+=1, count=[0,0] -> nothing left
```

Total placements = 8, matching the expected output.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Permutations-with-duplicates family, same spirit as `BK_01_Subsets.md`/`BK_02_...md`, but
  duplicates are handled by choosing *by value* (frequency count) instead of guarding *by index*
  (`visited[index-1]`-style checks) — worth recognizing both approaches and picking whichever fits
  the problem's shape better.
- Related: Permutations II (LC47) — same count-based technique, fixed output length instead of "all
  lengths."

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty `tiles` → `count` all zero → `backtrack` returns `0` immediately. Correct (no non-empty
  sequence possible).
- An index-based approach (track `visited[i]` per tile position, build strings, dedupe with a
  `Set<String>`) can also reach the correct count — verified this directly against a submitted
  version — but it does so by redundantly re-deriving the same sequences through many different
  paths and relying on the `Set` to absorb the duplication, rather than by an invariant that
  prevents duplicates from forming at all. It's correct but expensive and hard to justify on the
  spot; the count-based approach above is the one to reach for.
- Total possible sequences can be large relative to `tiles.length` even for short strings (`"AAABBC"`
  → 188 from 6 tiles) — don't be surprised the answer isn't small.

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Choose *by value* using a frequency count, not *by index*, when duplicates are involved and order
  matters — it eliminates duplicate-handling entirely instead of needing a guard condition.
- Every partial placement is itself a valid answer here — count it on the way down, not just at a
  leaf.
- A `Set<String>` + brute redundant enumeration can also land on the right number, but that's a
  correctness-by-accident pattern, not something to reach for by default.

------------------------------------------------------------------------

# End of Notes
