package leetcode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

public class L_179_Largest_Number {
    public static String largestNumber(int[] nums) {
        String[] arr = new String[nums.length];

        for (int i = 0; i < nums.length; i++) {
            arr[i] = String.valueOf(nums[i]);
        }

        Arrays.sort(arr, (a, b) -> (b+a).compareTo(a+b));

        if(arr[0].equals("0")) return "0";


        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arr.length;i++){
            sb.append(arr[i]);
        }

        return sb.toString();
    }
}
