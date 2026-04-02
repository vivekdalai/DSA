package leetcode;

import java.util.Arrays;

public class L_714_Best_Time_to_Buy_and_Sell_Stock_with_Transaction_Fee {
    public static int maxProfit(int[] prices, int fee) {
//        return func(0,true,prices,prices.length,fee); // recursion
        int n = prices.length;
        int[][] dp = new int[n][2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);
        }
        return func_memoization(0,1,prices,n,fee,dp); // recursion
//        int hold=-prices[0],sell=0,profit=0;
//        for(int i=1;i<prices.length;i++){
//            hold = Math.max(sell-prices[i],hold);
//            sell = Math.max(hold+prices[i]-fee,sell);
//        }
//        return sell;
    }

    public static int func(int idx, boolean buy, int[] prices, int n, int fee){
        if(idx==n) return 0;
        int profit=0;
        if(buy){
            profit += Math.max(
                    -prices[idx] + func(idx+1,false,prices,n,fee),
                    func(idx+1,true,prices,n,fee)
            );
        } else {
            profit += Math.max(
                    prices[idx] - fee + func(idx+1,true,prices,n,fee),
                    func(idx+1,false,prices,n,fee)
            );
        }
        return profit;
    }

    public static int func_memoization(int idx, int buy, int[] prices, int n, int fee, int[][] dp){
        if(idx==n) return 0;
        if(dp[idx][buy]!=-1) return dp[idx][buy];
        int profit=0;
        if(buy==1){
            profit += Math.max(
                    -prices[idx] + func_memoization(idx+1,0,prices,n,fee,dp),
                    func_memoization(idx+1,1,prices,n,fee,dp)
            );
        } else {
            profit += Math.max(
                    prices[idx] - fee + func_memoization(idx+1,1,prices,n,fee,dp),
                    func_memoization(idx+1,0,prices,n,fee,dp)
            );
        }
        dp[idx][buy] = profit;
        return profit;
    }
}
