# Dynamic Programming Problems

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Core Idea

Dynamic Programming is useful when:
- the problem has overlapping subproblems
- the answer can be built from smaller states
- we can define a state and recurrence clearly

Typical workflow:
1. define the state
2. write the recurrence
3. handle base cases
4. choose memoization or tabulation
5. optimize space if possible

------------------------------------------------------------------------

## 2. Common Patterns

- 1D DP
  - Fibonacci style, take / not-take, linear transitions
- Grid DP
  - move through a matrix with local transitions
- Subsequence / String DP
  - compare prefixes or suffixes of strings
- Knapsack / Subset DP
  - choose or skip items under constraints
- Stock DP
  - state based on day, buy/sell status, and transactions
- Interval DP
  - solve ranges and combine partitions
- Tree DP
  - solve using node-level states

------------------------------------------------------------------------

## 3. Problems in This Folder

### 1D DP / Basic Recurrence

1. [DP_01_Maximum_Sum_Subarray_Kadane.md](./DP_01_Maximum_Sum_Subarray_Kadane.md)
2. [DP_02_Max_Product_Subarray.md](./DP_02_Max_Product_Subarray.md)
3. [DP_03_Delete_and_Earn_House_Robber.md](./DP_03_Delete_and_Earn_House_Robber.md)
4. [DP_05_Climbing_Stairs_Count_Ways.md](./DP_05_Climbing_Stairs_Count_Ways.md)
5. [DP_06_Frog_Jump_Min_Energy.md](./DP_06_Frog_Jump_Min_Energy.md)
6. [DP_07_Max_Sum_Without_Adjacents.md](./DP_07_Max_Sum_Without_Adjacents.md)
7. [DP_08_House_Robber_I_II.md](./DP_08_House_Robber_I_II.md)
8. [DP_09_Geeks_Training_Max_Points.md](./DP_09_Geeks_Training_Max_Points.md)
9. [DP_17_Candy_Distribution_LC135.md](./DP_17_Candy_Distribution_LC135.md)
10. [DP_32_Longest_Divisible_Subset.md](./DP_32_Longest_Divisible_Subset.md)
11. [DP_38_Frog_Jump_II_RoundTrip_NoRepeat.md](./DP_38_Frog_Jump_II_RoundTrip_NoRepeat.md)
12. [DP_39_Frog_Jump_403.md](./DP_39_Frog_Jump_403.md)
13. [DP_44_LIS_Onlogn_and_Variants.md](./DP_44_LIS_Onlogn_and_Variants.md)

### Grid DP

1. [DP_10_Unique_Paths.md](./DP_10_Unique_Paths.md)
2. [DP_11_Unique_Paths_With_Obstacles.md](./DP_11_Unique_Paths_With_Obstacles.md)
3. [DP_12_Min_Path_Sum_Grid.md](./DP_12_Min_Path_Sum_Grid.md)
4. [DP_13_Triangle_Min_Path_Sum.md](./DP_13_Triangle_Min_Path_Sum.md)
5. [DP_14_Max_Falling_Path_Sum.md](./DP_14_Max_Falling_Path_Sum.md)
6. [DP_15_Cherry_Pickup_I.md](./DP_15_Cherry_Pickup_I.md)
7. [DP_16_Cherry_Pickup_II.md](./DP_16_Cherry_Pickup_II.md)

### Knapsack / Subset DP

1. [DP_04_B_Coin_Change_Number_of_Ways.md](./DP_04_B_Coin_Change_Number_of_Ways.md)
2. [DP_04_Coin_Change_Min_Coins.md](./DP_04_Coin_Change_Min_Coins.md)
3. [DP_18_0_1_Knapsack.md](./DP_18_0_1_Knapsack.md)
4. [DP_19_Subset_Sum_Equals_K.md](./DP_19_Subset_Sum_Equals_K.md)
5. [DP_20_Equal_Sum_Partition_LC416.md](./DP_20_Equal_Sum_Partition_LC416.md)
6. [DP_21_Min_Subset_Sum_Difference.md](./DP_21_Min_Subset_Sum_Difference.md)
7. [DP_22_Count_Subsets_Given_Difference.md](./DP_22_Count_Subsets_Given_Difference.md)
8. [DP_23_Target_Sum_LC494.md](./DP_23_Target_Sum_LC494.md)

### String / Sequence DP

