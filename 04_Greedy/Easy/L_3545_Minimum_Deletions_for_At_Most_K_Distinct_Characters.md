# Greedy Notes

## 10 - Minimum Deletions for At Most K Distinct Characters

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a string `s` and an integer `k`.

Delete the minimum number of characters so that the remaining string has at most `k` distinct characters.

Return that minimum deletion count.

**Example 1**

    Input: s = "abc", k = 2
    Output: 1

Explanation:

- Keep any two distinct characters
- Delete the remaining one

**Example 2**

    Input: s = "aaabbbcc", k = 2
    Output: 2

One optimal choice is to delete both `c` characters.

------------------------------------------------------------------------

## 2. Frequency-Based Intuition

If we want at most `k` distinct characters, we should keep the character types that appear most often.

Why?

Deleting a frequent character is expensive.

Deleting a rare character is cheap.

So the greedy target is:

- keep the `k` largest frequencies
- delete the rest

------------------------------------------------------------------------

## 3. Logic Used In The File

The method:

- counts frequency of each lowercase letter
- sorts the `freq` array
- sums the smallest `26 - k` frequencies

That total is the minimum number of deletions.

Even zero frequencies are present in the array, but they add nothing.

------------------------------------------------------------------------

## 4. Why Sorting Works

Once the frequencies are known, the problem is no longer about positions in the string.

It becomes:

    choose at most k buckets to keep

To minimize deletions, we must preserve the largest buckets and delete the smallest ones.

That is exactly what sorting gives us.

------------------------------------------------------------------------

## 5. Quick Walkthrough

**Input:**

    s = "aaabbbcc", k = 2

Frequencies:

    a -> 3
    b -> 3
    c -> 2

Keep the largest `2`:

    3 and 3

Delete the rest:

    2

Answer:

    2

------------------------------------------------------------------------

## 6. Clean Interview Version

```java
public int minDeletion(String s, int k) {
    int[] freq = new int[26];

    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }

    Arrays.sort(freq);

    int ans = 0;
    for (int i = 0; i < 26 - k; i++) {
        ans += freq[i];
    }

    return ans;
}
```

------------------------------------------------------------------------

## 7. Complexity And Pattern

- **Time:** `O(n + 26 log 26)`
- **Space:** `O(26)`

Pattern:

- frequency counting
- keep the heaviest buckets
- delete the cheapest character types first

------------------------------------------------------------------------

## End of Notes
