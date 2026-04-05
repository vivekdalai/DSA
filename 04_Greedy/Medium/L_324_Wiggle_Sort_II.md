# Greedy Notes

## Wiggle Sort II

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an integer array `nums`, reorder it in-place so that:

    nums[0] < nums[1] > nums[2] < nums[3] ...

You may assume a valid answer exists.

**Example 1**

    Input: nums = [1,5,1,1,6,4]
    Output: [1,6,1,5,1,4]

**Example 2**

    Input: nums = [1,3,2,2,3,1]
    Output: [2,3,1,3,1,2]

------------------------------------------------------------------------

## 2. Why Sorting Alone Is Not Enough

If we sort and place numbers directly, duplicates can collide and break the strict wiggle rule.

The standard strong solution is:

1. find the median
2. place values `>` median in odd positions first
3. place values `<` median in even positions near the end
4. leave median values in the remaining slots

Your file does this with:

- quickselect for median
- virtual indexing
- Dutch National Flag partition

------------------------------------------------------------------------

## 3. Virtual Indexing Trick

The mapping:

    newIndex(i, n) = (1 + 2 * i) % (n | 1)

visits indices in this order:

- odd positions first
- then even positions

That is perfect because the larger numbers should land in odd positions:

    nums[1], nums[3], nums[5], ...

------------------------------------------------------------------------

## 4. Three-Way Partition Around The Median

Once the median is known:

- values `> median` should go to the left side of virtual order
- values `< median` should go to the right side
- values `== median` stay in the middle

The loop with `left`, `i`, and `right` is exactly a Dutch National Flag partition, but applied through the virtual index mapping.

That is the heart of the solution.

------------------------------------------------------------------------

## 5. Clean Mental Model

If you remember only one idea, remember this:

- big numbers must occupy wiggle peaks
- small numbers must occupy wiggle valleys
- median values fill the remaining gaps

Virtual indexing is just the tool that lets the in-place partition place numbers into those peak/valley slots directly.

------------------------------------------------------------------------

## 6. Core Code Shape

```java
public static void wiggleSort(int[] nums) {
    int median = findMedian(nums, 0, nums.length - 1, nums.length / 2);
    int n = nums.length;
    int left = 0, i = 0, right = n - 1;

    while (i <= right) {
        int mapped = newIndex(i, n);

        if (nums[mapped] > median) {
            swap(nums, newIndex(left++, n), mapped);
            i++;
        } else if (nums[mapped] < median) {
            swap(nums, newIndex(right--, n), mapped);
        } else {
            i++;
        }
    }
}

private static int newIndex(int index, int n) {
    return (1 + 2 * index) % (n | 1);
}
```

------------------------------------------------------------------------

## 7. Complexity And Pattern

- **Time:** average `O(n)` with quickselect
- **Space:** `O(1)` extra

Pattern:

- select median
- 3-way partition
- use index remapping to satisfy a layout constraint

------------------------------------------------------------------------

## End of Notes
