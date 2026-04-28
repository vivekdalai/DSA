# Hash Table Notes

## 1525 - Number of Good Ways to Split a String

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/number-of-good-ways-to-split-a-string/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given a string `s`.

A split is called **good** if you can split `s` into two non-empty strings `sleft` and `sright` where their concatenation is equal to `s` (i.e., `sleft + sright = s`) and the number of distinct letters in `sleft` and `sright` is the same.

Return the number of **good splits** you can make in `s`.

**Example 1:**

```text
Input: s = "aacaba"
Output: 2
Explanation: There are 5 ways to split "aacaba" and 2 of them are good.
("a", "acaba") Left string and right string contains 1 and 3 different letters respectively.
("aa", "caba") Left string and right string contains 1 and 3 different letters respectively.
("aac", "aba") Left string and right string contains 2 and 2 different letters respectively (good split).
("aaca", "ba") Left string and right string contains 2 and 2 different letters respectively (good split).
("aacab", "a") Left string and right string contains 3 and 1 different letters respectively.
```

**Example 2:**

```text
Input: s = "abcd"
Output: 1
Explanation: Split the string as follows ("ab", "cd").
```

**Constraints:**

- `1 <= s.length <= 105`

- `s` consists of only lowercase English letters.

------------------------------------------------------------------------

## 2. First Intuition

A split is good when the left side and right side have the same number of distinct characters. Move the split from left to right and maintain both sides.

------------------------------------------------------------------------

## 3. Left and Right Frequency Maps

- The file starts with every character counted on the right side.
- For each character, it moves one occurrence to the left side.
- It compares the number of non-zero buckets in both frequency arrays.
- Since there are only 26 lowercase letters, even recomputing the distinct counts is effectively constant work.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "aacaba"`: as the split moves, the good cuts are after `aac` and after `aaca`, so the answer is `2`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int numSplits(String s) {
    int[] left = new int[26];
    int[] right = new int[26];
    int leftDistinct = 0;
    int rightDistinct = 0;

    for (char c : s.toCharArray()) {
        if (right[c - 'a']++ == 0) rightDistinct++;
    }

    int ans = 0;
    for (int i = 0; i < s.length() - 1; i++) {
        int idx = s.charAt(i) - 'a';
        if (left[idx]++ == 0) leftDistinct++;
        if (--right[idx] == 0) rightDistinct--;
        if (leftDistinct == rightDistinct) ans++;
    }
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)` for two arrays of size 26.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- For split-counting questions, maintain what is left and what remains right.
- Track distinct counts directly when possible.

------------------------------------------------------------------------

## End of Notes
