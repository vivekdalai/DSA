package leetcode;

import java.util.Arrays;
import java.util.Stack;

public class L_316_Remove_Duplicate_Letters {
    public static String removeDuplicateLetters(String s) {
        int[] lastSeen = new int[26];
        boolean[] visited = new boolean[26];
        int n = s.length();
        Arrays.fill(lastSeen,-1);

        for(int i=n-1;i>=0;i--){
            if(lastSeen[s.charAt(i)-'a']==-1)
                lastSeen[s.charAt(i)-'a']=i;
        }

        Stack<Integer> st = new Stack<>();


        for(int i=0;i<n;i++){

            if(visited[s.charAt(i)-'a']) continue;


            while(!st.empty() && s.charAt(st.peek())>s.charAt(i) && lastSeen[s.charAt(st.peek())-'a']>i){
                visited[s.charAt(st.pop())-'a'] = false;
            }

            st.push(i);
            visited[s.charAt(i)-'a'] = true;

        }
        StringBuilder sb = new StringBuilder();
        while (!st.empty()){
            sb.append(s.charAt(st.pop()));
        }


        return sb.reverse().toString();
    }
}
