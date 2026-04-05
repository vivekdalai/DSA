# Greedy Notes

## 17 - DI String Match

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/di-string-match/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s` of length `n` containing only `I` and `D`, construct a permutation of numbers from `0` to `n` such that:

- if `s[i] == 'I'`, then `perm[i] < perm[i + 1]`
- if `s[i] == 'D'`, then `perm[i] > perm[i + 1]`

Return any valid permutation.

**Example 1**

    Input: s = "IDID"
    Output: [0, 4, 1, 3, 2]

**Example 2**

    Input: s = "III"
    Output: [0, 1, 2, 3]

------------------------------------------------------------------------

## 2. Low-High Intuition

At every step, we only need to satisfy one relation:

- increasing
- or decreasing

So keep the smallest and largest remaining numbers:

    low = 0
    high = n

Then:

- for `I`, place `low` and increment it
- for `D`, place `high` and decrement it

This immediately guarantees the current relation.

------------------------------------------------------------------------

## 3. Why The Last Number Is Forced

After processing all `n` characters, exactly one number remains.

At that point:

    low == high

So the final position must receive that value.

No extra logic is needed.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    s = "IDID"

Start:

    low = 0, high = 4

Build answer:

- `I` -> place `0`, `low = 1`
- `D` -> place `4`, `high = 3`
- `I` -> place `1`, `low = 2`
- `D` -> place `3`, `high = 2`
- last value -> `2`

Result:

    [0, 4, 1, 3, 2]

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int[] diStringMatch(String s) {
    int low = 0, high = s.length();
    int[] ans = new int[s.length() + 1];

    for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == 'I') {
            ans[i] = low++;
        } else {
            ans[i] = high--;
        }
    }

    ans[ans.length - 1] = low;
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(n)` for the answer array

Pattern:

- maintain a remaining range
- satisfy each relation using one extreme

------------------------------------------------------------------------

## End of Notes
