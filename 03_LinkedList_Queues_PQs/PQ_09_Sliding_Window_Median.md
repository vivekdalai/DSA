# Priority Queue Notes

## 09 - Sliding Window Median

Problem: [LeetCode 480 - Sliding Window Median](https://leetcode.com/problems/sliding-window-median/description/)

---

## 1. Problem, In One Line

Given `nums` and a window size `k`, return the median of every contiguous window of length `k` as it slides across the array, one position at a time.

**Example**

    Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
    Output: [1,-1,-1,3,5,6]

    Input:  nums = [1,2,3,4,2,3,1,4,2], k = 3
    Output: [2,3,3,3,2,3,2]

---

## 2. Core Idea

Same two-heap split as `PQ_07_Find_Median_From_Data_Stream.md` — a max-heap for the lower half, a min-heap for the upper half, kept within one element of each other so the median is always at a top.

The difference that makes this problem genuinely harder: a data stream only ever **grows**, but a sliding window also **shrinks** — every step both adds `nums[right]` and must remove `nums[left]`. Removing an arbitrary value from the *middle* of a `PriorityQueue` isn't a supported `O(log n)` operation, which is where most attempts (including the one that follows) go wrong.

---

## 3. Why The Original Version Has Three Real Bugs

The submitted code used the standard "lazy deletion" trick — mark a value as removed in a frequency map, and only actually pop it once it surfaces at a heap's top. That trick is legitimate in general, but this particular implementation had three separate problems stacked on top of each other. Verified against a brute-force reference (sort each window directly) across tens of thousands of random cases.

**Bug 1 — off-by-one on the result array.** The pre-loop code correctly computes `result[0]` for the first window. But inside the sliding loop, `idx` starts at `0` too — so the very first loop iteration immediately **overwrites** `result[0]` with the *second* window's median, every subsequent window's answer lands one slot too early, and the *last* window's median is never computed at all (the array's final slot is left at Java's default `0.0`). This alone was enough to fail the textbook example:

    Input:  nums = [1,3,-1,-3,5,3,6,7], k = 3
    original code -> [-1.0, -1.0, 0.0, 4.0, 4.5, 0.0]
    correct        -> [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]

Fix: `idx` must start at `1`, since index `0` is already filled before the loop begins.

**Bug 2 — `getMedian` only handles one direction of imbalance.** It checks `maxHeap.size() > minHeap.size()` and falls back to averaging otherwise — but the rebalancing in `addElement` is *symmetric* (either heap is allowed to end up one larger), so `minHeap` can legitimately be the bigger side. When that happens, `getMedian` incorrectly averages two values instead of returning the single correct middle element — or worse, if `maxHeap` happens to be empty while `minHeap` still holds the window's only element, it returns a hardcoded `0.0` without even looking at `minHeap`. Concrete trace with `k = 1` (every window is a single element, so the median should just be that element): after the first slide, `maxHeap` becomes empty because its one element was lazily removed, `minHeap` holds the correct value `4`, and `getMedian` returns `0.0` anyway.

Fix: make the check symmetric — `minHeap` bigger returns `minHeap.peek()`, not just the reverse case.

**Bug 3 — lazy deletion via a shared frequency map is ambiguous with duplicates.** This is the deep one. Fixing bugs 1 and 2 alone still failed **60% of random trials** (30,034 / 50,000). The reason: when a value appears more than once in the current window, its copies can be split — one sitting in `maxHeap`, another in `minHeap`. A single shared `freqMap` only tracks *how many total copies remain in the window*, not *which heap's specific copy should be considered gone*. So when one copy leaves the window, the frequency count drops correctly, but pruning has no way to know whether it was the `maxHeap` copy or the `minHeap` copy that actually expired — the two heaps' bookkeeping can silently drift apart. Trying to patch this with explicit `maxHeapSize`/`minHeapSize` counters (instead of trusting `heap.size()`, which also counts un-pruned zombies) still isn't enough on its own — attempting it produced heap/counter states so inconsistent that `peek()` was called on an empty heap and threw `NullPointerException` during testing.

Fix: naively patching this (explicit `smallSize`/`largeSize` counters, incrementally adjusted) is not enough on its own — see Section 4 for the robust fix. It **is** possible to stay with plain `PriorityQueue`s and dodge this bug with a fundamentally different invariant-tracking design instead of switching structures — see Approach 2 in Section 7 — but that design is considerably easier to get subtly wrong than to verify was right in the first place.

---

## 4. What Goes In The Structures

Because plain `PriorityQueue` has no way to remove a specific value by identity (only "the current top"), the robust fix swaps both heaps for **`TreeMap<Integer, Integer>` used as an ordered multiset**:

- `small` — a `TreeMap` for the lower half. `small.lastKey()` is its maximum (the max-heap top equivalent).
- `large` — a `TreeMap` for the upper half. `large.firstKey()` is its minimum (the min-heap top equivalent).
- Each map's value is "how many copies of this key are currently in this half" — `merge(val, 1, Integer::sum)` to add, decrement-or-remove-the-key to delete.
- `smallSize` / `largeSize` — plain counters for total occupancy (a `TreeMap`'s own `.size()` counts distinct keys, not total copies, so this can't be skipped).

Removing `nums[left]` is now an **exact** `O(log k)` operation — decrement its count in whichever map it's in, no ambiguity about which physical "copy" is gone, because a `TreeMap` doesn't have copies at all, just a count per key.

---

## 5. The Algorithm, Step By Step

1. Insert the first `k` elements one at a time via `addNum` (classify against `small.lastKey()`, rebalance after each).
2. Record the median of the first window.
3. For each subsequent position: `addNum(nums[right])`, then `removeNum(nums[left])` (classify the same way, decrement, rebalance), then record the median.
4. `rebalance()` keeps `smallSize` within `[largeSize, largeSize + 1]` by moving `small.lastKey()` → `large` or `large.firstKey()` → `small` as needed.

---

## 6. Code

```java
import java.util.*;

class Solution {
    private TreeMap<Integer, Integer> small = new TreeMap<>(); // lower half; max = small.lastKey()
    private TreeMap<Integer, Integer> large = new TreeMap<>(); // upper half; min = large.firstKey()
    private int smallSize = 0, largeSize = 0;

    public double[] medianSlidingWindow(int[] nums, int k) {
        for (int i = 0; i < k; i++) {
            addNum(nums[i]);
        }

        double[] result = new double[nums.length - k + 1];
        result[0] = getMedian();

        for (int right = k; right < nums.length; right++) {
            addNum(nums[right]);
            removeNum(nums[right - k]);
            result[right - k + 1] = getMedian();
        }
        return result;
    }

    private void addNum(int num) {
        if (smallSize == 0 || num <= small.lastKey()) {
            small.put(num, small.getOrDefault(num, 0) + 1);
            smallSize++;
        } else {
            large.put(num, large.getOrDefault(num, 0) + 1);
            largeSize++;
        }
        rebalance();
    }

    private void removeNum(int num) {
        if (num <= small.lastKey()) {
            decrement(small, num);
            smallSize--;
        } else {
            decrement(large, num);
            largeSize--;
        }
        rebalance();
    }

    private void decrement(TreeMap<Integer, Integer> map, int key) {
        int count = map.get(key);
        if (count == 1) map.remove(key);
        else map.put(key, count - 1);
    }

    private void rebalance() {
        if (smallSize > largeSize + 1) {
            int moved = small.lastKey(); //lastKey() --> the largest key currently in map :: O(log N)
            decrement(small, moved);
            large.put(moved, large.getOrDefault(moved, 0) + 1);
            smallSize--; largeSize++;
        } else if (largeSize > smallSize) {
            int moved = large.firstKey(); //firstKey() --> the smallest key currently in map :: O(log N)
            decrement(large, moved);
            small.put(moved, small.getOrDefault(moved, 0) + 1);
            largeSize--; smallSize++;
        }
    }

    private double getMedian() {
        if (smallSize > largeSize) {
            return 1.0 * small.lastKey();
        }
        return ((long) small.lastKey() + (long) large.firstKey()) / 2.0;
    }
}
```

Complexity:
- Time: `O(n log k)` — each of the `n` elements is inserted once and removed once, each op `O(log k)` since both maps never hold more than `k` total entries.
- Space: `O(k)` across both maps.

Verified against a brute-force reference (sort each window directly) across 200,000 randomized trials, including heavy-duplicate inputs (small value ranges forcing many repeats across both halves) specifically to stress-test Bug 3's failure mode, **plus a further 50,000 trials at the extremes of the `int` range** — zero mismatches in either pass. That second pass exists because the first version of this code averaged the two middle keys as `(small.lastKey() + large.firstKey()) / 2.0`, which silently overflows `int` before the `/ 2.0` ever promotes it to `double` — e.g. `-2147483646 + -2147483647` wraps around to `3` instead of the true `-4294967293`, producing a wildly wrong median. Casting each key to `long` before adding (as above) is the fix; see Section 9 for the full trace.

---

## 7. Approach 2 — Balance-Delta Reconciliation (Stays With `PriorityQueue`)

There's a second correct approach that avoids `TreeMap` entirely and stays with two plain `PriorityQueue`s plus lazy deletion — and it genuinely survives the Bug 3 failure mode, for a specific structural reason rather than luck. Verified against brute force across 200,000 heavy-duplicate trials, zero mismatches, plus it matches both official examples.

**Why this one doesn't drift like the original submission did:** it never maintains a *persistent* `smallSize`/`largeSize` counter that gets incrementally adjusted round over round (that's exactly what drifted out of sync in the failed patch attempt from Section 3). Instead, every round computes a **fresh** `balance` delta from scratch:

