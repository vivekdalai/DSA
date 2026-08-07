# Monotonic Stack Notes

## 06 - Sum of Subarray Minimums (LC 907)

Problem: Given an array `nums`, find the sum of `min(subarray)` for every contiguous subarray of `nums`. Return the answer modulo `10^9 + 7`.

---

## 1. Problem Understanding

Example:

```text
nums = [3,1,2,4]
answer = 17
```

There are `n*(n+1)/2` subarrays. Brute force enumerates all of them and finds each minimum: `O(n^2)` or `O(n^3)` naively.

Key reframing: instead of asking "what is the min of each subarray," ask the reverse question — **"for each element, in how many subarrays is it *the* minimum?"** Then:

```text
answer = sum over i of ( nums[i] * (count of subarrays where nums[i] is the minimum) )
```

---

## 2. Core Intuition

Element `nums[i]` is the minimum of a subarray `[l, r]` (with `l <= i <= r`) exactly when:

- every element between `l` and `i-1` is `>= nums[i]` (nothing smaller blocks it on the left)
- every element between `i+1` and `r` is `> nums[i]` (nothing smaller-or-equal blocks it on the right — the strict/non-strict split avoids double-counting when duplicate values exist)

So we need, for each `i`:

```text
left[i]  = index of nearest element to the left that is strictly smaller than nums[i]   (PSE - previous smaller element)
right[i] = index of nearest element to the right that is smaller than or equal to nums[i] (NSE - next smaller-or-equal element)
```

Then the number of valid subarrays where `nums[i]` is the minimum is:

```text
countLeft  = i - left[i]
countRight = right[i] - i
contribution = nums[i] * countLeft * countRight
```

---

## 3. Why the Strict/Non-Strict Asymmetry Matters

If both sides used the same (`>=` or `>`) comparison, a subarray containing two equal minimum values would be counted twice — once "belonging" to each duplicate.

By making the left boundary strict (`>=` stops it, i.e., PSE uses `<` to keep popping... concretely, pop while `stack.top() >= curr`) and the right boundary non-strict in the opposite sense (pop while `stack.top() > curr`), each subarray with a repeated minimum gets attributed to exactly one occurrence — conventionally, the **leftmost** occurrence "owns" ties.

---

## 4. Java Implementation

```java
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int sumSubarrayMins(int[] nums) {
        int size = nums.length;
        int mod = 1_000_000_007;

        int[] prevSmallerEqual = new int[size]; // index of previous element <= nums[i]... (see note below)
        int[] nextSmaller = new int[size];

        Deque<Integer> st = new ArrayDeque<>(); // stores indices

        // previous smaller-or-equal element (index), pop while stack top's value >= curr
        for (int i = 0; i < size; i++) {
            while (!st.isEmpty() && nums[st.peekFirst()] >= nums[i]) {
                st.pollFirst();
            }
            prevSmallerEqual[i] = st.isEmpty() ? -1 : st.peekFirst();
            st.addFirst(i);
        }

        st.clear();

        // next strictly smaller element (index), pop while stack top's value > curr
        for (int i = size - 1; i >= 0; i--) {
            while (!st.isEmpty() && nums[st.peekFirst()] > nums[i]) {
                st.pollFirst();
            }
            nextSmaller[i] = st.isEmpty() ? size : st.peekFirst();
            st.addFirst(i);
        }

        long ans = 0;
        for (int i = 0; i < size; i++) {
            long countLeft = i - prevSmallerEqual[i];
            long countRight = nextSmaller[i] - i;
            ans = (ans + (countLeft * countRight % mod) * nums[i]) % mod;
        }

        return (int) ans;
    }
}
```

---

## 5. Dry Run

Input:

```text
nums = [3, 1, 2, 4]
```

Previous smaller-or-equal (pop while `>=`):

| i | nums[i] | pops | PSE index |
|---:|---:|---|---:|
| 0 | 3 | - | -1 |
| 1 | 1 | pop idx0(3) | -1 |
| 2 | 2 | - (top is idx1, val1 < 2) | 1 |
| 3 | 4 | - | 2 |

Next strictly smaller (pop while `>`):

| i | nums[i] | pops | NSE index |
|---:|---:|---|---:|
| 3 | 4 | - | 4 (size) |
| 2 | 2 | pop idx3(4) | 4 |
| 1 | 1 | pop idx2(2) | 4 |
| 0 | 3 | - (top is idx1, val1, not > 3) | 1 |

Contribution per index:

| i | nums[i] | countLeft = i-PSE | countRight = NSE-i | contribution |
|---:|---:|---:|---:|---:|
| 0 | 3 | 0-(-1)=1 | 1-0=1 | 3 |
| 1 | 1 | 1-(-1)=2 | 4-1=3 | 6 |
| 2 | 2 | 2-1=1 | 4-2=2 | 4 |
| 3 | 4 | 3-2=1 | 4-3=1 | 4 |

Total: `3 + 6 + 4 + 4 = 17` ✓ matches expected answer.

---

## 6. Edge Cases

- Single element: `answer = nums[0]` (one subarray, itself).
- All elements equal (e.g. `[2,2,2]`): the strict/non-strict split ensures each subarray's minimum is counted exactly once, attributed to the leftmost occurrence in each span.
- Strictly increasing array (e.g. `[1,2,3,4]`): every subarray's minimum is its leftmost element; `countRight` for index `i` extends all the way past the end for the smallest elements.
- Strictly decreasing array (e.g. `[4,3,2,1]`): mirror of the above; every subarray's minimum is its rightmost element.
- Large arrays with negative numbers: works the same, since only relative comparisons matter, not sign.
- Overflow: use `long` for intermediate `countLeft * countRight * nums[i]`, and apply `% mod` at each multiplication step, not just at the end.

---

## 7. Common Mistakes

- Using the same comparison operator (both `>=` or both `>`) on both sides — this double-counts subarrays when duplicate values exist.
- Forgetting to take `% mod` before multiplying (can overflow `int`, and even `long` overflow is possible if you multiply three large numbers together before ever taking a mod).
- Off-by-one when initializing `nextSmaller[i] = size` (not `size - 1`) — the "no next smaller" sentinel must be one past the last valid index so that `right[i] - i` correctly counts including the last element.
- Confusing this problem with "sum of subarray **maximums**" — that variant flips both comparisons (see [[MS_07_Sum_Of_Subarray_Ranges_LC2104]], which needs both).

---

## 8. Complexity

```text
Time:  O(n)      (two linear passes with a monotonic stack)
Space: O(n)
```

---

## 9. Pattern Recognition

"Sum of `f(subarray)` over all subarrays where `f` is min or max" almost always reduces to: **for each element, count how many subarrays it is the min/max of**, via previous/next smaller-or-equal (or greater-or-equal) boundaries.

---

## 10. Takeaway

Don't enumerate subarrays — enumerate **contributions**. Each element contributes `value * (ways to choose a left boundary) * (ways to choose a right boundary)`, and those "ways" are exactly the gaps between it and its nearest smaller neighbors, found via a monotonic stack in O(n).

---

# End of Notes
