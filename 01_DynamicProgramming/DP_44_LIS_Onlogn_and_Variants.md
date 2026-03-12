# Dynamic Programming Notes

## 44 - Longest Increasing Subsequence (LIS) O(n log n) + Variants

**Generated on:** 2026-02-25 23:14:06 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `nums`, find:
- Length of the Longest Increasing Subsequence (strictly increasing)
- Optionally reconstruct one valid LIS

Examples:
- [10,9,2,5,3,7,101,18] → LIS length = 4 (e.g., [2,3,7,101])

Classic variants:
- Reconstruct actual LIS (not just length)
- Longest Bitonic Subsequence (LBS)
- Russian Doll Envelopes (LIS on pairs after sorting with tie-breaks)

------------------------------------------------------------------------

## 🪜 2. LIS Approaches Overview

A) O(n^2) DP (length only or with reconstruction)
- `dp[i] = 1 + max(dp[j]) for all j < i with nums[j] < nums[i]`
- Track predecessor to reconstruct

B) O(n log n) Patience Sorting (length optimal; can reconstruct with extra bookkeeping)
- Maintain tail array `tails[len] = smallest tail value of an increasing subsequence of length len+1`
- For each `x`, binary search in tails to find position `pos`
- Update tails[pos] = x
- Length = size of tails at end
- To reconstruct sequence, also keep:
  - `posAt[i] = LIS length index ending at i`
  - `prev[i] = predecessor index`
  - `ends[len] = index of array where subsequence of length len ends`

------------------------------------------------------------------------

## 🔁 3. O(n^2) DP (Length + Reconstruction)

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

    public List<Integer> lisSequence(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        int bestLen = 0;
        int bestEnd = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > bestLen) {
                bestLen = dp[i];
                bestEnd = i;
            }
        }

        LinkedList<Integer> seq = new LinkedList<>();
        for (int i = bestEnd; i != -1; i = prev[i]) {
            seq.addFirst(nums[i]);
        }
        return seq;
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n)

------------------------------------------------------------------------

## ⚡ 4. O(n log n) LIS (Length) + Reconstruction

Trick to reconstruct:
- `tails[len]`: stores tail values (for length only)
- `ends[len]`: stores index in original array that ends a subsequence of length `len+1` with minimal tail
- `posAt[i]`: the LIS length index assigned to element i (0-based, so +1 gives length)
- `prev[i]`: predecessor index to chain back

```java
import java.util.*;

class LIS_ONlogN_Reconstruct {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] tails = new int[n];
        int size = 0;
        for (int x : nums) {
            int pos = Arrays.binarySearch(tails, 0, size, x);
            if (pos < 0) pos = -pos - 1;
            tails[pos] = x;
            if (pos == size) size++;
        }
        return size;
    }

    public List<Integer> lisSequence(int[] nums) {
        int n = nums.length;
        int[] ends = new int[n];   // index in nums that ends a LIS of given length
        int[] posAt = new int[n];  // LIS-length index assigned to i
        int[] prev = new int[n];   // predecessor index
        Arrays.fill(prev, -1);

        int size = 0;

        // We need tails array values to search positions by value, but to reconstruct we track ends indices.
        // We'll binary search over values in tails but also maintain ends so we can set prev properly.
        int[] tails = new int[n]; // will hold values of last elements
        for (int i = 0; i < n; i++) {
            int x = nums[i];
            int pos = Arrays.binarySearch(tails, 0, size, x);
            if (pos < 0) pos = -pos - 1;

            // link predecessor: index of end of subsequence of length pos
            if (pos > 0) prev[i] = ends[pos - 1];

            tails[pos] = x;
            ends[pos] = i;
            posAt[i] = pos;

            if (pos == size) size++;
        }

        // Reconstruct LIS of length 'size' by walking prev from ends[size-1]
        LinkedList<Integer> seq = new LinkedList<>();
        int cur = ends[size - 1];
        while (cur != -1) {
            seq.addFirst(nums[cur]);
            cur = prev[cur];
        }
        return seq;
    }

    public static void main(String[] args) {
        LIS_ONlogN_Reconstruct solver = new LIS_ONlogN_Reconstruct();
        int[] arr = {10,9,2,5,3,7,101,18};
        System.out.println(solver.lengthOfLIS(arr));   // 4
        System.out.println(solver.lisSequence(arr));   // [2,3,7,101]
    }
}
```

