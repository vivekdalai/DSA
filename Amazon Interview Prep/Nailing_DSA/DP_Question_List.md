# Amazon DP Interview Prep — Topic-wise Question List

Generated: 2026-08-03 · Scope: **in-person / onsite loop interview** (not the online
assessment — OA-specific framing was removed after confirming this is a loop round)

This list cross-references the DP notes already in [`01_DynamicProgramming/`](../../01_DynamicProgramming/)
against dynamic programming problems/patterns reported in recent (2025–2026) Amazon
onsite interview experiences. ✅ = already have a note in this repo (just review it).
⬜ = gap — no note yet, worth adding given it's been recently reported.

**Calibration note from research:** the onsite loop is typically 2–3 coding rounds of
45–60 minutes each (LeetCode-style, live/shared editor), plus a full behavioral loop
scored against Leadership Principles — every interviewer grades both the code *and*
an LP answer, so a technically perfect solution paired with a weak STAR answer can
still sink a round. On the coding side, DP shows up alongside sliding window,
two-pointers, hash maps, heaps, DFS/BFS, union-find, and greedy — and unlike the OA,
the loop is where the **meatier DP** (knapsack variants, interval DP, string-matching
DP, tree DP) is more likely to appear, especially as the interviewer probes with
follow-ups (brute force → memoized → tabulated → space-optimized). A reported
practical tip: Amazon interviewers reward **working code over optimal code** — get a
correct brute-force first, then optimize out loud, rather than stalling for the
"perfect" solution.

---

## 🎯 Priority — Recently Reported at Amazon (2025–2026)

| # | Problem | Status | Why it's here |
|---|---|---|---|
| 1 | **Word Break** (LC 139) | ✅ [DP_53](../../01_DynamicProgramming/DP_53_Word_Break_LC139.md) | Explicitly reported in 2026 Amazon question breakdowns (boolean DP, `dp[i]` = can segment `s[0:i]`); common starter that interviewers extend with follow-ups (return all segmentations, dictionary as trie) |
| 2 | **Jump Game / Jump Game II** (LC 55 / 45) | ✅ [DP_54](../../01_DynamicProgramming/DP_54_Jump_Game_I_II.md) | Reported in an Amazon SDE-1 interview (April 2026) — max reachable index / min jumps, interviewer explored recursive → memoized → optimized |
| 3 | Coin Change — Min Coins (LC 322) | ✅ [DP_04](../../01_DynamicProgramming/DP_04_Coin_Change_Min_Coins.md) | Repeatedly cited across 2025–2026 sources as a core Amazon DP question |
| 4 | Climbing Stairs (LC 70) | ✅ [DP_05](../../01_DynamicProgramming/DP_05_Climbing_Stairs_Count_Ways.md) | Named directly in OA prep guides as a recurring "basic DP" question |
| 5 | Longest Increasing Subsequence (LC 300) | ✅ [DP_44](../../01_DynamicProgramming/DP_44_LIS_Onlogn_and_Variants.md) | Reported as a harder OA slot question, ~30 min allocation |
| 6 | Longest Common Subsequence (LC 1143) | ✅ [DP_24](../../01_DynamicProgramming/DP_24_Longest_Common_Subsequence.md) | Came up in an Amazon SDE II interview writeup |
| 7 | House Robber I/II/III | ✅ [DP_08](../../01_DynamicProgramming/DP_08_House_Robber_I_II.md), [DP_49](../../01_DynamicProgramming/DP_49_House_Robber_III_Tree_DP.md) | Named as a classic "decision & state" DP Amazon favors |

---

## Topic-wise Breakdown

