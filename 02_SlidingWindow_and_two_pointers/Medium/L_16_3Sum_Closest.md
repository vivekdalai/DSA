# Two Pointers Notes

## 16 - 3Sum Closest

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/3sum-closest/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Choose three distinct indices so that their sum is as close as possible to `target`, then return that sum.
There is exactly one best answer for every valid input.

**Example 1**

```text
Input: nums = [-1,2,1,-4], target = 1
Output: 2
Explanation: The closest triplet sum is `(-1 + 2 + 1) = 2`.
```

**Example 2**

```text
Input: nums = [0,0,0], target = 1
Output: 0
Explanation: Only one triplet exists, so it is automatically the closest.
```

**Constraints**

- `3 <= nums.length <= 500`
- `-1000 <= nums[i] <= 1000`
- `-10^4 <= target <= 10^4`

------------------------------------------------------------------------

## 2. Closest Sum, Not Exact Sum

This is structurally close to `3Sum`, but the goal changes everything.

You do not stop at valid combinations of a specific value. Every triplet is a candidate, and you keep the one with the smallest absolute difference from `target`.

Sorting still helps because the two-pointer movement remains monotonic:

- sum too large -> move right pointer left
- sum too small -> move left pointer right

------------------------------------------------------------------------

## 3. How The File Tracks The Best Answer

The code sorts the array, then for each anchor `i` runs a two-pointer scan on the suffix.

It stores:

- `ans`: the best sum seen so far
- `minDiff`: the smallest `abs(sum - target)` seen so far

For every triplet:

- compute `sum`
- compute `diff`
- if `diff < minDiff`, update both `ans` and `minDiff`
- if `sum == target`, return immediately because exact match is unbeatable

Duplicate anchors are skipped just to avoid repeated work. Unlike `3Sum`, duplicate triplets in the output are not a concern here because the output is only one integer.

------------------------------------------------------------------------

## 4. Walkthrough on `[-1,2,1,-4]`, target `1`

Sort first: `[-4, -1, 1, 2]`.

- Anchor `-4`, pair `(-1, 2)` gives sum `-3`, diff `4`
- Move left pointer right, pair `(1, 2)` gives sum `-1`, diff `2`, so best becomes `-1`
- Anchor `-1`, pair `(1, 2)` gives sum `2`, diff `1`, so best becomes `2`

No exact hit appears, so the answer remains `2`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int threeSumClosest(int[] nums, int target) {
    Arrays.sort(nums);
    int n = nums.length;
    int ans = 0;
    int minDiff = Integer.MAX_VALUE;

    for (int i = 0; i < n; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }

        int left = i + 1;
        int right = n - 1;

        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];
            int diff = Math.abs(sum - target);

            if (diff < minDiff) {
                minDiff = diff;
                ans = sum;
            }

            if (sum == target) {
                return sum;
            } else if (sum > target) {
                right--;
            } else {
                left++;
            }
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n^2)`
- **Space:** `O(1) extra`

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- If the array can be sorted and the score changes monotonically with sum, two pointers are usually available.
- For `closest` problems, store the best score seen so far and update on every candidate.
- Exact hit means immediate return because absolute difference `0` is optimal.

------------------------------------------------------------------------

## End of Notes
