package leetcode;

import java.util.HashMap;

public class L_397_Integer_Replacement {

    public static int integerReplacement(int n) {
        if(n==1) return 0;
        int min = Integer.MAX_VALUE;
        if(n%2==1) return Math.min(integerReplacement(n+1)+1, Math.min(integerReplacement(n-1)+1, min));
        return Math.min(integerReplacement(n/2)+1, min);
    }

    public static int integerReplacementWithMemoization(int n) {
       HashMap<Long,Integer> map = new HashMap<>();
       return helper(n,map);
    }

    public static int helper(long n, HashMap<Long, Integer> map){
        if(n==1) return 0;
        if(map.containsKey(n)) return map.get(n);
        int min = Integer.MAX_VALUE;
        int ans;
        if(n%2==1){
            ans = 1 + Math.min(helper(n+1, map), Math.min(helper(n-1,map), min));
        } else{
            ans = 1 + Math.min(helper(n/2,map), min);
        }
        map.put(n,ans);
        return ans;

    }


}
