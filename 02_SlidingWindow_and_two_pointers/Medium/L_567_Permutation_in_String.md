# Two Pointers and Sliding Window Notes

## 567 - Permutation in String

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/permutation-in-string/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given two strings `s1` and `s2`, return `true` if `s2` contains a permutation of `s1`, or `false` otherwise.

In other words, return `true` if one of `s1`'s permutations is the substring of `s2`.

**Example 1:**

```text
Input: s1 = "ab", s2 = "eidbaooo"
Output: true
Explanation: s2 contains one permutation of s1 ("ba").
```

**Example 2:**

```text
Input: s1 = "ab", s2 = "eidboaoo"
Output: false
```

**Constraints:**

- `1 <= s1.length, s2.length <= 104`

- `s1` and `s2` consist of lowercase English letters.

------------------------------------------------------------------------

## 2. First Intuition

A permutation of `p` is just an anagram of `p`. So the question becomes whether any window of length `p.length()` in `s` has the same character counts.

------------------------------------------------------------------------

## 3. Fixed Window Anagram Check

- The file builds frequency arrays for `p` and the first same-length window of `s`.
- It checks the arrays, then slides one character at a time.
- If any window has matching counts, it returns `true`; otherwise it returns `false` after the scan.

------------------------------------------------------------------------

## 4. Short Dry Run

For `p = "ab"`, `s = "eidbaooo"`: windows `ei` and `id` fail, but `ba` matches the counts of `ab`, so return `true`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean checkInclusion(String p, String s) {
    int k = p.length();
    if (s.length() < k) return false;

    int[] need = new int[26];
    int[] window = new int[26];
    for (int i = 0; i < k; i++) {
        need[p.charAt(i) - 'a']++;
        window[s.charAt(i) - 'a']++;
    }

    for (int left = 0; left <= s.length() - k; left++) {
        if (Arrays.equals(need, window)) return true;
        if (left == s.length() - k) break;
        window[s.charAt(left) - 'a']--;
        window[s.charAt(left + k) - 'a']++;
    }
    return false;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(26 * n)`, effectively `O(n)`.
- Space: `O(1)`.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Permutation in string is the same pattern as fixed-window anagram search.
- Early return is enough because the problem asks for existence, not all starts.

------------------------------------------------------------------------

## End of Notes
