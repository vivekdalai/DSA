package leetcode;

public class L_738_Monotone_Increasing_Digits {
    public int monotoneIncreasingDigits(int n) {
        char[] s = String.valueOf(n).toCharArray();

        int len=s.length;
        int mark=len;
        for(int i=len-1;i>=1;i--){
            if(s[i-1]>s[i]){ // violation
                mark = i-1;
                s[i-1]--;
            }
        }

        for(int i=mark+1;i<len;i++){
            s[i]='9';
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++){
            sb.append((s[i]));
        }

        return Integer.parseInt(sb.toString());
    }
}
