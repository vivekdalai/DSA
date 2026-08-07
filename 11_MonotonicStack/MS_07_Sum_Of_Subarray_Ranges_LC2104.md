# Monotonic Stack Notes

## 07 - Sum of Subarray Ranges (LC 2104)

Problem: The "range" of a subarray is `max(subarray) - min(subarray)`. Given an array `nums`, return the sum of ranges of all its contiguous subarrays.

---

## 1. Problem Understanding

Example:

```text
nums = [1,2,3]
answer = 4
```

Subarrays and their ranges:

```text
[1]     -> 0
[2]     -> 0
[3]     -> 0
[1,2]   -> 1
[2,3]   -> 1
[1,2,3] -> 2
```

Sum: `0+0+0+1+1+2 = 4`.

Since `range = max - min`, and sum is linear:

```text
sum(range) = sum(max over all subarrays) - sum(min over all subarrays)
```

This means the problem is literally [[MS_06_Sum_Of_Subarray_Minimums_LC907]] computed twice — once for max, once for min — and subtracted.

---

## 2. Core Intuition

Reuse the exact "contribution counting" idea from Sum of Subarray Minimums:

```text
sumOfSubarrayMax = sum over i of nums[i] * (subarrays where nums[i] is the max)
sumOfSubarrayMin = sum over i of nums[i] * (subarrays where nums[i] is the min)
answer = sumOfSubarrayMax - sumOfSubarrayMin
```

For the **max** version, flip the comparisons: find previous/next *greater* elements instead of smaller ones (with the same strict/non-strict split to avoid double-counting duplicates).

---

## 3. Why Monotonic Stack (Twice)

Both `calMaxSum` and `calMinSum` are independent applications of the same monotonic-stack template used in Sum of Subarray Minimums:

```text
For MIN:
  leftBoundary  = previous element with value <  curr   (pop while stack.top() >= curr)
  rightBoundary = next element with value     <= curr   (pop while stack.top() > curr)

For MAX:
  leftBoundary  = previous element with value >  curr   (pop while stack.top() <= curr)
  rightBoundary = next element with value     >= curr   (pop while stack.top() < curr)
```

The strict side (left) vs. non-strict side (right) assignment just needs to be **consistent** — it doesn't matter which side you make strict, as long as exactly one side is strict per direction, so ties aren't double-counted.

---

## 4. Java Implementation

```java
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public long subArrayRanges(int[] nums) {
        return calMaxSum(nums) - calMinSum(nums);
    }

    private long calMaxSum(int[] nums) {
        int size = nums.length;
        int[] leftGreater = new int[size];
        int[] rightGreater = new int[size];

        Deque<Integer> st = new ArrayDeque<>();

        // previous strictly-or-equal greater element (index)
        for (int i = 0; i < size; i++) {
            while (!st.isEmpty() && nums[st.peekFirst()] <= nums[i]) {
                st.pollFirst();
            }
            leftGreater[i] = st.isEmpty() ? -1 : st.peekFirst();
            st.addFirst(i);
        }

        st.clear();

        // next strictly greater element (index)
        for (int i = size - 1; i >= 0; i--) {
            while (!st.isEmpty() && nums[st.peekFirst()] < nums[i]) {
                st.pollFirst();
            }
            rightGreater[i] = st.isEmpty() ? size : st.peekFirst();
            st.addFirst(i);
        }

        long ans = 0;
        for (int i = 0; i < size; i++) {
            long leftCount = i - leftGreater[i];
            long rightCount = rightGreater[i] - i;
            ans += leftCount * rightCount * nums[i];
        }
        return ans;
    }

    private long calMinSum(int[] nums) {
        int size = nums.length;
        int[] leftSmaller = new int[size];
        int[] rightSmaller = new int[size];

        Deque<Integer> st = new ArrayDeque<>();

        // previous smaller-or-equal element (index)
        for (int i = 0; i < size; i++) {
            while (!st.isEmpty() && nums[st.peekFirst()] >= nums[i]) {
                st.pollFirst();
            }
            leftSmaller[i] = st.isEmpty() ? -1 : st.peekFirst();
            st.addFirst(i);
        }

        st.clear();

        // next strictly smaller element (index)
        for (int i = size - 1; i >= 0; i--) {
            while (!st.isEmpty() && nums[st.peekFirst()] > nums[i]) {
                st.pollFirst();
            }
            rightSmaller[i] = st.isEmpty() ? size : st.peekFirst();
            st.addFirst(i);
        }

        long ans = 0;
        for (int i = 0; i < size; i++) {
            long leftCount = i - leftSmaller[i];
            long rightCount = rightSmaller[i] - i;
            ans += leftCount * rightCount * nums[i];
        }
        return ans;
    }
}
```