- Classify `outNum` (the value leaving the window) as belonging to `smallList` or `largeList` by comparing it against `smallList.peek()` — contributing `-1` or `+1` to `balance`.
- Classify `inNum` (the value entering) the same way, physically inserting it immediately — contributing the opposite sign.
- Apply **one** compensating move based on the sign of the combined `balance`, then reset `balance = 0` for next round.

This is safe specifically because the cleanup loops run at the **end of every round**, so `smallList.peek()` / `largeList.peek()` are always guaranteed fresh (never a zombie) at the moment the *next* round reads them for classification or for the median itself. And because `int`s have no identity beyond their value, it never actually matters *which physical copy* of a duplicated value is the one lazily swept away later — `outgoingNum` tracks a removal credit by value, and paying down that credit against whichever heap's top happens to match is equally correct regardless of which specific occurrence it "really" was. `.size()` is only ever used here as an emptiness guard, never trusted as a running total — which is exactly the trust that broke the earlier patch attempt.

Bugs 1 and 2 from Section 3 don't reappear here either, essentially by construction: there's no separate pre-loop `result[0]` write to fall out of sync with a loop-internal index (the median is recorded once per iteration of a single loop, first window included), and the rebalance is driven by this round's fresh `balance` sign rather than a stale "which heap is bigger" size comparison.

