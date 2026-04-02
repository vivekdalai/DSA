package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class L_1221_Split_a_String_in_Balanced_Strings {
    public static int balancedStringSplit(String s) {

        int balance = 0;
        int ans = 0;

        for(char c : s.toCharArray()){
            if(c == 'L') balance++;
            else balance--;

            if(balance == 0) ans++;
        }

        return ans;

    }
}
