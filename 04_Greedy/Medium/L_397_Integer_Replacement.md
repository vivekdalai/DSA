# Greedy Notes

## Integer Replacement

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given a positive integer `n`, you may do:

- if `n` is even, replace it with `n / 2`
- if `n` is odd, replace it with either `n + 1` or `n - 1`

Return the minimum number of replacements needed to make `n = 1`.

**Example 1**

    Input: n = 8
    Output: 3

Because:

    8 -> 4 -> 2 -> 1

**Example 2**

    Input: n = 7
    Output: 4

------------------------------------------------------------------------

## 2. File Strategy

The file explores both options for odd numbers:

- `n + 1`
- `n - 1`

and uses memoization to keep the recursion efficient.

That is the safest way to reason about correctness when revising this problem.

------------------------------------------------------------------------

## 3. Why `long` Helps

If `n = Integer.MAX_VALUE`, then `n + 1` would overflow an `int`.

That is why the memoized helper uses `long`.

This detail is easy to forget and is one of the main traps in this problem.

------------------------------------------------------------------------

## 4. Transition

If `n` is even:

    answer = 1 + helper(n / 2)

If `n` is odd:

    answer = 1 + min(helper(n - 1), helper(n + 1))

Memoization stores the minimum steps for each visited value.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int integerReplacementWithMemoization(int n) {
    HashMap<Long, Integer> map = new HashMap<>();
    return helper(n, map);
}

public static int helper(long n, HashMap<Long, Integer> map) {
    if (n == 1) return 0;
    if (map.containsKey(n)) return map.get(n);

    int ans;
    if (n % 2 == 0) {
        ans = 1 + helper(n / 2, map);
    } else {
        ans = 1 + Math.min(helper(n - 1, map), helper(n + 1, map));
    }

    map.put(n, ans);
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** effectively near `O(log n)` states with memoization in practice
- **Space:** `O(log n)` recursion depth plus memo storage

Pattern:

- recursion on number states
- memoize both branches for odd values

------------------------------------------------------------------------

## End of Notes
