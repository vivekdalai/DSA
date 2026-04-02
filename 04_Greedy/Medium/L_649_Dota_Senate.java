package leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class L_649_Dota_Senate {
    public static String predictPartyVictory(String senate) {
        int cntD=0,cntR=0,killD=0,killR=0;

        for(char c: senate.toCharArray()){
            if(c=='R') cntR++;
            if(c=='D') cntD++;
        }

        if(cntR==0) return "Dire";
        if(cntD==0) return "Radiant";

        Queue<Character> queue = new LinkedList<>();

        for(char c:senate.toCharArray()){
            queue.add(c);
        }

        while(!queue.isEmpty()){
            char curr = queue.poll(); // current fighter

            if(curr=='R'){
                if(killR==0){ // ready to kill D
                    killD++;
                    cntD--;
                    if(cntD==0) return "Radiant"; // all D dies
                    queue.add(curr); // R will come later to kill D hehe
                }else{
                    killR--; // 1 R dies here
                }
            }else if(curr=='D'){
                if(killD==0){ // ready to kill R
                    killR++;
                    cntR--;
                    if(cntR==0) return "Dire"; // all R dies
                    queue.add(curr); // D will come later to kill R hehe
                }else{
                    killD--; // 1 D dies here
                }
            }

        }
        return "";
    }
}
