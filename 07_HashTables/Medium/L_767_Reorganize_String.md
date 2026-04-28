# Hash Table Notes

## 767 - Reorganize String

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/reorganize-string/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s`, rearrange the characters of `s` so that any two adjacent characters are not the same.

Return any possible rearrangement of `s` or return `""` if not possible.

**Example 1:**

```text
Input: s = "aab"
Output: "aba"
```

**Example 2:**

```text
Input: s = "aaab"
Output: ""
```

**Constraints:**

- `1 <= s.length <= 500`

- `s` consists of lowercase English letters.

------------------------------------------------------------------------

## 2. First Intuition

The most frequent character is the hardest to place. If it appears more than half the string length rounded up, some adjacent duplicates are unavoidable. Otherwise, greedily take high-frequency characters while avoiding the previous character.

------------------------------------------------------------------------

## 3. Max Heap by Character Frequency

- The file counts lowercase frequencies and rejects impossible cases early.
- It pushes available characters into a max heap by remaining count.
- Each step tries the most frequent character; if it equals the last appended character, it uses the next most frequent character instead.
- After using a character, its remaining count is decreased and pushed back if still positive.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "aab"`: counts are `a:2, b:1`. Pick `a`, then cannot pick `a` again, so pick `b`, then pick the remaining `a`, forming `aba`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public String reorganizeString(String s) {
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
        if (freq[c - 'a'] > (s.length() + 1) / 2) return "";
    }

    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);
    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0) pq.offer(new int[] {freq[i], i});
    }

    StringBuilder ans = new StringBuilder();
    while (!pq.isEmpty()) {
        int[] first = pq.poll();
        if (ans.length() > 0 && ans.charAt(ans.length() - 1) == (char) (first[1] + 'a')) {
            if (pq.isEmpty()) return "";
            int[] second = pq.poll();
            ans.append((char) (second[1] + 'a'));
            if (--second[0] > 0) pq.offer(second);
            pq.offer(first);
        } else {
            ans.append((char) (first[1] + 'a'));
            if (--first[0] > 0) pq.offer(first);
        }
    }
    return ans.toString();
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log 26)`, effectively `O(n)`.
- Space: `O(26)`, effectively `O(1)`.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- If no adjacent equals are allowed, check the max frequency bound first.
- A heap is useful when the best next choice changes after every placement.

------------------------------------------------------------------------

## End of Notes
