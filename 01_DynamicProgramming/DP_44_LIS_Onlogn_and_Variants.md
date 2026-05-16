# Dynamic Programming Notes

## 44 - Longest Increasing Subsequence (LIS) O(n log n) + Variants

**Generated on:** 2026-02-25 23:14:06 (IST)

---

## 1. What We Are Solving First

Given an array `nums`, first focus on only this:

- Find the **length** of the Longest Increasing Subsequence
- Strictly increasing means `next > previous`

Example:

- `nums = [10, 9, 2, 5, 3, 7, 101, 18]`
- Answer = `4`
- One LIS is `[2, 3, 7, 101]`

Important:

- In this note, first understand how to find the **LIS length**
- Reconstruction is a separate step and should not be mixed too early

---

## 2. First Intuition: O(n^2) DP

Let:

- `dp[i]` = length of LIS ending exactly at index `i`

Then:

- Start every `dp[i] = 1`
- For every `j < i`, if `nums[j] < nums[i]`, then `nums[i]` can extend the subsequence ending at `j`
- So:

```java
dp[i] = max(dp[i], dp[j] + 1)
```

Code:

```java
import java.util.*;

class LIS_ON2 {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        int ans = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(ans, dp[i]);
        }

        return ans;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(n)`

This is easy to understand, but not optimal.

---

## 3. Goal of the O(n log n) Method

We want the same answer, but faster.

Instead of asking:

- "What is the LIS ending at every index?"

We maintain a smarter structure:

- For every possible length, keep the **smallest possible tail**

That array is called `tails`.

---

## 4. Core Idea of `tails`

`tails[len]` means:

- the smallest ending value of an increasing subsequence of length `len + 1`

This does **not** mean `tails` is the actual LIS.

It only stores the best tail values that help us build longer subsequences later.

Why smallest tail?

- Smaller tail is always better
- Because it gives more chance to attach future bigger numbers

Example:

- Suppose for subsequence length `3`, one tail is `11`
- Another tail is `7`
- Keeping tail `7` is better than keeping tail `11`
- Because more future elements can extend `7`

So for each length, we only keep the smallest possible tail.

---

## 5. How Update Happens

For each number `x`:

1. Find the first position in `tails` where value is `>= x`
2. Replace that position with `x`
3. If no such position exists, append `x`

Interpretation:

- Replacing means: "I found a better tail for this subsequence length"
- Appending means: "I created a longer increasing subsequence"

Because we use binary search on `tails`, each update is `O(log n)`.

---

## 6. Dry Run

Take:

```text
[10, 9, 2, 5, 3, 7, 101, 18]
```

Process one by one:

- `10` -> `tails = [10]`
- `9`  -> replace first value `>= 9` -> `tails = [9]`
- `2`  -> replace first value `>= 2` -> `tails = [2]`
- `5`  -> append -> `tails = [2, 5]`
- `3`  -> replace first value `>= 3` -> `tails = [2, 3]`
- `7`  -> append -> `tails = [2, 3, 7]`
- `101` -> append -> `tails = [2, 3, 7, 101]`
- `18` -> replace first value `>= 18` -> `tails = [2, 3, 7, 18]`

Final answer:

- Length of LIS = size of `tails` = `4`

Very important:

- Final `tails = [2, 3, 7, 18]`
- But one actual LIS could be `[2, 3, 7, 101]`
- So `tails` helps us get the **length**
- `tails` is not guaranteed to be the actual subsequence

This is the most important thing to remember.

---

## 7. O(n log n) Code for LIS Length

```java
private int LIS(int[] numbers){
    //Longest Increasing Subsequence
    int n = numbers.length;
    int[] tails = new int[n];
    int size = 0;

    for(int x : numbers){
        int left = 0;
        int right = size - 1;

        //find first index where tails[idx] >= x
        int ans = -1;
        while(left <= right){
            int mid = (left + right) / 2;
            if(tails[mid] >= x){
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if(ans == -1){
            tails[size] = x;
            size++;
        } else{
            tails[ans] = x; //replace that greater element with smaller value (x), eg LIS (2, 5) --> replace --> (2 ,3)
        }

    }

    return size;
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(n)`

---

## 8. Why This Works

At any time:

- `tails[0]` = smallest tail of all increasing subsequences of length `1`
- `tails[1]` = smallest tail of all increasing subsequences of length `2`
- `tails[2]` = smallest tail of all increasing subsequences of length `3`
- and so on

If we improve a tail value, we never hurt the answer.

Why?

- Because a smaller tail is always at least as useful as a bigger tail for future extension

So:

- replace = improve existing subsequence ending value
- append = increase LIS length

Hence the length of `tails` at the end is exactly the LIS length.

---

## 9. Common Confusions

### Confusion 1: Is `tails` the actual LIS?

No.

- `tails` is only a helper structure for the length
- It stores best possible tail values

### Confusion 2: Why do we replace values?

Because a smaller ending value is better for the future.

Example:

- Length `2` subsequence ending with `3` is better than length `2` subsequence ending with `5`
- Both have same length
- But `3` has more chance to be extended

### Confusion 3: Why first `>= x`?

Because LIS is strictly increasing.

- If current tail is already `>= x`, then `x` can be a better/smaller replacement there

### Confusion 4: When does answer increase?

Only when `x` goes beyond all current tails.

- That means we found a longer increasing subsequence

---

## 10. If You Want Built-in Binary Search

Same logic using `Arrays.binarySearch`:

```java
import java.util.*;

class LIS_ONlogN_Length_BuiltIn {
    public int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;

        for (int x : nums) {
            int pos = Arrays.binarySearch(tails, 0, size, x);
            if (pos < 0) {
                pos = -pos - 1;
            }
            tails[pos] = x;
            if (pos == size) {
                size++;
            }
        }

        return size;
    }
}
```

Note:

- The insertion position from `binarySearch` gives exactly the place where we should replace or append

---

## 11. Reconstruction Comes After This

Once the length logic is clear, reconstruction becomes much easier.

For reconstruction, we need extra bookkeeping such as:

- which index ended a subsequence of a given length
- who is the predecessor of each element

But do not mix that with the core `tails` idea in the beginning.

First lock this in:

- `tails` is for LIS length
- binary search gives where current number fits
- replacing improves tail
- appending increases answer

Then reconstruction is just an additional layer on top.

---

## 12. Variants You Will See Later

- Reconstruct one actual LIS
- Number of LIS
- Longest Bitonic Subsequence
- Russian Doll Envelopes

These all build on the LIS thinking above.

---

## 13. Quick Takeaway

- `dp[i]` approach gives LIS length in `O(n^2)`
- `tails` approach gives LIS length in `O(n log n)`
- `tails[k]` stores the smallest possible tail for length `k + 1`
- `tails` is not the actual subsequence
- Answer = size of `tails`

---

# End of Notes
