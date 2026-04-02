package leetcode;

public class L_1526_Minimum_Number_Of_Increments_On_Subarrays_To_Form_A_Target_Array {
    public static int minNumberOperations(int[] target) {
        int ans = target[0];
        for(int i=1;i<target.length;i++){
            if(target[i]>target[i-1]){
                ans+=target[i]-target[i-1];
            }
        }
        return  ans;
    }
}
