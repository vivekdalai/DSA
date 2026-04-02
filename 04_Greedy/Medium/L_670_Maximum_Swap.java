package leetcode;

import java.util.Arrays;
import java.util.Stack;

public class L_670_Maximum_Swap {
    public static int maximumSwap(int num) {
        char[] digits = String.valueOf(num).toCharArray();
        int n=digits.length;
        int[] freq = new int[10];
        int mark1=-1,mark2=-1;

        for(int i=0;i<digits.length;i++) freq[digits[i]-'0']++;

        int max=-1;
        for(int i=0;i<n;i++){
            int c=9;
            while(c>=0){
                if(freq[c]>0){
                    max=c;break;
                }
                c--;
            }
            if(max>digits[i]-'0'){
                mark1=i;break;
            }

            freq[digits[i]-'0']--;
        }

        for(int i=n-1;i>mark1;i--){
            if(digits[i]-'0'==max){
                mark2=i;break;
            }
        }
        if(mark1>-1){ // mark1 found
            char temp = digits[mark1];
            digits[mark1] = digits[mark2];
            digits[mark2] = temp;
        }

        StringBuilder sb = new StringBuilder();
        for(char c:digits) sb.append(c);
        return Integer.parseInt(sb.toString());
    }

    public int maximumSwap_v2(int num) { // removed unnecessary while loop by storing last occurrence
        char[] digits = String.valueOf(num).toCharArray();

        int[] last = new int[10];

        // store last occurrence
        for (int i = 0; i < digits.length; i++) {
            last[digits[i] - '0'] = i;
        }

        for (int i = 0; i < digits.length; i++) {
            for (int d = 9; d > digits[i] - '0'; d--) {
                if (last[d] > i) {
                    // swap
                    char temp = digits[i];
                    digits[i] = digits[last[d]];
                    digits[last[d]] = temp;

                    return Integer.parseInt(new String(digits));
                }
            }
        }

        return num;
    }


    public static int maximumSwap_v3(int num) {
        String numStr = String.valueOf(num);
        char arr[]  = numStr.toCharArray();

        Stack<Integer> st = new Stack<>();
        int[] GER = new int[arr.length];
        for(int i = arr.length - 1; i >=0 ; i--){
            int curr = arr[i] - '0';
            if(st.isEmpty() || (arr[st.peek()] - '0') < curr){
                GER[i] = -1;
                st.push(i);
            } else if ((arr[st.peek()] - '0') == curr){ //value at
                GER[i] = -1;
            } else {
                GER[i] = st.peek();
            }
        }

        System.out.println(Arrays.toString(GER));

        for(int i = 0; i < arr.length; i++){
            if(GER[i] != -1){
                char temp = arr[i];
                arr[i] = arr[GER[i]];
                arr[GER[i]] = temp;
                break;
            }
        }

        System.out.println(Arrays.toString(arr));

        int ans = Integer.parseInt(new String(arr));

        return ans;

    }
}
