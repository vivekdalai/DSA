# Dynamic Programming Notes

## 01 - Maximum Sum Subarray (Kadane's Algorithm)

**Generated on:** 2026-02-24 09:55:27

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums`, find the **contiguous subarray**\
(with at least one element) which has the **largest sum**.

This is a **1D Dynamic Programming** problem.

Core Decision at every index:

-   Start a new subarray from here\
    OR
-   Extend the previous subarray

------------------------------------------------------------------------

## 🪜 2. State Definition

    dp[i] = Maximum sum of subarray ending at index i

We only care about subarrays that **END at i**.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

    dp[i] = max(nums[i], nums[i] + dp[i-1])

Meaning:

-   Either start fresh from `nums[i]`
-   Or extend previous best subarray

------------------------------------------------------------------------

## 🧱 4. Base Case

    dp[0] = nums[0]

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Since `dp[i]` depends only on `dp[i-1]`,\
we do NOT need a full array.

Use two variables:

-   `maxEndingHere` → represents dp\[i\]
-   `maxSoFar` → stores global maximum

### Complexity

-   **Time:** O(N)
-   **Space:** O(1)

------------------------------------------------------------------------

## 💻 6. Clean Interview Version (Optimal Code)

``` java
public int maxSubArray(int[] nums) {
    int maxEndingHere = nums[0];
    int maxSoFar = nums[0];

    for (int i = 1; i < nums.length; i++) {
        maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }

    return maxSoFar;
}
```

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

**Input:**

    nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]

Step-by-step:

  Index   Value   maxEndingHere   maxSoFar
  ------- ------- --------------- ----------
  0       -2      -2              -2
  1       1       1               1
  2       -3      -2              1
  3       4       4               4
  4       -1      3               4
  5       2       5               5
  6       1       6               6
  7       -5      1               6
  8       4       5               6

**Final Answer:** `6`\
Subarray = `[4, -1, 2, 1]`

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

**Pattern Name:** Extend or Restart\
**Family:** Kadane / 1D DP

Whenever you see:

-   Maximum / Minimum subarray
-   Contiguous segment
-   Optimal solution
-   Choice: extend previous OR start new

👉 Think Kadane.

------------------------------------------------------------------------

## 🔄 9. Common Variations

1.  **Return actual subarray**\
    → Track start and end indices.

2.  **Circular array**\
    → `max(normalKadane, totalSum - minSubarray)`

3.  **Maximum Product Subarray**\
    → Track both `maxEndingHere` and `minEndingHere`.

------------------------------------------------------------------------

## End of Notes