1. [DP_24_Longest_Common_Subsequence.md](./DP_24_Longest_Common_Subsequence.md)
2. [DP_25_Print_LCS.md](./DP_25_Print_LCS.md)
3. [DP_26_Longest_Common_Substring.md](./DP_26_Longest_Common_Substring.md)
4. [DP_27_Longest_Palindromic_Subsequence.md](./DP_27_Longest_Palindromic_Subsequence.md)
5. [DP_28_Longest_Palindromic_Substring.md](./DP_28_Longest_Palindromic_Substring.md)
6. [DP_29_Min_Insertions_To_Make_Palindrome.md](./DP_29_Min_Insertions_To_Make_Palindrome.md)
7. [DP_30_Edit_Distance_LC72.md](./DP_30_Edit_Distance_LC72.md)
8. [DP_31_Wildcard_Matching.md](./DP_31_Wildcard_Matching.md)
9. [DP_40_Palindrome_Partitioning_II_Min_Cuts.md](./DP_40_Palindrome_Partitioning_II_Min_Cuts.md)
10. [DP_43_Regular_Expression_Matching_LC10.md](./DP_43_Regular_Expression_Matching_LC10.md)
11. [DP_45_Distinct_Subsequences_LC115.md](./DP_45_Distinct_Subsequences_LC115.md)
12. [DP_46_Interleaving_String_LC97.md](./DP_46_Interleaving_String_LC97.md)
13. [DP_47_Shortest_Common_Supersequence_SCS_and_Print.md](./DP_47_Shortest_Common_Supersequence_SCS_and_Print.md)

### Stock DP

1. [DP_33_Stocks_Best_Time_I_Single_Transaction.md](./DP_33_Stocks_Best_Time_I_Single_Transaction.md)
2. [DP_34_Stocks_Best_Time_II_Infinite_Transactions.md](./DP_34_Stocks_Best_Time_II_Infinite_Transactions.md)
3. [DP_35_Stocks_Best_Time_III_Two_Transactions.md](./DP_35_Stocks_Best_Time_III_Two_Transactions.md)
4. [DP_36_Stocks_With_Cooldown.md](./DP_36_Stocks_With_Cooldown.md)
5. [DP_37_Stocks_With_Transaction_Fee.md](./DP_37_Stocks_With_Transaction_Fee.md)
6. [DP_52_Stocks_Best_Time_IV_K_Transactions.md](./DP_52_Stocks_Best_Time_IV_K_Transactions.md)

### Interval / Partition DP

1. [DP_41_Burst_Balloons_Interval_DP.md](./DP_41_Burst_Balloons_Interval_DP.md)
2. [DP_42_Egg_Dropping_SuperEggDrop.md](./DP_42_Egg_Dropping_SuperEggDrop.md)
3. [DP_48_Matrix_Chain_Multiplication_MCM.md](./DP_48_Matrix_Chain_Multiplication_MCM.md)

### Tree DP

1. [DP_49_House_Robber_III_Tree_DP.md](./DP_49_House_Robber_III_Tree_DP.md)
2. [DP_50_Max_Profit_Job_Scheduling_LC1235.md](./DP_50_Max_Profit_Job_Scheduling_LC1235.md)
3. [DP_51_Binary_Tree_Maximum_Path_Sum_LC124.md](./DP_51_Binary_Tree_Maximum_Path_Sum_LC124.md)

------------------------------------------------------------------------

## 4. Recognition Hints

Think DP when:
- brute force tries many repeated choices
- each answer depends on smaller states
- the question asks for min / max / count / feasibility
- recursion naturally forms repeated overlapping calls

Common signals:
- "pick or skip"
- "best up to index `i`"
- "ways to reach"
- "compare prefixes"
- "partition at every cut"

------------------------------------------------------------------------

## 5. Common Pitfalls

- choosing the wrong state definition
- mixing 0-based and 1-based DP incorrectly
- forgetting base cases
- using recursion without memoization
- returning `dp[n][m]` when the answer may live anywhere in the table
- missing space optimization opportunities after understanding transitions

------------------------------------------------------------------------

## 6. Takeaway

- DP becomes much easier once you identify the family of the problem.
- The state definition is the real heart of the solution.
- After that, recurrence, base cases, and optimization usually follow naturally.

------------------------------------------------------------------------

# End of Notes
