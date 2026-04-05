# Greedy Notes

## Partition Labels

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/partition-labels/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s`, partition it into as many parts as possible so that each letter appears in at most one part.

Return a list of the sizes of those parts.

**Example 1**

    Input: s = "ababcbacadefegdehijhklij"
    Output: [9,7,8]

**Example 2**

    Input: s = "eccbbbbdec"
    Output: [10]

------------------------------------------------------------------------

## 2. Last Occurrence Intuition

If a character appears again later, the current partition cannot end before that last occurrence.

So while scanning a partition, keep extending its right boundary to cover the farthest last occurrence of every character seen so far.

When the current index reaches that boundary, the partition is complete.

------------------------------------------------------------------------

## 3. Cleaner Version In The File

The `v2` method is the easiest one to revise.

Steps:

- precompute `lastSeen[ch]`
- scan with `start` and `end`
- update:

    end = max(end, lastSeen[currentChar])

- when `i == end`, push `end - start + 1`

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    s = "ababcbacadefegdehijhklij"

Start at index `0`.

The early letters force the first partition to extend up to index `8`.

When the scan reaches `8`, all characters seen so far end within that range.

So the first block length is:

    9

Then repeat for the remaining suffix.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static List<Integer> partitionLabels_v2(String s) {
    int[] lastSeen = new int[26];

    for (int i = 0; i < s.length(); i++) {
        lastSeen[s.charAt(i) - 'a'] = i;
    }

    int start = 0, end = 0;
    List<Integer> list = new ArrayList<>();

    for (int i = 0; i < s.length(); i++) {
        end = Math.max(end, lastSeen[s.charAt(i) - 'a']);

        if (i == end) {
            list.add(end - start + 1);
            start = end + 1;
        }
    }

    return list;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)` for fixed alphabet

Pattern:

- precompute last occurrence
- grow each segment until all its characters are fully contained

------------------------------------------------------------------------

## End of Notes
