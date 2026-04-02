package leetcode;

import java.util.Arrays;

public class L_122_Best_Time_to_Buy_and_Sell_Stock_II {
    public int maxProfit(int[] prices) {

//        return func(0,true,prices,prices.length,fee); // recursion

        int n = prices.length;
        int[][] dp = new int[n][2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);
        }
        return func_memoization(0,1,prices,n,dp); // recursion with memoization

        // optimal
//        int hold=-prices[0],sell=0,profit=0;
//        for(int i=1;i<prices.length;i++){
//            hold = Math.max(sell-prices[i],hold);
//            sell = Math.max(hold+prices[i],sell);
//        }
//        return sell;
    }

    public static int func(int idx, boolean buy, int[] prices, int n){
        if(idx==n) return 0;
        int profit=0;
        if(buy){
            profit += Math.max(
                    -prices[idx] + func(idx+1,false,prices,n),
                    func(idx+1,true,prices,n)
            );
        } else {
            profit += Math.max(
                    prices[idx] + func(idx+1,true,prices,n),
                    func(idx+1,false,prices,n)
            );
        }
        return profit;
    }

    public static int func_memoization(int idx, int buy, int[] prices, int n, int[][] dp){
        if(idx==n) return 0;
        if(dp[idx][buy]!=-1) return dp[idx][buy];
        int profit=0;
        if(buy==1){
            profit += Math.max(
                    -prices[idx] + func_memoization(idx+1,0,prices,n,dp),
                    func_memoization(idx+1,1,prices,n,dp)
            );
        } else {
            profit += Math.max(
                    prices[idx] + func_memoization(idx+1,1,prices,n,dp),
                    func_memoization(idx+1,0,prices,n,dp)
            );
        }
        dp[idx][buy] = profit;
        return profit;
    }
}
