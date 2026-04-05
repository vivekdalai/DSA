# Greedy Notes

## Broken Calculator

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/broken-calculator/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

On a broken calculator, you can do only two operations:

- multiply the current number by `2`
- subtract `1`

Starting from `startValue`, return the minimum number of operations needed to reach `target`.

**Example 1**

    Input: startValue = 2, target = 3
    Output: 2

Because:

    2 -> 4 -> 3

**Example 2**

    Input: startValue = 5, target = 8
    Output: 2

Because:

    5 -> 4 -> 8

------------------------------------------------------------------------

## 2. Reverse Greedy Insight

Going forward is awkward because:

- doubling can overshoot
- subtracting `1` has many possibilities

Going backward from `target` is much cleaner.

Reverse operations are:

- if target is even, divide by `2`
- if target is odd, add `1`

This greedy is optimal because:

- an even target should have come from halving
- an odd target cannot have come directly from doubling, so it must first become even

------------------------------------------------------------------------

## 3. Loop Logic

While:

    target > startValue

do:

- if `target` is even, `target /= 2`
- else `target++`
- increment operation count

When `target <= startValue`, the remaining work is simple:

    startValue - target

using forward `-1` operations in reverse.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    startValue = 3, target = 10

Reverse from `10`:

- `10` even -> `5`
- `5` odd  -> `6`
- `6` even -> `3`

Operations:

    3

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int brokenCalc(int startValue, int target) {
    int cnt = 0;

    while (target > startValue) {
        cnt++;
        if (target % 2 == 0) target /= 2;
        else target++;
    }

    return cnt + startValue - target;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(log target)`
- **Space:** `O(1)`

Pattern:

- reverse the process
- greedily reduce the target toward the start value

------------------------------------------------------------------------

## End of Notes
