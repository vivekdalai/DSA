# Greedy Notes

## 14 - Can Place Flowers

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a flowerbed array where:

- `0` means empty
- `1` means already planted

You cannot plant flowers in adjacent plots.

Given an integer `n`, return `true` if `n` new flowers can be planted, otherwise return `false`.

**Example 1**

    Input: flowerbed = [1, 0, 0, 0, 1], n = 1
    Output: true

**Example 2**

    Input: flowerbed = [1, 0, 0, 0, 1], n = 2
    Output: false

------------------------------------------------------------------------

## 2. Local Greedy Decision

At any index, a flower can be planted only if:

- the current plot is empty
- the left neighbor is empty or out of bounds
- the right neighbor is empty or out of bounds

If planting is possible, the greedy choice is to plant immediately.

Why?

Because planting now can only help the count, and waiting gives no advantage.

------------------------------------------------------------------------

## 3. One Pass Strategy

Scan the array from left to right.

For each `0` cell:

- read `left`
- read `right`
- if both are `0`, plant here and increment the count

The file mutates the array to mark newly planted flowers, which keeps later checks correct.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    flowerbed = [1, 0, 0, 0, 1], n = 1

Check each position:

- index `1`: left is `1`, cannot plant
- index `2`: left is `0`, right is `0`, plant here

Now count is `1`, so return `true`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean canPlaceFlowers(int[] flowerbed, int n) {
    int cnt = 0;

    for (int i = 0; i < flowerbed.length; i++) {
        if (flowerbed[i] == 0) {
            int left = (i == 0) ? 0 : flowerbed[i - 1];
            int right = (i == flowerbed.length - 1) ? 0 : flowerbed[i + 1];

            if (left == 0 && right == 0) {
                flowerbed[i] = 1;
                cnt++;
            }
        }

        if (cnt >= n) return true;
    }

    return false;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- local feasibility check
- greedy placement as soon as a slot is safe

------------------------------------------------------------------------

## End of Notes
