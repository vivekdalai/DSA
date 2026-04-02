package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class L_402_Remove_K_Digits {
    public static  String removeKdigits(String num, int k) {
        Deque<Character> st = new ArrayDeque<>();

        for(char c: num.toCharArray()){
            while(!st.isEmpty() && k>0 && c<st.peek()){
                st.pop();
                k--;
            }
            st.push(c);
        }

        while(!st.isEmpty() && k>0){
            st.pop();k--;
        }

        StringBuilder sb = new StringBuilder();
        while(!st.isEmpty()){
            sb.append(st.pop());
        }

        while(sb.length()>0 && sb.charAt(sb.length()-1)=='0'){
            sb.deleteCharAt(sb.length()-1);
        }
        String ans = sb.reverse().toString();
        return ans.isEmpty()?"0":ans;
    }

    public static  String removeKdigits_v2(String num, int k) {
        StringBuilder sb = new StringBuilder();

        for(char c: num.toCharArray()){
            while(!sb.isEmpty() && k>0 && c<sb.charAt(sb.length()-1)){
                sb.deleteCharAt(sb.length()-1);
                k--;
            }
            sb.append(c);
        }

        while(!sb.isEmpty() && k>0){
            sb.deleteCharAt(sb.length()-1);k--;
        }


        sb.reverse();

        while(sb.length()>0 && sb.charAt(sb.length()-1)=='0'){
            sb.deleteCharAt(sb.length()-1);
        }
        String ans = sb.reverse().toString();
        return ans.isEmpty()?"0":ans;
    }
}