```java
import java.util.*;
import java.util.stream.*;

class SlidingWindow {
    public static double[] medianSlidingWindow(int[] nums, int k) {
        List<Double> medians = new ArrayList<>();
        HashMap<Integer, Integer> outgoingNum = new HashMap<>();
        PriorityQueue<Integer> smallList = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> largeList = new PriorityQueue<>();

        for (int i = 0; i < k; i++)
            smallList.offer(nums[i]);
        for (int i = 0; i < k / 2; i++)
            largeList.offer(smallList.poll());

        int balance = 0;
        int i = k;
        while (true) {
            if ((k & 1) == 1)
                medians.add((double) smallList.peek());
            else
                medians.add(((long) smallList.peek() + (long) largeList.peek()) * 0.5);

            if (i >= nums.length) break;

            int outNum = nums[i - k];
            int inNum = nums[i];
            i++;

            if (outNum <= smallList.peek()) balance -= 1;
            else balance += 1;
            outgoingNum.merge(outNum, 1, Integer::sum);

            if (smallList.size() > 0 && inNum <= smallList.peek()) {
                balance += 1;
                smallList.offer(inNum);
            } else {
                balance -= 1;
                largeList.offer(inNum);
            }

            if (balance < 0) smallList.offer(largeList.poll());
            else if (balance > 0) largeList.offer(smallList.poll());
            balance = 0;

            while (smallList.size() > 0 && outgoingNum.getOrDefault(smallList.peek(), 0) > 0)
                outgoingNum.put(smallList.peek(), outgoingNum.get(smallList.poll()) - 1);
            while (largeList.size() > 0 && outgoingNum.getOrDefault(largeList.peek(), 0) > 0)
                outgoingNum.put(largeList.peek(), outgoingNum.get(largeList.poll()) - 1);
        }

        return medians.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
```

(The original submission's own test driver referenced an undefined `PrintHyphens` helper class in `main` — unrelated to the algorithm itself, just a missing local utility; omitted here since this file follows the repo's convention of leaving `main` out unless it clarifies usage.)

Complexity:
- Time: `O(n log k)` — same shape as Approach 1, heap push/pop bounded by window size.
- Space: `O(k)` across both heaps and the pending-removal map.

