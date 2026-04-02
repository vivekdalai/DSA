package leetcode;

public class L_942_DI_String_Match {
    public int[] diStringMatch(String s) {
        int I=0,D=s.length();
        int[] ans = new int[D+1];

        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='I'){
                ans[i]=I;I++;
            }else{
                ans[i]=D;D--;
            }
        }

        ans[ans.length-1]=D;
        return ans;

    }
}