Complexity:
- Time: O(n log n)
- Space: O(n)

Notes:
- Strictly increasing: use binarySearch on `tails` (lower_bound).
- Non-decreasing LIS can be handled by using upper_bound comparisons.

------------------------------------------------------------------------

## 🔀 5. Variant: Longest Bitonic Subsequence (LBS)

- Bitonic = first strictly increasing, then strictly decreasing
- Idea:
  - `LISleft[i]` = LIS length ending at i (left-to-right)
  - `LISright[i]` = LIS length starting at i (right-to-left on reversed or compute LDS)
  - LBS at i = `LISleft[i] + LISright[i] - 1`
- Answer = max over i of LBS[i]

O(n^2) version:

```java
import java.util.*;

class LongestBitonicSubsequence {
    public int lbs(int[] nums) {
        int n = nums.length;
        int[] lis = new int[n];
        int[] lds = new int[n];
        Arrays.fill(lis, 1);
        Arrays.fill(lds, 1);

        // LIS left to right
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    lis[i] = Math.max(lis[i], lis[j] + 1);
                }
            }
        }
        // LDS right to left (strictly decreasing => compare reversed)
        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j > i; j--) {
                if (nums[j] < nums[i]) {
                    lds[i] = Math.max(lds[i], lds[j] + 1);
                }
            }
        }

        int best = 0;
        for (int i = 0; i < n; i++) {
            best = Math.max(best, lis[i] + lds[i] - 1);
        }
        return best;
    }
}
```

------------------------------------------------------------------------

## 💌 6. Variant: Russian Doll Envelopes (LC 354)

- Given envelopes `envelopes[i] = (w, h)`, nest one inside another if both `w` and `h` are strictly increasing.
- Sort by `w` ascending and on ties by `h` descending (to avoid using equal `w` in LIS on `h`)
- Then compute LIS on heights `h` with strict increase.

```java
import java.util.*;

class RussianDollEnvelopes {
    public int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a,b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(b[1], a[1]); // tie-break: h desc
        });

        // LIS on heights with strict increase
        int[] tails = new int[envelopes.length];
        int size = 0;
        for (int[] e : envelopes) {
            int h = e[1];
            int pos = Arrays.binarySearch(tails, 0, size, h);
            if (pos < 0) pos = -pos - 1;
            tails[pos] = h;
            if (pos == size) size++;
        }
        return size;
    }
}
```

Complexity:
- Sorting O(n log n), LIS O(n log n), so O(n log n) total.

------------------------------------------------------------------------

## 🧪 7. Edge Cases and Pitfalls

- Strict vs non-decreasing subsequence: adjust comparison/binary search accordingly
- For reconstruction in O(n log n), ensure predecessors are set using `ends[pos-1]` and record original indices
- Russian Dolls: Always tie-break heights descending when widths equal; otherwise equal-width items chain incorrectly

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: LIS (Patience Sorting), Reconstruction, and Variants (Bitonic, Russian Dolls)
- Family: Sequence DP with O(n log n) optimization
- Triggers:
  - Strict ordering constraints
  - Pairs sorting + 1-D LIS on second key

------------------------------------------------------------------------

## ✅ 9. Takeaway

- For LIS length, use patience sorting O(n log n)
- For LIS reconstruction, track `ends`, `prev`, and `posAt`
- LBS via LIS+LDS; Russian Dolls via sort+LIS on height (with tie-breaks)

------------------------------------------------------------------------

# End of Notes
