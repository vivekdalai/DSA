# Greedy Notes

## Largest Number

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/largest-number/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a list of non-negative integers `nums`, arrange them so they form the largest possible number.

Return the result as a string.

**Example 1**

    Input: nums = [10, 2]
    Output: "210"

**Example 2**

    Input: nums = [3, 30, 34, 5, 9]
    Output: "9534330"

------------------------------------------------------------------------

## 2. Why Normal Sorting Fails

Comparing numbers by numeric value is not enough.

Example:

    9 and 34

Numeric order says `34` is larger, but for concatenation:

    "934" > "349"

So the correct comparison is:

    a before b if (a + b) > (b + a)

------------------------------------------------------------------------

## 3. Comparator Intuition

Convert all numbers to strings.

Sort using:

    (b + a).compareTo(a + b)

That places the better leading choice first.

One special case remains:

- if the largest string after sorting is `"0"`, then the whole answer is just `"0"`

That avoids answers like `"0000"`.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [3, 30, 34, 5, 9]

Important comparisons:

- `9` should come before everyone
- `5` should come before `34`
- `34` should come before `3`
- `3` should come before `30`

Sorted order:

    ["9", "5", "34", "3", "30"]

Join them:

    "9534330"

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static String largestNumber(int[] nums) {
    String[] arr = new String[nums.length];

    for (int i = 0; i < nums.length; i++) {
        arr[i] = String.valueOf(nums[i]);
    }

    Arrays.sort(arr, (a, b) -> (b + a).compareTo(a + b));

    if (arr[0].equals("0")) return "0";

    StringBuilder sb = new StringBuilder();
    for (String s : arr) sb.append(s);
    return sb.toString();
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n * k)` where `k` is average string length
- **Space:** `O(n)`

Pattern:

- custom comparator based on concatenation order

------------------------------------------------------------------------

## End of Notes