Note: LC 2104's constraints are small enough that no modulo is required (unlike LC 907), but overflow is still possible with `int`, so accumulate in `long`.

---

## 5. Dry Run

Input:

```text
nums = [1, 2, 3]
```

**Max side** — `leftGreater` (pop while `<=`): all `-1` (array is increasing, nothing to the left is ever `>=`).
`rightGreater` (pop while `<`): index0->1 (`2>1`), index1->2 (`3>2`), index2->3(size).

Contributions to max-sum:
- i=0: left=0-(-1)=1, right=1-0=1 → 1*1*1=1
- i=1: left=1-(-1)=2, right=2-1=1 → 2*1*2=4
- i=2: left=2-(-1)=3, right=3-2=1 → 3*1*3=9

`sumOfSubarrayMax = 1+4+9 = 14`

**Min side** — `leftSmaller` (pop while `nums[st.top()] >= curr`, left to right):
- i=0 (val1): stack empty → leftSmaller[0]=-1; push idx0
- i=1 (val2): top idx0(val1), 1>=2? No → leftSmaller[1]=0; push idx1
- i=2 (val3): top idx1(val2), 2>=3? No → leftSmaller[2]=1; push idx2

`leftSmaller = [-1, 0, 1]`

`rightSmaller` (pop while `nums[st.top()] > curr`, right to left):
- i=2 (val3): stack empty → rightSmaller[2]=3(size); push idx2
- i=1 (val2): top idx2(val3), 3>2 → pop; stack empty → rightSmaller[1]=3; push idx1
- i=0 (val1): top idx1(val2), 2>1 → pop; stack empty → rightSmaller[0]=3; push idx0

`rightSmaller = [3, 3, 3]`

Contributions to min-sum:
- i=0: left=0-(-1)=1, right=3-0=3 → 1*3*1=3
- i=1: left=1-0=1, right=3-1=2 → 1*2*2=4
- i=2: left=2-1=1, right=3-2=1 → 1*1*3=3

`sumOfSubarrayMin = 3+4+3 = 10`

Final:

```text
answer = 14 - 10 = 4
```

Matches the expected output. As a sanity check by brute force: subarray mins of `[1,2,3]` are `1,2,3,1,2,1` (sum `10`) and maxes are `1,2,3,2,3,3` (sum `14`); `14-10=4`.

---

## 6. Edge Cases

- Single element: range is always `0`, since `max == min` for a subarray of length 1.
- Strictly monotonic array (increasing or decreasing): still computed correctly by the general formula; no special-casing needed.
- All elements equal: `sumOfSubarrayMax == sumOfSubarrayMin`, so the answer is `0`.
- Negative numbers: works unchanged, since only relative order matters.
- Large arrays: use `long` throughout to avoid overflow — `leftCount * rightCount * nums[i]` can be large even for moderate `n`.

---

## 7. Common Mistakes

- Reusing the exact same pop-comparison operators for both the min and max helper — they must be mirrored (`>=`/`>` for min, `<=`/`<` for max), otherwise duplicate values get miscounted.
- Forgetting to clear/reset the stack between the left-pass and right-pass within each helper.
- Subtracting in the wrong order (`min - max` instead of `max - min`).
- Not verifying a hand-traced dry run against brute force — as shown above, it's easy to make an off-by-one error when simulating stack pops manually.

---

## 8. Complexity

```text
Time:  O(n)      (four linear passes total: two for max, two for min)
Space: O(n)
```

---

## 9. Pattern Recognition

Whenever a problem asks for a sum over all subarrays of `(max - min)`, split it into two independent applications of the "sum of subarray minimums/maximums" template ([[MS_06_Sum_Of_Subarray_Minimums_LC907]]) and subtract.

---

## 10. Takeaway

Linearity of the "range = max - min" formula lets us decompose a seemingly harder problem into two copies of an already-solved one. When manually dry-running monotonic stack code, always cross-check against brute force on a tiny input — it's the fastest way to catch a flipped comparison operator.

---

# End of Notes
