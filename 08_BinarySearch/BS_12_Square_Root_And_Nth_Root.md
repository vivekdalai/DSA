# Binary Search Notes

## 12 - Square Root and Nth Root

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/sqrtx/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

For square root, given a non-negative integer `x`, return the floor value of `sqrt(x)`.

Example:

```text
Input: x = 8
Output: 2
```

For nth root, given `n` and `m`, find integer `x` such that:

```text
x ^ n = m
```

If no integer root exists, return `-1`.

------------------------------------------------------------------------

## 2. First Intuition

The answer lies in a numeric range.

For square root:

```text
0 <= answer <= x
```

If `mid * mid <= x`, then `mid` is possible and we can try bigger.
If `mid * mid > x`, then `mid` is too large.

This is binary search on answer.

------------------------------------------------------------------------

## 3. Methodology

For square root:

- Binary search from `1` to `x`.
- Keep the largest `mid` whose square is `<= x`.
- Use division or `long` to avoid overflow.

For nth root:

- Binary search from `1` to `m`.
- Compare `mid ^ n` with `m`.
- Stop multiplication early if product exceeds `m`.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int mySqrt(int x) {
        if (x < 2) {
            return x;
        }

        int low = 1;
        int high = x / 2;
        int ans = 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if ((long) mid * mid <= x) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return ans;
    }
}
```

Nth root helper:

```java
class NthRoot {
    public int nthRoot(int n, int m) {
        int low = 1;
        int high = m;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = comparePower(mid, n, m);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    private int comparePower(int base, int power, int target) {
        long result = 1;

        for (int i = 0; i < power; i++) {
            result *= base;
            if (result > target) {
                return 1;
            }
        }

        if (result == target) {
            return 0;
        }
        return -1;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `x = 8`:

- Try `4`: `16 > 8`, too large.
- Try `2`: `4 <= 8`, possible.
- Try `3`: `9 > 8`, too large.
- Answer is `2`.

------------------------------------------------------------------------

## 6. Complexity

- Square root time: `O(log x)`
- Nth root time: `O(log m * n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Use this when the answer is numeric and feasibility changes at a point.
- Avoid overflow in multiplication.
- Floor answer means save the possible value and keep trying bigger.

------------------------------------------------------------------------

## End of Notes