### 1. 1D Linear DP (foundation — highest OA relevance)
- ✅ Climbing Stairs — [DP_05](../../01_DynamicProgramming/DP_05_Climbing_Stairs_Count_Ways.md)
- ✅ House Robber I — [DP_08](../../01_DynamicProgramming/DP_08_House_Robber_I_II.md)
- ✅ House Robber II (circular) — [DP_08](../../01_DynamicProgramming/DP_08_House_Robber_I_II.md)
- ✅ Maximum Subarray (Kadane) — [DP_01](../../01_DynamicProgramming/DP_01_Maximum_Sum_Subarray_Kadane.md)
- ✅ Delete and Earn — [DP_03](../../01_DynamicProgramming/DP_03_Delete_and_Earn_House_Robber.md)
- ✅ Decode Ways — [DP_55](../../01_DynamicProgramming/DP_55_Decode_Ways_LC91.md)
- ✅ Perfect Squares — [DP_57](../../01_DynamicProgramming/DP_57_Perfect_Squares_LC279.md)

### 2. Grid / Path DP
- ✅ Unique Paths — [DP_10](../../01_DynamicProgramming/DP_10_Unique_Paths.md)
- ✅ Unique Paths with Obstacles — [DP_11](../../01_DynamicProgramming/DP_11_Unique_Paths_With_Obstacles.md)
- ✅ Min Path Sum — [DP_12](../../01_DynamicProgramming/DP_12_Min_Path_Sum_Grid.md)
- ✅ Triangle Min Path Sum — [DP_13](../../01_DynamicProgramming/DP_13_Triangle_Min_Path_Sum.md)
- ✅ Max Falling Path Sum — [DP_14](../../01_DynamicProgramming/DP_14_Max_Falling_Path_Sum.md)
- ✅ Cherry Pickup I / II — [DP_15](../../01_DynamicProgramming/DP_15_Cherry_Pickup_I.md), [DP_16](../../01_DynamicProgramming/DP_16_Cherry_Pickup_II.md)
- ✅ Dungeon Game — [DP_58](../../01_DynamicProgramming/DP_58_Dungeon_Game_LC174.md)

### 3. Knapsack Family
- ✅ 0/1 Knapsack — [DP_18](../../01_DynamicProgramming/DP_18_0_1_Knapsack.md)
- ✅ Subset Sum — [DP_19](../../01_DynamicProgramming/DP_19_Subset_Sum_Equals_K.md)
- ✅ Partition Equal Subset Sum — [DP_20](../../01_DynamicProgramming/DP_20_Equal_Sum_Partition_LC416.md)
- ✅ Minimum Subset Sum Difference — [DP_21](../../01_DynamicProgramming/DP_21_Min_Subset_Sum_Difference.md)
- ✅ Count Subsets Given Difference — [DP_22](../../01_DynamicProgramming/DP_22_Count_Subsets_Given_Difference.md)
- ✅ Target Sum — [DP_23](../../01_DynamicProgramming/DP_23_Target_Sum_LC494.md)
- ✅ Coin Change — Min Coins — [DP_04](../../01_DynamicProgramming/DP_04_Coin_Change_Min_Coins.md)
- ✅ Coin Change II — Number of Ways — [DP_04_B](../../01_DynamicProgramming/DP_04_B_Coin_Change_Number_of_Ways.md)
- ✅ Ones and Zeroes — [DP_56](../../01_DynamicProgramming/DP_56_Ones_And_Zeroes_LC474.md)

