package leetcode;

public class L_1827_Minimum_Operations_To_Make_The_Array_Increasing {
    public int minOperations(int[] nums) {
        int n=nums.length;
        int ans=0;
        for(int i=1;i<n;i++){
            if(nums[i-1]>nums[i]){
                ans += nums[i-1]-nums[i]+1;
                nums[i]+=ans;
            }
        }
        return ans;
    }
}