**Approach 1 vs. Approach 2** — both are `O(n log k)` and fully correct; the real trade-off is which failure mode you're more comfortable defending in an interview. `TreeMap` (Approach 1) makes the "no ambiguity" property structurally obvious from the data structure choice itself — easier to explain and to get right under pressure. The balance-delta approach (Approach 2) stays with the more "expected" two-heap shape, but its correctness rests on an invariant (fresh-tops-every-round, never-trust-accumulated-size) that's easy to state once you've seen it and easy to break subtly if you're deriving it live, as the failed patch attempt in Section 3 demonstrates firsthand. One point in Approach 2's favor: its median line was written with `(long)` casts (`((long) smallList.peek() + (long) largeList.peek()) * 0.5`) from the start, so it was never exposed to the `int`-overflow bug documented in Section 9 — Approach 1's code originally wasn't, and had to be fixed.

---

## 8. Dry Run

Input: `nums = [1,3,-1,-3,5,3,6,7], k = 3`

| Window | `small` (lower half) | `large` (upper half) | Median |
|---|---|---|---|
| `[1,3,-1]` | `{-1,1}` | `{3}` | `1.0` |
| `[3,-1,-3]` | `{-3,-1}` | `{3}` | `-1.0` |
| `[-1,-3,5]` | `{-3,-1}` | `{5}` | `-1.0` |
| `[-3,5,3]` | `{-3,3}` | `{5}` | `3.0` |
| `[5,3,6]` | `{3,5}` | `{6}` | `5.0` |
| `[3,6,7]` | `{3,6}` | `{7}` | `6.0` |

Result: `[1.0, -1.0, -1.0, 3.0, 5.0, 6.0]` — matches the expected output exactly.

---

## 9. Edge Cases and Pitfalls

- `k == 1`: every window is a single element; `large` stays empty the whole time, and `getMedian` must return `small.lastKey()` directly — this is exactly the case that exposed Bug 2 above.
- Duplicate values split across both halves are the entire reason `TreeMap` is used instead of `PriorityQueue` here — don't "simplify" back to two heaps with a shared frequency map without re-deriving why that fails (Bug 3).
- Classification for both insert and remove uses the **same** rule (`num <= small.lastKey()`) — using a different comparison for removal than insertion is an easy way to reintroduce Bug 3's drift even with `TreeMap`s.
- `small` is guaranteed non-empty whenever the window is non-empty (it always holds the "extra" element on odd sizes and ties on even sizes), so `small.lastKey()` is always safe to call once at least one element has been added — but only after the first `addNum`, not before.
- **`int` overflow when averaging the two middle keys — a real bug that shipped in this note's first version.** `(small.lastKey() + large.firstKey()) / 2.0` adds two `Integer`s as plain `int` arithmetic *before* the `/ 2.0` ever promotes anything to `double` — the promotion happens one step too late. With values near the ends of the `int` range this silently wraps instead of throwing: `nums = [2147483647,-14756,21474,-2147483646,-2147483647,-5555,9999], k = 2` produces a window `[-2147483646, -2147483647]` whose true sum is `-4294967293`, but as 32-bit `int` arithmetic that wraps to `3`, giving a median of `1.5` instead of the correct `-2147483646.5`. 200,000 stress-test trials never caught this because they only ever used small value ranges (roughly -10 to 10) — the bug needed values near `Integer.MIN_VALUE`/`Integer.MAX_VALUE` to surface, which a real submission's test cases can absolutely include even when your own stress test doesn't think to. Fix: cast each key to `long` *before* adding, exactly like Approach 2's median line already did (`((long) small.lastKey() + (long) large.firstKey()) / 2.0`) — cheap insurance to apply to *any* "average two numbers" line, not just this one.

---

## 10. Complexity And Pattern

Pattern:

- two-structure "split down the middle" median tracking — same family as `PQ_07_Find_Median_From_Data_Stream.md`, but the *removal* requirement is what forces extra care that streaming-only median tracking never needed.
- General lesson: lazy deletion (mark-and-skip-on-peek) becomes unreliable the moment the same value can independently exist in **two** different structures that need to stay in sync — unless you either (a) switch to something with exact, addressable removal (`TreeMap`, an indexed heap, a balanced BST — Approach 1), or (b) very carefully never trust an accumulated size/count across rounds, only ever a freshly-recomputed one against freshly-cleaned tops (Approach 2). Reach for (a) by default; reach for (b) only once you can state precisely why it's safe, not just that it happened to pass.

Trigger words:
- "median of every window of size k", "sliding window" combined with "median" or "middle element"
- Any two-heap streaming problem that gets a "now also support removing an element" follow-up — that follow-up is usually the interviewer testing whether you reach for lazy-deletion blindly or recognize when it breaks.

---

## End of Notes
