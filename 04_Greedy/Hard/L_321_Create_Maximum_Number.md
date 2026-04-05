# Greedy Notes

## 03 - Create Maximum Number

**Generated on:** 2026-04-06 01:00:13 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/create-maximum-number/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given two arrays of digits:

- `nums1`
- `nums2`

You must create the largest possible number of length `k`.

Rules:

- Digits taken from the same array must keep their original relative order
- You may choose some digits from `nums1` and the rest from `nums2`

Return the resulting array of digits.

**Example 1**

    Input: nums1 = [3, 4, 6, 5], nums2 = [9, 1, 2, 5, 8, 3], k = 5
    Output: [9, 8, 6, 5, 3]

Explanation:

- Pick the best subsequence `[6, 5]` from `nums1`
- Pick the best subsequence `[9, 8, 3]` from `nums2`
- Merge them greedily to form the largest possible number

**Example 2**

    Input: nums1 = [6, 7], nums2 = [6, 0, 4], k = 5
    Output: [6, 7, 6, 0, 4]

------------------------------------------------------------------------

## 2. Why This Problem Is Hard

There are three decisions happening at once:

1. How many digits should come from `nums1`?
2. For a fixed count, which subsequence from each array is best?
3. Once the two best subsequences are chosen, how should they be merged?

If even one of those steps is greedy in the wrong way, the final number becomes smaller.

Your implementation handles all three layers separately, which is the right way to think about this problem.

------------------------------------------------------------------------

## 3. Outer Loop: Try Every Valid Split

Suppose we take:

    n1 digits from nums1
    n2 = k - n1 digits from nums2

Not every split is legal, so the code checks:

    n1 <= nums1.length
    n2 <= nums2.length

For every valid split:

- build the best length-`n1` subsequence from `nums1`
- build the best length-`n2` subsequence from `nums2`
- merge them into the best possible combined number
- compare that candidate against the best answer so far

This is why the loop runs from `0` to `k`.

------------------------------------------------------------------------

## 4. Step 1: Pick The Best Subsequence From One Array

This part is a classic **monotonic stack** trick.

Goal:

- From an array of length `m`, keep exactly `t` digits
- So we are allowed to discard `m - t` digits

The greedy rule is:

- While the current digit is bigger than the stack top
- And we still have deletions available
- Pop the smaller digit

Why?

Because a larger digit placed earlier makes the whole number lexicographically larger.

That is exactly what these variables represent:

    canMiss1 = len1 - n1
    canMiss2 = len2 - n2

After scanning the array, if deletions are still left, remove digits from the end because trailing digits are least valuable.

------------------------------------------------------------------------

## 5. Step 2: Merge Greedily, But With Tie Awareness

After extracting the best subsequence from both arrays, the merge is not as simple as:

- always take the larger current digit

That fails when the current digits are equal.

Example:

    arr1 = [6, 7]
    arr2 = [6, 0, 4]

If both current digits are `6`, the decision must depend on the remaining suffix, not just the current value.

So the merge rule becomes:

- Take from `arr1` if `arr1[i...]` is lexicographically greater than `arr2[j...]`
- Otherwise take from `arr2`

That is what `greater(...)` checks in your file.

This tie-handling step is the entire heart of the merge.

------------------------------------------------------------------------

## 6. Step 3: Keep The Best Candidate Across Splits

Once one merged candidate is built, the code compares it with the current answer digit by digit.

At the first differing position:

- larger digit wins
- if equal, continue

This is the correct lexicographic comparison for numbers stored as arrays.

------------------------------------------------------------------------

## 7. Clean Mental Model

This problem is not one single greedy.

It is:

- greedy split enumeration
- greedy subsequence extraction with a monotonic stack
- greedy lexicographic merge

Missing any one of these makes the final answer wrong.

------------------------------------------------------------------------

## 8. Dry Run

**Input:**

    nums1 = [3, 4, 6, 5]
    nums2 = [9, 1, 2, 5, 8, 3]
    k = 5

One winning split is:

    take 2 digits from nums1
    take 3 digits from nums2

### Best length-2 subsequence from `nums1`

From:

    [3, 4, 6, 5]

Best choice:

    [6, 5]

### Best length-3 subsequence from `nums2`

From:

    [9, 1, 2, 5, 8, 3]

Best choice:

    [9, 8, 3]

### Merge them

Compare suffixes at each step:

- `[6, 5]` vs `[9, 8, 3]` -> take `9`
- `[6, 5]` vs `[8, 3]` -> take `8`
- `[6, 5]` vs `[3]` -> take `6`
- `[5]` vs `[3]` -> take `5`
- take `3`

Result:

    [9, 8, 6, 5, 3]

That is the final answer.

------------------------------------------------------------------------

## 9. Complexity

Let:

    m = nums1.length
    n = nums2.length

For each valid split:

- extracting subsequences is linear
- merging is greedy
- suffix comparison during merge can rescan equal prefixes

So this implementation is:

- **Time:** worst-case `O(k * (m + n) + k^3)`
- **Space:** `O(k)`

The suffix comparisons are the reason the merge step can cost more than plain linear time in the worst case.

------------------------------------------------------------------------

## 10. Pattern Recognition

This problem combines three powerful patterns:

- **Monotonic stack for best subsequence of fixed length**
- **Greedy merge using lexicographic suffix comparison**
- **Enumerate all feasible splits when the final answer mixes two sources**

Whenever you see:

- maintain relative order
- choose digits from multiple arrays
- maximize the final lexicographic number

think in exactly this order:

    split -> extract -> merge -> compare

------------------------------------------------------------------------

## 11. Core Code From The File

```java
public static int[] maxNumber(int[] nums1, int[] nums2, int k) {
    int[] ans = new int[k];

    int len1 = nums1.length;
    int len2 = nums2.length;

    for (int i = 0; i <= k; i++) {
        int n1 = i;
        int n2 = k - i;

        if (n1 <= len1 && n2 <= len2) {
            int canMiss1 = len1 - n1;
            int canMiss2 = len2 - n2;

            Deque<Integer> st1 = new ArrayDeque<>();
            Deque<Integer> st2 = new ArrayDeque<>();

            for (int d : nums1) {
                while (!st1.isEmpty() && canMiss1 > 0 && d > st1.peek()) {
                    st1.pop();
                    canMiss1--;
                }
                st1.push(d);
            }

            while (!st1.isEmpty() && canMiss1 > 0) {
                st1.pop();
                canMiss1--;
            }

            for (int d : nums2) {
                while (!st2.isEmpty() && canMiss2 > 0 && d > st2.peek()) {
                    st2.pop();
                    canMiss2--;
                }
                st2.push(d);
            }

            while (!st2.isEmpty() && canMiss2 > 0) {
                st2.pop();
                canMiss2--;
            }

            int[] arr1 = new int[n1];
            int[] arr2 = new int[n2];

            for (int j = n1 - 1; j >= 0; j--) {
                if (!st1.isEmpty()) arr1[j] = st1.pop();
            }

            for (int j = n2 - 1; j >= 0; j--) {
                if (!st2.isEmpty()) arr2[j] = st2.pop();
            }

            int[] mergedArray = mergeGreedy(arr1, n1, arr2, n2);
            ans = findMaxNumberArray(mergedArray, ans);
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## End of Notes
