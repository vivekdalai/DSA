# Two Pointers and Sliding Window Notes

## 438 - Find All Anagrams in a String

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/find-all-anagrams-in-a-string/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given two strings `s` and `p`, return an array of all the start indices of `p`'s anagrams in `s`. You may return the answer in **any order**.

**Example 1:**

```text
Input: s = "cbaebabacd", p = "abc"
Output: [0,6]
Explanation:
The substring with start index = 0 is "cba", which is an anagram of "abc".
The substring with start index = 6 is "bac", which is an anagram of "abc".
```

**Example 2:**

```text
Input: s = "abab", p = "ab"
Output: [0,1,2]
Explanation:
The substring with start index = 0 is "ab", which is an anagram of "ab".
The substring with start index = 1 is "ba", which is an anagram of "ab".
The substring with start index = 2 is "ab", which is an anagram of "ab".
```

**Constraints:**

- `1 <= s.length, p.length <= 3 * 104`

- `s` and `p` consist of lowercase English letters.

------------------------------------------------------------------------

## 2. First Intuition

Every anagram of `p` has exactly the same character counts and exactly length `p.length()`. Slide a fixed-size window over `s` and compare counts.

------------------------------------------------------------------------

## 3. Fixed Window Frequency Match

- The file builds `fp` for pattern counts and `fs` for the first window in `s`.
- It checks whether the two arrays match.
- Then it removes the outgoing left character and adds the incoming right character.
- Every matching window start index is added to the answer list.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "cbaebabacd"`, `p = "abc"`: the first window `cba` matches, so add `0`. Later window `bac` matches, so add `6`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public List<Integer> findAnagrams(String s, String p) {
    List<Integer> ans = new ArrayList<>();
    int n = s.length();
    int k = p.length();
    if (n < k) return ans;

    int[] need = new int[26];
    int[] window = new int[26];
    for (int i = 0; i < k; i++) {
        need[p.charAt(i) - 'a']++;
        window[s.charAt(i) - 'a']++;
    }

    for (int left = 0; left <= n - k; left++) {
        if (Arrays.equals(need, window)) ans.add(left);
        if (left == n - k) break;
        window[s.charAt(left) - 'a']--;
        window[s.charAt(left + k) - 'a']++;
    }
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(26 * n)`, effectively `O(n)`.
- Space: `O(1)` for two arrays of size 26.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Anagram windows are fixed-size frequency comparisons.
- With a fixed alphabet, array comparison is simple and fast enough.

------------------------------------------------------------------------

## End of Notes
