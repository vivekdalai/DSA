package leetcode;

import java.util.*;

public class L_621_Task_Scheduler {
    public static int leastInterval(char[] tasks, int n) {

        int[] freq = new int[26];
        for(char c: tasks){
            freq[c-'A']++;
        }
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        for(int f : freq){
            if(f>0) maxHeap.add(f);
        }

        Queue<int[]> queue = new LinkedList<>();

        int time=0;

        while(!maxHeap.isEmpty() || !queue.isEmpty()){
            time++;
            if(!maxHeap.isEmpty()) {
                int maxFreq = maxHeap.poll() - 1;
                if (maxFreq > 0)
                    queue.add(new int[]{maxFreq, time + n});
            }

            if(!queue.isEmpty()){
                int[] list = queue.peek();
                if(list[1]==time){
                    maxHeap.add(list[0]);
                    queue.poll();
                }
            }
        }

        return time;

    }
}