### 4. String / Sequence DP (Amazon likes framing these as text-processing/data problems)
- ✅ Longest Common Subsequence — [DP_24](../../01_DynamicProgramming/DP_24_Longest_Common_Subsequence.md) (+ [Print LCS, DP_25](../../01_DynamicProgramming/DP_25_Print_LCS.md))
- ✅ Longest Common Substring — [DP_26](../../01_DynamicProgramming/DP_26_Longest_Common_Substring.md)
- ✅ Longest Palindromic Subsequence — [DP_27](../../01_DynamicProgramming/DP_27_Longest_Palindromic_Subsequence.md)
- ✅ Longest Palindromic Substring — [DP_28](../../01_DynamicProgramming/DP_28_Longest_Palindromic_Substring.md)
- ✅ Min Insertions to Make Palindrome — [DP_29](../../01_DynamicProgramming/DP_29_Min_Insertions_To_Make_Palindrome.md)
- ✅ Edit Distance — [DP_30](../../01_DynamicProgramming/DP_30_Edit_Distance_LC72.md)
- ✅ Wildcard Matching — [DP_31](../../01_DynamicProgramming/DP_31_Wildcard_Matching.md)
- ✅ Regular Expression Matching — [DP_43](../../01_DynamicProgramming/DP_43_Regular_Expression_Matching_LC10.md)
- ✅ Distinct Subsequences — [DP_45](../../01_DynamicProgramming/DP_45_Distinct_Subsequences_LC115.md)
- ✅ Interleaving String — [DP_46](../../01_DynamicProgramming/DP_46_Interleaving_String_LC97.md)
- ✅ Shortest Common Supersequence — [DP_47](../../01_DynamicProgramming/DP_47_Shortest_Common_Supersequence_SCS_and_Print.md)
- ✅ Word Break — [DP_53](../../01_DynamicProgramming/DP_53_Word_Break_LC139.md)

### 5. Stock / Buy-Sell DP
- ✅ Best Time to Buy/Sell I–IV — [DP_33](../../01_DynamicProgramming/DP_33_Stocks_Best_Time_I_Single_Transaction.md) → [DP_37](../../01_DynamicProgramming/DP_37_Stocks_With_Transaction_Fee.md), [DP_52](../../01_DynamicProgramming/DP_52_Stocks_Best_Time_IV_K_Transactions.md)
- Fully covered — mentioned as a common Amazon "decision & state" family, review only.

### 6. Interval / Partition DP (more common in loop rounds than OA)
- ✅ Palindrome Partitioning II — [DP_40](../../01_DynamicProgramming/DP_40_Palindrome_Partitioning_II_Min_Cuts.md)
- ✅ Burst Balloons — [DP_41](../../01_DynamicProgramming/DP_41_Burst_Balloons_Interval_DP.md)
- ✅ Matrix Chain Multiplication — [DP_48](../../01_DynamicProgramming/DP_48_Matrix_Chain_Multiplication_MCM.md)
- ✅ Egg Drop — [DP_42](../../01_DynamicProgramming/DP_42_Egg_Dropping_SuperEggDrop.md)

### 7. Tree DP
- ✅ House Robber III — [DP_49](../../01_DynamicProgramming/DP_49_House_Robber_III_Tree_DP.md)
- ✅ Binary Tree Max Path Sum — [DP_51](../../01_DynamicProgramming/DP_51_Binary_Tree_Maximum_Path_Sum_LC124.md)

### 8. Jump Game family (greedy/DP boundary — interviewer often wants both angles)
- ✅ Jump Game / Jump Game II — [DP_54](../../01_DynamicProgramming/DP_54_Jump_Game_I_II.md)

### 9. Scheduling / LIS
- ✅ Weighted Job Scheduling — [DP_50](../../01_DynamicProgramming/DP_50_Max_Profit_Job_Scheduling_LC1235.md)
- ✅ LIS + O(n log n) variants — [DP_44](../../01_DynamicProgramming/DP_44_LIS_Onlogn_and_Variants.md)

---

## Gap Summary — status

All 6 originally-flagged gap problems now have full `DP_XX_...` notes in
`01_DynamicProgramming/`, written in the same template as the rest of the repo
(problem understanding → state → recurrence → base cases → Java implementations →
dry run → pattern recognition → edge cases → takeaway):

1. ✅ **Word Break** (LC 139) — [DP_53](../../01_DynamicProgramming/DP_53_Word_Break_LC139.md)
2. ✅ **Jump Game / Jump Game II** (LC 55 / 45) — [DP_54](../../01_DynamicProgramming/DP_54_Jump_Game_I_II.md)
3. ✅ **Decode Ways** (LC 91) — [DP_55](../../01_DynamicProgramming/DP_55_Decode_Ways_LC91.md)
4. ✅ **Ones and Zeroes** (LC 474) — [DP_56](../../01_DynamicProgramming/DP_56_Ones_And_Zeroes_LC474.md)
5. ✅ **Perfect Squares** (LC 279) — [DP_57](../../01_DynamicProgramming/DP_57_Perfect_Squares_LC279.md)
6. ✅ **Dungeon Game** (LC 174) — [DP_58](../../01_DynamicProgramming/DP_58_Dungeon_Game_LC174.md)

