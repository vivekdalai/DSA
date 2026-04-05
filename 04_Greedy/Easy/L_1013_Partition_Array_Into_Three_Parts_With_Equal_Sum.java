package leetcode;

import java.util.Arrays;

public class L_1013_Partition_Array_Into_Three_Parts_With_Equal_Sum {

    public boolean canThreePartsEqualSum(int[] arr) {
        int sum = 0, len = arr.length;
        for(int i=0; i < len; i++){
            sum +=arr[i];
        }

        if (sum % 3 != 0) return false;
        int chunkSum = sum / 3;
        int localSum = 0, cnt = 0;
        for (int i = 0; i < len; i++) {
            localSum += arr[i];

            if(localSum == chunkSum) {
                cnt++; localSum = 0;
            }
            // if found two chunks and if remaining elements are still present that's gonna be equal sum coz we have made sure that sum is divisible by 3 and we have equal chunks as well
            if (cnt == 2 && i < len - 1) return true;
        }
        return false;
    }
}
