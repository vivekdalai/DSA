package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class L_581_Shortest_Unsorted_Continuous_Subarray {
    public static int findUnsortedSubarray(int[] nums) {

        Deque<Integer> st = new ArrayDeque<>();

        int n=nums.length;
        int minStart=Integer.MAX_VALUE;
        int maxEnd=Integer.MIN_VALUE;
        for(int i=0;i<n;i++){
            while(!st.isEmpty() && nums[st.peek()]>nums[i]){
                minStart = Math.min(st.pop(),minStart);
            }
            st.push(i);
        }
//        System.out.println(minStart);
        st.clear();

        for(int i=n-1;i>=0;i--){
            while(!st.isEmpty() && nums[st.peek()]<nums[i]){
                maxEnd=Math.max(st.pop(),maxEnd);
            }
            st.push(i);
        }
//        System.out.println(maxEnd);
        return minStart==Integer.MAX_VALUE?0:maxEnd-minStart+1;
    }

    public static int findUnsortedSubarray_v2(int[] nums) {

        int n=nums.length;
        int minStart=Integer.MAX_VALUE;
        int maxEnd=Integer.MIN_VALUE;
        for(int i=1;i<n;i++){
            int j=i-1;
            while(j>=0 && nums[j]>nums[i]){
                minStart = Math.min(j,minStart);
                j--;
            }
        }
        for(int i=n-2;i>=0;i--){
            int j=i+1;
            while(j<n && nums[j]<nums[i]){
                maxEnd=Math.max(j,maxEnd);
                j++;
            }
        }

        return minStart==Integer.MAX_VALUE?0:maxEnd-minStart+1;
    }

    public static int findUnsortedSubarray_v3(int[] nums) {

        int n=nums.length;
        int minm=Integer.MAX_VALUE;
        int maxm=Integer.MIN_VALUE;
        int start=-1,end=-2;
        for(int i=0;i<n;i++){
           maxm = Math.max(maxm,nums[i]);
           minm = Math.min(minm,nums[n-i-1]);
           if(nums[i]<maxm) end=i;
           if(nums[n-i-1]>minm) start=n-i-1;
        }


        return end-start+1;
    }
}
