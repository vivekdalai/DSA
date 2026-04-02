package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class L_1403_Minimum_Subsequence_In_Non_Increasing_Order {
    public static List<Integer> minSubsequence(int[] nums) {
        Arrays.sort(nums);
        int totalSum=0;

        for(int i=0;i<nums.length;i++){
            totalSum+=nums[i];
        }
        System.out.println(totalSum);
        int currSum=0;
        List<Integer> ans = new ArrayList<>();
        for(int i=nums.length-1;i>=0;i--){

            currSum+=nums[i];
            ans.add(nums[i]);
            if(currSum > (totalSum-currSum)){
                return ans;
            }
        }

        return ans;
    }

    public static List<Integer> minSubsequence_v2(int[] nums) {

        int[] freq = new int[101];
        int n = nums.length;
        int totalSum=0;

        for(int i=0;i<n;i++){
            int k = nums[i];
            freq[k]++;
            totalSum+=k;
        }
        int currSum=0;
        List<Integer> ans = new ArrayList<>();
        for(int i=100;i>=1;i--){
            int fr = freq[i];

            while(fr>0){
                currSum += i;
                ans.add(i);fr--;
                if(2*currSum > totalSum){
                    return ans;
                }
            }

        }

        return ans;
    }
}
