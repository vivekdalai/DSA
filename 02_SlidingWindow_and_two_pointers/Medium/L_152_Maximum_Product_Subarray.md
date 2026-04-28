# Two Pointers and Sliding Window Notes

## 152 - Maximum Product Subarray

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-product-subarray/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums`, find a subarray that has the largest product, and return the product.

The test cases are generated so that the answer will fit in a **32-bit** integer.

**Note** that the product of an array with a single element is the value of that element.

**Example 1:**

```text
Input: nums = [2,3,-2,4]
Output: 6
Explanation: [2,3] has the largest product 6.
```

**Example 2:**

```text
Input: nums = [-2,0,-1]
Output: 0
Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
```

**Constraints:**

- `1 <= nums.length <= 2 * 104`

- `-10 <= nums[i] <= 10`

- The product of any subarray of `nums` is **guaranteed** to fit in a **32-bit** integer.

------------------------------------------------------------------------

## 2. First Intuition

Negative numbers flip product signs, and zeros break subarrays. Scanning from both ends catches the best product whether the useful negative gets excluded from the left side or the right side.

------------------------------------------------------------------------

## 3. Prefix and Suffix Products

- The file keeps `prefixProd` from the left and `suffixProd` from the right.
- Whenever a running product becomes `0`, it resets to `1` before continuing.
- At each index, it updates the answer with the best of the current prefix and suffix products.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[2,3,-2,4]`: prefix products see `2`, `6`, `-12`, `-48`; suffix products help account for products from the right. The best value is `6` from `[2,3]`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxProduct(int[] nums) {
    int prefix = 1;
    int suffix = 1;
    int best = nums[0];
    int n = nums.length;

    for (int i = 0; i < n; i++) {
        if (prefix == 0) prefix = 1;
        if (suffix == 0) suffix = 1;

        prefix *= nums[i];
        suffix *= nums[n - 1 - i];
        best = Math.max(best, Math.max(prefix, suffix));
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Maximum product is harder than maximum sum because negatives can become useful.
- Bidirectional product scans handle odd counts of negative numbers.

------------------------------------------------------------------------

## End of Notes
