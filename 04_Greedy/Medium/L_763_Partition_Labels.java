package leetcode;

import java.util.*;

public class L_763_Partition_Labels {
    public static List<Integer> partitionLabels(String s) {

        int[] lastSeen = new int[26];
        Set<Character> set = new HashSet<>();

        for(int i=0;i<s.length();i++){
            lastSeen[s.charAt(i)-'a']=i;
        }

        int cnt=0;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<s.length();i++){
            cnt++;
            if (!set.contains(s.charAt(i))) set.add(s.charAt(i));

            if(lastSeen[s.charAt(i)-'a']  == i){
                set.remove(s.charAt(i));
            }
            if(set.isEmpty()){
                list.add(cnt);
                cnt=0;
            }
        }
        return list;
    }


    public static List<Integer> partitionLabels_v2(String s) {

        int[] lastSeen = new int[26];

        for(int i=0;i<s.length();i++){
            lastSeen[s.charAt(i)-'a']=i;
        }

        int start = 0;
        int end = 0;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<s.length();i++){
            int currIdx = lastSeen[s.charAt(i)-'a'];

            if(currIdx>end){
                end=currIdx;
            } else if (i==end){
                list.add(end-start+1);
                start=end+1;
            }
        }
        return list;
    }
}
