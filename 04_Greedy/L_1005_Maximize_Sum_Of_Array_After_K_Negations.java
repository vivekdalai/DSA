package leetcode;

import java.util.Arrays;

public class L_1005_Maximize_Sum_Of_Array_After_K_Negations {
    public static int largestSumAfterKNegations(int[] nums, int k) {
        Arrays.sort(nums);
        int n=nums.length;
        int ans=0;
        for(int i=0;i<n;i++){
            if(nums[i]<0){
                if(k>0){
                    nums[i]*=-1;
                    k--;
                }
            }
            ans+=nums[i];
        }

        int min = Integer.MAX_VALUE;
        for(int i=0;i<n;i++){
            min = Math.min(min,nums[i]);
        }

        if(k%2==1)
            ans-=2*min;
        return ans;
    }
}
