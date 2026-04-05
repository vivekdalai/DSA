# Greedy Notes

## 04 - Split Array Largest Sum

**Generated on:** 2026-04-06 01:00:13 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an array `nums` and an integer `k`, split the array into exactly `k` non-empty continuous subarrays.

Return the minimum possible value of the largest subarray sum among those `k` parts.

**Example 1**

    Input: nums = [7, 2, 5, 10, 8], k = 2
    Output: 18

Explanation:

One optimal split is:

    [7, 2, 5] and [10, 8]

Their sums are:

    14 and 18

So the minimized largest sum is `18`.

**Example 2**

    Input: nums = [1, 2, 3, 4, 5], k = 2
    Output: 9

One optimal split is:

    [1, 2, 3] and [4, 5]

------------------------------------------------------------------------

## 2. The Real Search Space

We are not directly searching for the split positions.

We are searching for the answer itself:

    "What is the smallest value X such that the array can be split into at most k parts, and every part has sum <= X?"

That converts the problem into **binary search on answer**.

------------------------------------------------------------------------

## 3. Why The Range Is `[max(nums), sum(nums)]`

The largest subarray sum can never be smaller than the biggest element:

    lower bound = max(nums)

Because every element must belong to some subarray.

The largest subarray sum can always be as large as the total sum:

    upper bound = sum(nums)

That corresponds to taking the whole array as one part.

Your code stores these as:

    l = max element
    r = total sum

------------------------------------------------------------------------

## 4. Greedy Feasibility Check

For a fixed candidate limit `mid`, the helper `func(...)` counts how many subarrays are required if no subarray sum is allowed to exceed `mid`.

Greedy rule:

- Keep extending the current subarray while the sum stays `<= mid`
- As soon as adding the next element would cross `mid`, cut here and start a new subarray

This greedy makes each part as long as possible.

That is exactly what we want, because it minimizes the number of parts needed for that limit.

------------------------------------------------------------------------

## 5. Monotonic Property That Enables Binary Search

If a limit `mid` is feasible, then any larger limit is also feasible.

Why?

- A larger allowed maximum only makes splitting easier
- It can never require more subarrays

So:

- if required parts `<= k`, try smaller answers
- if required parts `> k`, the limit is too small

This monotonic behavior is the reason binary search is valid.

------------------------------------------------------------------------

## 6. One Important Detail: Why `<= k` Is Enough

The helper returns the minimum number of parts needed under a given limit.

If that number is smaller than `k`, the limit is still feasible because the array contains non-negative values.

We can always split some existing subarray further without increasing the largest subarray sum.

So the feasibility condition is:

    func(nums, n, mid, k) <= k

------------------------------------------------------------------------

## 7. Dry Run

**Input:**

    nums = [7, 2, 5, 10, 8]
    k = 2

Bounds:

    l = 10
    r = 32

### Try `mid = 21`

Greedy partition:

- `[7, 2, 5]` -> sum `14`
- add `10` would make `24`, so cut
- `[10, 8]` -> sum `18`

Parts needed:

    2

Feasible. Save `21`, search left half.

### Try `mid = 15`

Greedy partition:

- `[7, 2, 5]` -> `14`
- next `10` starts new part
- next `8` starts another part

Parts needed:

    3

Not feasible. Search right half.

### Try `mid = 18`

Greedy partition:

- `[7, 2, 5]` -> `14`
- `[10, 8]` -> `18`

Parts needed:

    2

Feasible. Save `18`.

Binary search finishes with:

    answer = 18

------------------------------------------------------------------------

## 8. Clean Interview Version

```java
public int splitArray(int[] nums, int k) {
    int n = nums.length, l = 0, r = 0, ans = 0;

    for (int i = 0; i < n; i++) {
        l = Math.max(l, nums[i]);
        r += nums[i];
    }

    while (l <= r) {
        int mid = (l + r) / 2;

        if (func(nums, n, mid, k) <= k) {
            ans = mid;
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }

    return ans;
}

public int func(int[] nums, int n, int mid, int k) {
    int sum = 0, cnt = 1;

    for (int i = 0; i < n; i++) {
        sum += nums[i];

        if (sum > mid) {
            sum = nums[i];
            cnt++;
        }
    }

    return cnt;
}
```

------------------------------------------------------------------------

## 9. Complexity

- **Time:** `O(n log(sum(nums) - max(nums) + 1))`
- **Space:** `O(1)`

Each binary-search step scans the array once.

------------------------------------------------------------------------

## 10. Pattern Recognition

This is a textbook **binary search on answer + greedy validation** problem.

Clues:

- The answer is numeric
- You need the minimum feasible value
- Feasibility becomes easier as the answer grows
- A linear scan can validate one candidate

Whenever those four signs appear together, check whether binary search on the answer space is possible.

------------------------------------------------------------------------

## End of Notes
