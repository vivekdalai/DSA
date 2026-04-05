# Greedy Notes

## Rabbits In Forest

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/rabbits-in-forest/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Each rabbit answers how many other rabbits have the same color as itself.

If a rabbit answers `x`, then it belongs to a color group of size:

    x + 1

Given the array `answers`, return the minimum number of rabbits that could be in the forest.

**Example 1**

    Input: answers = [1,1,2]
    Output: 5

**Example 2**

    Input: answers = [10,10,10]
    Output: 11

------------------------------------------------------------------------

## 2. Grouping Intuition

If rabbits say `x`, they must be arranged in groups of size `x + 1`.

So if `freq[x]` rabbits gave answer `x`, they need:

    ceil(freq[x] / (x + 1))

groups, each contributing `x + 1` rabbits.

That is exactly what the file computes.

------------------------------------------------------------------------

## 3. Why Partial Groups Still Count Full Size

Suppose `x = 2`, so each color group size is `3`.

If only `4` rabbits gave answer `2`, we cannot place them in one full group plus one group of size `1`.

The second color still needs capacity for `3` rabbits of that color, even if not all were explicitly seen in the input.

So the second partial group still contributes a full `3`.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    answers = [1,1,2]

Counts:

- answer `1` appears `2` times -> group size `2`, need `1` group -> contributes `2`
- answer `2` appears `1` time  -> group size `3`, need `1` group -> contributes `3`

Total:

    5

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int numRabbits(int[] answers) {
    int[] freq = new int[1001];

    for (int x : answers) {
        freq[x]++;
    }

    int ans = 0;
    for (int i = 0; i < 1001; i++) {
        if (freq[i] > 0) {
            ans += (int) Math.ceil(freq[i] / (double) (i + 1)) * (i + 1);
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n + K)` where `K = 1001`
- **Space:** `O(K)`

Pattern:

- count identical answers
- pack them into fixed-size groups using ceiling

------------------------------------------------------------------------

## End of Notes
