# Dynamic Programming Notes

## 02 - Maximum Product Subarray

**Generated on:** 2026-02-24 11:13:33

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums`, find the contiguous subarray\
(with at least one element) which has the **largest product**.

This looks similar to Kadane's Algorithm (Maximum Sum Subarray).\
BUT multiplication introduces a twist:

👉 A negative × negative = positive

So we must track both:

-   Maximum product ending at index i
-   Minimum product ending at index i

------------------------------------------------------------------------

## 🪜 2. State Definition

    maxEndingHere = maximum product of subarray ending at i
    minEndingHere = minimum product of subarray ending at i
    maxSoFar      = global maximum product

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For every number `num`:

    tempMax = max(num, maxEndingHere * num, minEndingHere * num)
    tempMin = min(num, maxEndingHere * num, minEndingHere * num)

Then update:

    maxEndingHere = tempMax
    minEndingHere = tempMin
    maxSoFar = max(maxSoFar, maxEndingHere)

------------------------------------------------------------------------

## 🧱 4. Base Case

    maxEndingHere = nums[0]
    minEndingHere = nums[0]
    maxSoFar      = nums[0]

------------------------------------------------------------------------

## 📦 5. Why Do We Need minEndingHere?

Example:

    nums = [-2, 3, -4]

Step 1: - maxEndingHere = -2 - minEndingHere = -2

Step 2 (3): - maxEndingHere = 3 - minEndingHere = -6

Step 3 (-4): - maxEndingHere = 24 (because -6 × -4 = 24) - minEndingHere
= -12

Final Answer = 24

Without tracking minimum, we would miss this.

------------------------------------------------------------------------

## 💻 6. Clean Interview Version (Optimal Code)

``` java
public int maxProduct(int[] nums) {
    int maxSoFar = nums[0];
    int maxEndingHere = nums[0];
    int minEndingHere = nums[0];

    for (int i = 1; i < nums.length; i++) {
        int num = nums[i];

        int tempMax = Math.max(num, 
                        Math.max(maxEndingHere * num, 
                                 minEndingHere * num));

        int tempMin = Math.min(num, 
                        Math.min(maxEndingHere * num, 
                                 minEndingHere * num));

        maxEndingHere = tempMax;
        minEndingHere = tempMin;

        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }

    return maxSoFar;
}
```

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:

    nums = [2, 3, -2, 4]

  Index   Value   maxEndingHere   minEndingHere   maxSoFar
  ------- ------- --------------- --------------- ----------
  0       2       2               2               2
  1       3       6               3               6
  2       -2      -2              -12             6
  3       4       4               -48             6

Final Answer = 6\
Subarray = \[2, 3\]

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

Pattern Name: Kadane with Dual Tracking\
Family: 1D DP -- Contiguous Subarray

Whenever you see:

-   Maximum product subarray
-   Negative numbers involved
-   Multiplication instead of addition

👉 Think: Track BOTH max and min.

------------------------------------------------------------------------

## 🔄 9. Edge Cases to Remember

1.  Single element array\
2.  All negative numbers\
3.  Zeros reset product sequence\
4.  Very large product values (overflow in some languages)

------------------------------------------------------------------------

# End of Notes
