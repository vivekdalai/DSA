# Linked List / Queue / Priority Queue Notes

## 09 - Sliding Window Maximum (LC 239)

**Generated on:** 2026-03-31 19:24:35 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `nums` and a window size `k`, return the maximum element in every contiguous window of length `k`.

Example:
- `nums = [1, 3, -1, -3, 5, 3, 6, 7]`
- `k = 3`
- answer = `[3, 3, 5, 5, 6, 7]`

Why this is not brute force:
- scanning all `k` elements for every window costs `O(n * k)`
- if `k` is large, that becomes too slow

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use a **monotonic decreasing deque** storing indices.

Deque rules:
- indices must stay inside the current window
- values must remain in decreasing order from front to back

This guarantees:
- the front of the deque always stores the index of the current window maximum

------------------------------------------------------------------------

## 🔁 3. Deque Operations

For each index `i`:

1. Remove indices from the front if they are outside the current window
2. Remove indices from the back while `nums[back] <= nums[i]`
   - those smaller values can never become maximum again
3. Push current index at the back
4. Once `i >= k - 1`, record `nums[deque.front]`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.ArrayDeque;
import java.util.Deque;

class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] ans = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        int idx = 0;

        for (int i = 0; i < n; i++) {
            int windowStart = i - k + 1;

            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                deque.pollFirst();
            }

            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i >= k - 1) {
                ans[idx++] = nums[deque.peekFirst()];
            }
        }

        return ans;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(k)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For:
- `nums = [1, 3, -1, -3, 5]`
- `k = 3`

`i = 0`, value = `1`
- deque = `[0]`

`i = 1`, value = `3`
- remove `1` from deque back because `1 <= 3`
- deque = `[1]`

`i = 2`, value = `-1`
- deque = `[1, 2]`
- first full window `[1, 3, -1]`
- max = `nums[1] = 3`

`i = 3`, value = `-3`
- deque = `[1, 2, 3]`
- window `[3, -1, -3]`
- max = `3`

`i = 4`, value = `5`
- index `1` goes out if needed by window start
- remove smaller values from back: `-3`, `-1`, `3`
- deque = `[4]`
- window `[-1, -3, 5]`
- max = `5`

Answer:
- `[3, 3, 5]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use monotonic deque when the problem asks for:
- sliding window maximum
- sliding window minimum
- best value in every fixed-size window
- maintain max/min in streaming window

Trigger words:
- "every window of size k"
- "maximum/minimum in each subarray of fixed size"
- "remove outdated and maintain best candidate"

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `k = 1` -> answer is the array itself
- All elements equal -> deque still works
- Do not store values only; store indices to know when elements go out of the window
- Remove out-of-window indices before taking answer
- The PDF implementation used front/back in reverse orientation; that is valid too, but consistency matters

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Use a deque of indices, not values.
- Keep deque monotonic decreasing by value.
- Front always gives the current window maximum.
- Every index enters and leaves deque once -> total O(n).

------------------------------------------------------------------------

# End of Notes
