# Two Pointers and Sliding Window Notes

## 424 - Longest Repeating Character Replacement

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-repeating-character-replacement/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given a string `s` and an integer `k`. You can choose any character of the string and change it to any other uppercase English character. You can perform this operation at most `k` times.

Return the length of the longest substring containing the same letter you can get after performing the above operations.

**Example 1:**

```text
Input: s = "ABAB", k = 2
Output: 4
Explanation: Replace the two 'A's with two 'B's or vice versa.
```

**Example 2:**

```text
Input: s = "AABABBA", k = 1
Output: 4
Explanation: Replace the one 'A' in the middle with 'B' and form "AABBBBA".
The substring "BBBB" has the longest repeating letters, which is 4.
There may exists other ways to achieve this answer too.
```

**Constraints:**

- `1 <= s.length <= 105`

- `s` consists of only uppercase English letters.

- `0 <= k <= s.length`

------------------------------------------------------------------------

## 2. First Intuition

Inside any window, the cheapest way to make all characters equal is to keep the most frequent character and replace every other character. The cost is `windowLength - maxFreq`.

------------------------------------------------------------------------

## 3. Window Cost from Max Frequency

- The file tracks character frequencies for uppercase letters.
- `maxFreq` stores the highest frequency seen in the current scan.
- If `windowLength - maxFreq > k`, the window needs too many replacements, so move `l`.
- The answer is the largest valid window length observed.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "AABABBA"`, `k = 1`: windows like `AABA` can become `AAAA` with one replacement, length `4`. Longer windows need more than one replacement, so the answer is `4`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int characterReplacement(String s, int k) {
    int[] freq = new int[26];
    int left = 0;
    int maxFreq = 0;
    int best = 0;

    for (int right = 0; right < s.length(); right++) {
        int idx = s.charAt(right) - 'A';
        freq[idx]++;
        maxFreq = Math.max(maxFreq, freq[idx]);

        while (right - left + 1 - maxFreq > k) {
            freq[s.charAt(left) - 'A']--;
            left++;
        }

        best = Math.max(best, right - left + 1);
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)` for 26 uppercase letters.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- For replacement windows, compute the cost to make the window uniform.
- A stale `maxFreq` is acceptable here because it never causes the answer to shrink incorrectly.

------------------------------------------------------------------------

## End of Notes