100% coverage of everything flagged from the research pass — the repo's DP notes now
span the full pattern surface reported for Amazon's onsite loop.

---

## Sources

- [30 Amazon LeetCode Interview Questions for 2026 — Verve AI](https://www.vervecopilot.com/blog/amazon-leetcode-interview-questions)
- [Amazon Interview Question: Dynamic Programming Questions — Glassdoor](https://www.glassdoor.com/Interview/Dynamic-Programming-Questions-QTN_780822.htm)
- [Top 18 Amazon Coding Interview Questions with Solutions — DesignGurus](https://www.designgurus.io/blog/amazon-top-coding-interview-questions)
- [Amazon Coding Round Questions 2026: Patterns + Solutions — PapersAdda](https://papersadda.com/article/amazon-coding-round-questions-2026/)
- [Amazon DS & Algo Round Interview Questions Asked In 2026 — CodeZym](https://codezym.com/blog/15-amazon-dsa-interview-questions-2026)
- [The Amazon OA cheat sheet: Every pattern I've seen in 2026 — Fahim ul Haq (Medium)](https://medium.com/@fahimulhaq/the-amazon-oa-cheat-sheet-every-pattern-ive-seen-in-2026-065ef2abd575)
- [Amazon OA Questions 2026: Real Problems, Patterns & a 6-Week Study Plan — Shadecoder](https://www.shadecoder.com/blogs/amazon-oa-questions-2026-real-problems-patterns-a-6-week-study-plan)
- [Amazon OA Guide 2025: OA1 & OA2 Format, Real Questions — Shadecoder](https://www.shadecoder.com/blogs/amazon-oa-oa1-oa2-complete-guide-%E2%80%94-questions-format-what-actually-gets-you-to-onsite)
- [Top 120 Most Frequently Asked Amazon SDE-1 Interview Questions (2022–2025) — LeetCode Discuss](https://leetcode.com/discuss/post/7474246/top-120-most-frequently-asked-amazon-sde-imwy/)
- [Amazon SDE-1 (Feb 2025) Interview Experience — LeetCode Discuss](https://leetcode.com/discuss/post/6461439/amazon-sde-1-feb-2025-interview-experien-wv10/)
- [Amazon SDE-1/2 interview questions 2025 — LeetCode Discuss](https://leetcode.com/discuss/post/6752237/amazon-interview-questions-2025-by-anony-qgzt/)
- [Amazon SDE II Interview Experience — LeetCode Discuss](https://leetcode.com/discuss/interview-question/875845/amazon-sde-ii-interview-experience)
- [Top 20 Dynamic Programming Interview Questions — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/top-20-dynamic-programming-interview-questions/)
- [Amazon SDE Sheet: Interview Questions and Answers — GeeksforGeeks](https://www.geeksforgeeks.org/dsa/amazon-sde-sheet-interview-questions-and-answers/)
- [Dynamic Programming Questions at Amazon: What to Expect — CodeJeet](https://codejeet.com/blog/amazon-dynamic-programming-interview-questions)
- [Amazon L5 Interview Guides & Questions (2026) — Hello Interview](https://www.hellointerview.com/guides/amazon/l5)
- [How to Ace Amazon SDE Interview: Full 2026 Prep Guide — Interview Kickstart](https://interviewkickstart.com/interview-questions/companies/amazon/sde-interview-guide)
- [Amazon SDE Interview 2026: New Loop Format & Leadership Principles That Matter Most — Topalupu](https://www.topalupu.com/blog/amazon-sde-interview-2026)
