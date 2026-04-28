# Two Pointers Notes

## 18 - 4Sum

**Generated on:** 2026-04-09 00:24:24 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/4sum/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Given an integer array `nums` and an integer `target`, return all **unique** quadruplets
`[nums[a], nums[b], nums[c], nums[d]]` such that:

- `a`, `b`, `c`, and `d` are distinct indices
- `nums[a] + nums[b] + nums[c] + nums[d] == target`

The answer can be returned in any order.

**Example 1**

```text
Input: nums = [1,0,-1,0,-2,2], target = 0
Output: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
Explanation: After sorting, each valid quadruplet is discovered by fixing two values and
using two pointers on the remaining suffix.
```

**Example 2**

```text
Input: nums = [2,2,2,2,2], target = 8
Output: [[2,2,2,2]]
Explanation: Even though the array has many equal values, duplicate quadruplets are not allowed.
```

**Constraints**

- `1 <= nums.length <= 200`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`

------------------------------------------------------------------------

## 2. Intuition: Reduce 4Sum To Repeated 2Sum

Brute force would try all 4-index combinations, which is `O(n^4)`.

The file uses the standard improvement:

- sort the array first
- fix the first number `i`
- fix the second number `j`
- solve the remaining `2Sum` on the sorted suffix using two pointers

Once the array is sorted, pointer movement becomes predictable:

- if the current sum is too small, move the left pointer right
- if the current sum is too large, move the right pointer left

That cuts one full loop and brings the main work down to `O(n^3)`.

------------------------------------------------------------------------

## 3. What The Actual Java Code Is Doing

The implementation in `fourSum(int[] a, int target)` is a clean
`two anchors + two pointers` solution.

### Step 1: sort first

```java
Arrays.sort(a);
```

Sorting is what makes duplicate skipping and pointer movement possible.

### Step 2: choose the first two positions

- `i` runs from left to right and skips duplicate anchors with
  `if (i > 0 && a[i] == a[i - 1]) continue;`
- `j` runs from `i + 1` onward and also skips duplicate second anchors with
  `if (j > i + 1 && a[j] == a[j - 1]) continue;`

This is how the code avoids producing the same quadruplet multiple times.

### Step 3: use two pointers for the remaining pair

For every fixed `(i, j)`:

- `k = j + 1`
- `l = n - 1`

Now compare:

```java
long sum = (long) a[i] + a[j] + a[k] + a[l];
```

Using `long` is important here because four `int` values near `10^9` can overflow an `int`.

- if `sum == target`, record the quadruplet
- if `sum > target`, move `l--`
- otherwise move `k++`

After a match, the code moves both pointers inward and skips repeated values near `k` and `l`.
That keeps the output unique without using a `Set`.

------------------------------------------------------------------------

## 4. Short Walkthrough

Take:

```text
nums = [1,0,-1,0,-2,2], target = 0
```

After sorting:

```text
[-2,-1,0,0,1,2]
```

- `i = 0` gives `-2`
- `j = 1` gives `-1`
- two pointers start at `0` and `2`
- the first useful hit becomes `[-2,-1,1,2]`

Still with `i = 0`:

- move `j` to the first `0`
- now the two-pointer scan finds `[-2,0,0,2]`

Then:

- `i = 1` gives `-1`
- `j = 2` gives `0`
- the pair search finds `[-1,0,0,1]`

Those are exactly the three unique answers.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static List<List<Integer>> fourSum(int[] nums, int target) {
    Arrays.sort(nums);
    int n = nums.length;
    List<List<Integer>> ans = new ArrayList<>();

    for (int i = 0; i < n - 3; i++) {
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }

        for (int j = i + 1; j < n - 2; j++) {
            if (j > i + 1 && nums[j] == nums[j - 1]) {
                continue;
            }

            int left = j + 1;
            int right = n - 1;

            while (left < right) {
                long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];

                if (sum == target) {
                    ans.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                    left++;
                    right--;

                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n^3)` after sorting
- **Space:** `O(1)` extra, excluding the answer list

If you count the sorting stack for primitive-array quicksort, it is effectively `O(log n)` auxiliary space.

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- This is the direct `4Sum` extension of `3Sum`: sort, freeze more anchors, then finish with two pointers.
- The real pattern is `k-Sum -> reduce to (k-1)-Sum -> eventually solve 2Sum on a sorted array`.
- Duplicate handling must happen at every decision level: `i`, `j`, and after each successful pair.
- Large-value combination problems should immediately trigger an overflow check. Here, `long sum` is the safe choice.

------------------------------------------------------------------------

## End of Notes
