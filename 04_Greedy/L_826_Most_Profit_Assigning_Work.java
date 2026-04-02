package leetcode;

import java.lang.reflect.Array;
import java.util.Arrays;

public class L_826_Most_Profit_Assigning_Work {
    public int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
        int n = difficulty.length;
        int[][] dp = new int[n][2];

        int m = worker.length;

        for (int i = 0; i < n; i++) {
            dp[i][0] = difficulty[i];
            dp[i][1] = profit[i];
        }

        Arrays.sort(dp,(a,b)->a[0]-b[0]);
        Arrays.sort(worker);

        int i = 0, currMaxProfit = 0, maxProfit = 0;
        for (int w : worker) {
            while (i < n && dp[i][0] <= w) {
                currMaxProfit = Math.max(currMaxProfit, dp[i][1]);
                i++;
            }

            maxProfit += currMaxProfit;
        }

        return maxProfit;
    }
}
