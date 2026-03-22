# Two Pointers Notes

## 08 - Merge Sorted Array (LC 88)

**Generated on:** 2026-03-23 01:08:44 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given two sorted integer arrays:
- `nums1` of size `m + n`, where first `m` elements are valid and the last `n` are empty space
- `nums2` of size `n`

Merge `nums2` into `nums1` as one sorted array, in-place.

Examples:
- `nums1 = [1,2,3,0,0,0], m = 3`
- `nums2 = [2,5,6], n = 3`
- result → `[1,2,2,3,5,6]`

------------------------------------------------------------------------

## 🪜 2. Core Two-Pointer Idea

If we merge from the front:
- we would overwrite useful values in `nums1`

So merge from the back:
- `i = m - 1` → last valid element in nums1
- `j = n - 1` → last element in nums2
- `k = m + n - 1` → fill position from the end

At each step:
- place the larger of `nums1[i]` and `nums2[j]` at `nums1[k]`

------------------------------------------------------------------------

## 🔁 3. Steps

1. Initialize:
   - `i = m - 1`
   - `j = n - 1`
   - `k = m + n - 1`
2. Compare `nums1[i]` and `nums2[j]`
3. Put larger one at `nums1[k]`
4. Move corresponding pointer backward
5. Continue until `nums2` is exhausted

Why only need to ensure `nums2` is exhausted?
- If elements remain in `nums1`, they are already in correct place

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class MergeSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;
        int j = n - 1;
        int k = m + n - 1;

        while (j >= 0) {
            if (i >= 0 && nums1[i] > nums2[j]) {
                nums1[k] = nums1[i];
                i--;
            } else {
                nums1[k] = nums2[j];
                j--;
            }
            k--;
        }
    }
}
```

Complexity:
- Time: O(m + n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums1 = [1,2,3,0,0,0], m = 3`
`nums2 = [2,5,6], n = 3`

Pointers:
- i = 2 (3)
- j = 2 (6)
- k = 5

Steps:
- compare 3 and 6 → put 6 at nums1[5]
- compare 3 and 5 → put 5 at nums1[4]
- compare 3 and 2 → put 3 at nums1[3]
- compare 2 and 2 → put nums2's 2 at nums1[2]
- continue...

Final:
- `[1,2,2,3,5,6]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- two sorted arrays
- merge in-place
- one array has buffer space at the end

This is the classic:
- **two pointers from the back**

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `n = 0` → nothing to merge
- `m = 0` → copy all of nums2 into nums1
- Merge from back, not front
- Only `while (j >= 0)` is required

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Use 3 pointers from the back
- Always place the larger element at the end
- Fundamental in-place merging pattern

------------------------------------------------------------------------

# End of Notes
