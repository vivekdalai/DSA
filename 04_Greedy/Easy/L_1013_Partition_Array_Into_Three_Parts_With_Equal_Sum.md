# Greedy Notes

## 02 - Partition Array Into Three Parts With Equal Sum

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/partition-array-into-three-parts-with-equal-sum/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an array `arr`, return `true` if it can be partitioned into three non-empty contiguous parts with equal sum.

Otherwise, return `false`.

**Example 1**

    Input: arr = [0, 2, 1, -6, 6, -7, 9, 1, 2, 0, 1]
    Output: true

One valid split is:

    [0, 2, 1, -6, 6, -7]
    [9, 1]
    [2, 0, 1]

Each part sums to `0`.

**Example 2**

    Input: arr = [0, 2, 1, -6, 6, 7, 9, -1, 2, 0, 1]
    Output: false

------------------------------------------------------------------------

## 2. First Observation

If the total sum is not divisible by `3`, the answer is immediately `false`.

So the real target is:

    chunkSum = totalSum / 3

Now the problem becomes:

- can we find one prefix with sum `chunkSum`
- then another contiguous part with sum `chunkSum`
- while still leaving at least one element for the third part

------------------------------------------------------------------------

## 3. Greedy Scan

The method keeps a running sum `localSum`.

Whenever:

    localSum == chunkSum

we count one completed chunk and reset `localSum` to `0`.

The important shortcut is:

- if two chunks are found before the last index
- then the remaining suffix must also have sum `chunkSum`

That works because the whole array sum is already known to be `3 * chunkSum`.

------------------------------------------------------------------------

## 4. Why The Early Return Is Valid

Suppose the total sum is divisible by `3`.

If we already found two non-empty parts, both summing to `chunkSum`, then:

    remainingSum = totalSum - 2 * chunkSum = chunkSum

So we do not need to explicitly scan for the third chunk, as long as the suffix is non-empty.

That is why the code checks:

    cnt == 2 && i < len - 1

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean canThreePartsEqualSum(int[] arr) {
    int sum = 0;

    for (int x : arr) {
        sum += x;
    }

    if (sum % 3 != 0) return false;

    int chunkSum = sum / 3;
    int localSum = 0, cnt = 0;

    for (int i = 0; i < arr.length; i++) {
        localSum += arr[i];

        if (localSum == chunkSum) {
            cnt++;
            localSum = 0;
        }

        if (cnt == 2 && i < arr.length - 1) return true;
    }

    return false;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- global divisibility check
- one pass to lock chunks greedily

This works well when you only need to prove the existence of equal-sum segments.

------------------------------------------------------------------------

## End of Notes
