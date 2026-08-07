# Monotonic Stack Notes

## 02 - Next Greater Element I (LC 496)

Problem: For each element in an array, find the first element to its right that is strictly greater than it. If no such element exists, the answer is `-1`.

---

## 1. Problem Understanding

Example:

```text
nums = [4,12,5,3,1,2,5,3,1,2,4,6]
answer = [12,-1,6,5,2,5,6,4,2,4,6,-1]
```

For `nums[0] = 4`, the first greater element to the right is `12`.

For `nums[1] = 12`, nothing to its right is greater, so the answer is `-1`.

Brute force checks, for every index, all indices to its right:

```text
O(n^2)
```

We want `O(n)`.

---

## 2. Core Intuition

Scan from **right to left**.

Keep a stack of "candidates that could still be the answer for something to their left."

While scanning right to left, an element can only be the next-greater-answer for the current element if it is bigger than the current element. Anything smaller can never help anyone further left (the current, bigger element blocks it), so it should be discarded.

That is exactly a **monotonic decreasing stack** (top to bottom is decreasing... actually top is always the smallest "usable" candidate greater than everything already popped).

---

## 3. Why Monotonic Stack?

Rule while moving right to left, for current element `curr`:

```text
while stack not empty and stack.top() <= curr:
    pop()          // top can never be the answer for curr or for anyone left of curr
answer[i] = stack.empty() ? -1 : stack.top()
push(curr)
```

Every element is pushed once and popped at most once, so the total work across the whole scan is `O(n)`.

---

## 4. Java Implementation

```java
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {
            int curr = nums[i];

            while (!stack.isEmpty() && stack.peekFirst() <= curr) {
                stack.pollFirst();
            }

            result[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            stack.addFirst(curr);
        }

        return result;
    }
}
```

Note: LC 496 actually asks for the answer only for a subset `nums1` that is a subsequence of `nums2`. In that version, compute the "next greater" map for every value in `nums2` first (as above, using a `HashMap<Integer,Integer>` instead of an array, since values in `nums2` are distinct), then look up each value of `nums1` in that map.

```java
class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreater = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = nums2.length - 1; i >= 0; i--) {
            int curr = nums2[i];

            while (!stack.isEmpty() && stack.peekFirst() <= curr) {
                stack.pollFirst();
            }

            nextGreater.put(curr, stack.isEmpty() ? -1 : stack.peekFirst());
            stack.addFirst(curr);
        }

        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = nextGreater.get(nums1[i]);
        }

        return result;
    }
}
```

---

## 5. Dry Run

Input:

```text
nums = [4, 12, 5, 3, 1, 2, 5, 3, 1, 2, 4, 6]
```

| i (right to left) | curr | pops | stack after push | result[i] |
|---:|---:|---|---|---:|
| 11 | 6 | - | [6] | -1 |
| 10 | 4 | - | [4,6] | 6 |
| 9 | 2 | - | [2,4,6] | 4 |
| 8 | 1 | - | [1,2,4,6] | 2 |
| 7 | 3 | pop 1, pop 2 | [3,4,6] | 4 |
| 6 | 5 | pop 3, pop 4 | [5,6] | 6 |
| 5 | 2 | - | [2,5,6] | 5 |
| 4 | 1 | - | [1,2,5,6] | 2 |
| 3 | 3 | pop 1, pop 2 | [3,5,6] | 5 |
| 2 | 5 | pop 3 | [5,6] | 6 |
| 1 | 12 | pop 5, pop 6 | [12] | -1 |
| 0 | 4 | - | [4,12] | 12 |

Final:

```text
[12, -1, 6, 5, 2, 5, 6, 4, 2, 4, 6, -1]
```

---

## 6. Edge Cases

- Empty array: return an empty array.
- Single element: no element to its right, answer is `[-1]`.
- Strictly decreasing array (e.g. `[5,4,3,2,1]`): every answer is `-1`, stack never keeps more than one useful element for very long but nothing ever finds a greater element.
- Strictly increasing array (e.g. `[1,2,3,4,5]`): each element's answer is the very next element; the stack stays small because every new (smaller, when scanned right-to-left) element gets its answer immediately from the previous push.
- Duplicate values (e.g. `[2,2,2]`): must use `<=` (not `<`) when popping, otherwise an equal element would incorrectly be treated as "greater."
- All elements equal: every answer is `-1`.

---

## 7. Common Mistakes

- Using `<` instead of `<=` in the pop condition — this makes an equal value incorrectly count as a "next greater" element, which is wrong since we need *strictly* greater.
- Scanning left to right and trying to look forward — this loses the benefit of the stack and degrades to brute force.
- Forgetting to push the current element after computing its answer.
- For the two-array LC 496 variant, forgetting that `nums1` is a subsequence of `nums2` and the map must be built from `nums2`.

---

## 8. Complexity

```text
Time:  O(n)
Space: O(n)
```

Each element is pushed once and popped at most once.

---

## 9. Pattern Recognition

Use this exact template whenever the question is "for each element, find the nearest greater/smaller element to the left/right":

```text
direction of scan:
    "to the right"  -> scan right to left
    "to the left"   -> scan left to right

pop condition (finding GREATER):
    stack.top() <= curr   -> pop
pop condition (finding SMALLER):
    stack.top() >= curr   -> pop
```

---

## 10. Takeaway

Scanning in the *opposite* direction of "where the answer lives" and keeping a monotonic stack of still-useful candidates turns an `O(n^2)` nested-loop problem into `O(n)`.

This exact template (direction + pop condition) is reused in [[MS_03_Next_Greater_Element_II_Circular_LC503]], [[MS_04_Stock_Span_Problem]], [[MS_06_Sum_Of_Subarray_Minimums_LC907]], [[MS_07_Sum_Of_Subarray_Ranges_LC2104]], and [[MS_08_Largest_Rectangle_In_Histogram_LC84]].

---

# End of Notes
