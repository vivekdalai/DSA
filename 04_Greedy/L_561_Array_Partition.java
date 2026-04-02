package leetcode;

import java.util.Arrays;

public class L_561_Array_Partition {
    public int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int ans = 0;
        for(int i=1;i<nums.length;){
            ans+=Math.min(nums[i],nums[i-1]);i+=2;
        }

        return ans;
    }
}
