# Two Pointers and Sliding Window Notes

## 76 - Minimum Window Substring

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/minimum-window-substring/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given two strings `s` and `t` of lengths `m` and `n` respectively, return the **minimum window** **substring** of `s` such that every character in `t` (**including duplicates**) is included in the window. If there is no such substring, return the empty string `""`.

The testcases will be generated such that the answer is **unique**.

**Example 1:**

```text
Input: s = "ADOBECODEBANC", t = "ABC"
Output: "BANC"
Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
```

**Example 2:**

```text
Input: s = "a", t = "a"
Output: "a"
Explanation: The entire string s is the minimum window.
```

**Example 3:**

```text
Input: s = "a", t = "aa"
Output: ""
Explanation: Both 'a's from t must be included in the window.
Since the largest window of s only has one 'a', return empty string.
```

**Constraints:**

- `m == s.length`

- `n == t.length`

- `1 <= m, n <= 105`

- `s` and `t` consist of uppercase and lowercase English letters.

**Follow up:** Could you find an algorithm that runs in `O(m + n)` time?

------------------------------------------------------------------------

## 2. First Intuition

Expand the right side until the window contains all required characters, then shrink from the left while it remains valid. Every time it is valid, try to improve the answer.

------------------------------------------------------------------------

## 3. Expandable and Shrinkable Window

- The file uses two ASCII frequency arrays: one for `t`, one for the current window.
- `uniqueChar` counts how many distinct characters are required.
- `formed` counts how many required characters currently meet their exact needed frequency.
- When `formed == uniqueChar`, the window is valid and the left pointer moves inward to minimize it.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "ADOBECODEBANC"`, `t = "ABC"`: the first valid window is `ADOBEC`; shrinking and later expanding eventually finds `BANC`, which is the shortest valid window.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public String minWindow(String s, String t) {
    int[] need = new int[128];
    int[] window = new int[128];
    int required = 0;

    for (char c : t.toCharArray()) {
        if (need[c] == 0) required++;
        need[c]++;
    }

    int formed = 0;
    int left = 0;
    int bestLen = Integer.MAX_VALUE;
    int bestStart = 0;

    for (int right = 0; right < s.length(); right++) {
        char rc = s.charAt(right);
        window[rc]++;
        if (need[rc] > 0 && window[rc] == need[rc]) formed++;

        while (formed == required) {
            if (right - left + 1 < bestLen) {
                bestLen = right - left + 1;
                bestStart = left;
            }
            char lc = s.charAt(left++);
            window[lc]--;
            if (need[lc] > 0 && window[lc] < need[lc]) formed--;
        }
    }

    return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestStart, bestStart + bestLen);
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(|s| + |t|)`
- Space: `O(1)` for fixed ASCII arrays.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Variable-size minimum window: expand until valid, shrink while valid.
- Track satisfied character types, not just total character count.

------------------------------------------------------------------------

## End of Notes
