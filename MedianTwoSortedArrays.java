

public class MedianTwoSortedArrays {

    public static void main(String[] args) {
        Solution objName = new Solution();

        int[] arr = {1,2,4,5,6,7};

        /*1 + 2 = 3
        5 - 2 = 3
        4 - 1 = 3
        7- 4 = 3
        (7 + 5 + 2) - (1 + 4 + 6)
        */
        int result = objName.findTargetSumWays(arr, 3);
        System.out.println("result : " + result);

    }
}


class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        int total = 0;
        for(int x : nums) total += x;

        if(Math.abs(target) > total || (target + total) % 2 != 0) return 0;
        int reqSum = (total + target) / 2;
        int[][] dp = new int[n+1][reqSum + 1];

        // to handle cases if 0 is present in nums.
        dp[0][0] = 1;
        for(int i = 1 ; i <= n ; i++){
            if(nums[i-1] == 0)  dp[i][0] = dp[i-1][0] * 2;
            else
                dp[i][0] = dp[i-1][0];
        }
        for(int i = 1; i <= n ; i++){
            for(int j = 1 ; j <= reqSum ; j++){
                if(nums[i-1] > j)
                    dp[i][j] = dp[i-1][j];
                else
                    dp[i][j] = dp[i-1][j] + dp[i-1][j - nums[i-1]];
            }
        }
        return dp[n][reqSum];
    }
}
