package leetcode;

import java.util.Arrays;

public class L_976_Largest_Perimeter_Triangle {
    public int largestPerimeter(int[] nums) {
        // for triangle a<=b<=c --> a+b>c

        Arrays.sort(nums);
        int n=nums.length;
        for(int i=n-1;i>=2;i--){
            if(nums[i-2]+nums[i-1]>nums[i]){
                return nums[i-2]+nums[i-1]+nums[i];
            }
        }
        return 0;
    }
}
