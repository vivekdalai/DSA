# Greedy Notes

## 12 - Assign Cookies

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You have:

- `g[i]` = greed factor of child `i`
- `s[j]` = size of cookie `j`

A child is content if assigned one cookie with size at least its greed factor.

Each child can get at most one cookie, and each cookie can be used once.

Return the maximum number of content children.

**Example 1**

    Input: g = [1, 2, 3], s = [1, 1]
    Output: 1

**Example 2**

    Input: g = [1, 2], s = [1, 2, 3]
    Output: 2

------------------------------------------------------------------------

## 2. Greedy Matching Idea

The smallest cookie that can satisfy the least greedy remaining child should be used there.

Why?

Giving a large cookie to a small greed too early may waste a resource that a greedier child needs.

So we sort both arrays and match from smallest upward.

------------------------------------------------------------------------

## 3. Two-Pointer Rule

After sorting:

- `i` points to the current child
- `j` points to the current cookie

If:

    s[j] >= g[i]

then this cookie can satisfy the child:

- count one answer
- move to the next child

In either case, the current cookie is consumed, so `j` moves forward.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    g = [1, 2, 3]
    s = [1, 1]

Sorted arrays stay the same.

Matching:

- cookie `1` satisfies greed `1`
- next cookie `1` cannot satisfy greed `2`

Total content children:

    1

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int findContentChildren(int[] g, int[] s) {
    Arrays.sort(g);
    Arrays.sort(s);

    int cnt = 0, i = 0, j = 0;

    while (i < g.length && j < s.length) {
        if (g[i] <= s[j]) {
            cnt++;
            i++;
        }
        j++;
    }

    return cnt;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(m log m + n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort demand and supply
- match the smallest feasible supply first

------------------------------------------------------------------------

## End of Notes
