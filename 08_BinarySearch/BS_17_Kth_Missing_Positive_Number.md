# Binary Search Notes

## 17 - Kth Missing Positive Number

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/kth-missing-positive-number/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given a sorted positive integer array `arr` and an integer `k`, return the `k`th missing positive number.

Example:

```text
Input: arr = [2,3,4,7,11], k = 5
Output: 9
```

Missing positives are `[1,5,6,8,9,10,...]`.

------------------------------------------------------------------------

## 2. First Intuition

At index `i`, if no numbers were missing, the value should be:

```text
i + 1
```

So the count of missing numbers before `arr[i]` is:

```text
arr[i] - (i + 1)
```

This missing count increases as index increases, so we can binary search.

------------------------------------------------------------------------

## 3. Methodology

Find the first index where:

```text
missingCount(index) >= k
```

After binary search:

- `high` points to the last index where missing count is `< k`
- answer is `high + 1 + k`

Why?

If `high` valid numbers are before the answer, then the kth missing number shifts by that many present numbers.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int findKthPositive(int[] arr, int k) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int missing = arr[mid] - (mid + 1);

            if (missing < k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return low + k;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `arr = [2,3,4,7,11]`, `k = 5`:

Missing counts:

```text
index 0: 2 - 1 = 1
index 1: 3 - 2 = 1
index 2: 4 - 3 = 1
index 3: 7 - 4 = 3
index 4: 11 - 5 = 6
```

First index with missing count at least `5` is index `4`.
After search, `low = 4`, so answer is:

```text
low + k = 4 + 5 = 9
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on a derived count.
- The formula `arr[i] - (i + 1)` is the key.
- Good interview problem because it tests whether you can binary search on an invisible property.

------------------------------------------------------------------------

## End of Notes
