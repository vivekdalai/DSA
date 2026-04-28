# Two Pointers Notes

## 15 - 3Sum

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/3sum/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Return all unique triplets whose sum is `0`.
Indices must be distinct, and the output must not contain duplicate triplets even if the input has repeated values.

**Example 1**

```text
Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
Explanation: After sorting, both valid zero-sum triplets can be found with one fixed index plus a two-pointer scan.
```

**Example 2**

```text
Input: nums = [0,1,1]
Output: []
Explanation: No three numbers sum to zero.
```

**Example 3**

```text
Input: nums = [0,0,0]
Output: [[0,0,0]]
Explanation: Only one distinct triplet is allowed in the answer.
```

**Constraints**

- `3 <= nums.length <= 3000`
- `-10^5 <= nums[i] <= 10^5`

------------------------------------------------------------------------

## 2. Sort First, Then Freeze One Number

Once the array is sorted, fixing `a[i]` turns the rest of the problem into a sorted 2-sum search on the suffix.

- If the current triplet sum is too small, move `j` right to increase it.
- If the sum is too large, move `k` left to decrease it.

Sorting also gives a clean way to skip duplicates so the answer set stays unique.

------------------------------------------------------------------------

## 3. Duplicate Handling In The Actual Code

The file follows the standard structure:

- sort the array
- loop `i` from left to right
- skip duplicate anchors with `if (i > 0 && a[i] == a[i - 1]) continue`
- run two pointers `j = i + 1` and `k = n - 1`

On a hit, the code adds `[a[i], a[j], a[k]]`, then skips repeated values beside both pointers before moving inward.

Those duplicate-skipping `while` loops are the key detail. Without them, the same value combination would be added multiple times.

------------------------------------------------------------------------

## 4. Walkthrough on `[-1,0,1,2,-1,-4]`

After sorting, the array becomes `[-4, -1, -1, 0, 1, 2]`.

- Anchor `-4`: even the best pair on the right cannot make `0`, so no triplet here
- Anchor first `-1`: two pointers find `[-1, -1, 2]`, then later `[-1, 0, 1]`
- Anchor second `-1`: skipped because it would reproduce the same triplets
- Remaining anchors do not create new zero-sum triplets

Final answer: `[[-1, -1, 2], [-1, 0, 1]]`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static List<List<Integer>> threeSum(int[] nums) {
    Arrays.sort(nums);
    List<List<Integer>> ans = new ArrayList<>();
    int n = nums.length;

    for (int i = 0; i < n; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }

        int left = i + 1;
        int right = n - 1;

        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];

            if (sum == 0) {
                ans.add(Arrays.asList(nums[i], nums[left], nums[right]));
                left++;
                right--;

                while (left < right && nums[left] == nums[left - 1]) {
                    left++;
                }
                while (left < right && nums[right] == nums[right + 1]) {
                    right--;
                }
            } else if (sum < 0) {
                left++;
            } else {
                right--;
            }
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n^2)`
- **Space:** `O(1) extra, excluding the output list`

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- The shape is `sort + fixed anchor + two-pointer sweep`.
- Whenever a problem asks for unique combinations in a sorted setting, duplicate skipping is usually the make-or-break detail.
- This is the standard reduction from `3Sum` to repeated `2Sum` on a sorted suffix.

------------------------------------------------------------------------

## End of Notes
