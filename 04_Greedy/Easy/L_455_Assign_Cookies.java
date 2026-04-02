package leetcode;

import java.lang.reflect.Array;
import java.util.Arrays;

public class L_455_Assign_Cookies {
    public static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g); Arrays.sort(s);
        int cnt=0,i=0,j=0;
        while(i<g.length && j<s.length){
            if(g[i]<=s[j]) {
                cnt++;i++;
            }
            j++;
        }
        return cnt;
    }
}
