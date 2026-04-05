# Greedy Notes

## Split Array into Consecutive Subsequences

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given a sorted integer array `nums`, return `true` if it can be split into one or more subsequences such that:

- each subsequence is consecutive
- each subsequence has length at least `3`

Otherwise, return `false`.

**Example 1**

    Input: nums = [1,2,3,3,4,5]
    Output: true

One valid split is:

    [1,2,3] and [3,4,5]

**Example 2**

    Input: nums = [1,2,3,4,4,5]
    Output: false

------------------------------------------------------------------------

## 2. Two-Map Intuition

The file keeps two maps:

- `freq[x]` = how many copies of `x` remain unused
- `vMap[x]` = how many subsequences are currently waiting for `x` next

For each number `x`, always prefer:

1. extend an existing subsequence that needs `x`
2. otherwise start a new subsequence `x, x+1, x+2`

If neither is possible, return `false`.

------------------------------------------------------------------------

## 3. Why Extension Comes First

Short existing subsequences are dangerous.

If we start a new sequence too early, we may leave an older sequence stuck below length `3`.

So the greedy rule is:

- satisfy vacancies first
- create a new subsequence only when extension is impossible

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [1,2,3,3,4,5]

Use `1`:

- cannot extend anything
- start `[1,2,3]`
- now one subsequence waits for `4`

Use next `3`:

- cannot extend anything waiting for `3`
- start `[3,4,5]`

All numbers are used successfully.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static boolean isPossible(int[] nums) {
    Map<Integer, Integer> freq = new HashMap<>();
    Map<Integer, Integer> vMap = new HashMap<>();

    for (int x : nums) {
        freq.put(x, freq.getOrDefault(x, 0) + 1);
    }

    for (int x : nums) {
        if (freq.getOrDefault(x, 0) == 0) continue;

        freq.put(x, freq.get(x) - 1);

        if (vMap.getOrDefault(x, 0) > 0) {
            vMap.put(x, vMap.get(x) - 1);
            vMap.put(x + 1, vMap.getOrDefault(x + 1, 0) + 1);
        } else if (freq.getOrDefault(x + 1, 0) > 0 && freq.getOrDefault(x + 2, 0) > 0) {
            freq.put(x + 1, freq.get(x + 1) - 1);
            freq.put(x + 2, freq.get(x + 2) - 1);
            vMap.put(x + 3, vMap.getOrDefault(x + 3, 0) + 1);
        } else {
            return false;
        }
    }

    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(n)`

Pattern:

- frequency map + vacancy map
- extend existing structures before starting new ones

------------------------------------------------------------------------

## End of Notes
