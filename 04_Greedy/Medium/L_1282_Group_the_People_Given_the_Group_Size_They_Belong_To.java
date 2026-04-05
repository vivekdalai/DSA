package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class L_1282_Group_the_People_Given_the_Group_Size_They_Belong_To {
    public static List<List<Integer>> groupThePeople(int[] groupSizes) {

        Map<Integer, List<Integer>> map = new HashMap<>();
        int n = groupSizes.length;
        for (int i = 0; i < n; i++) {
            List<Integer> list = map.getOrDefault(groupSizes[i], new ArrayList<>());
            list.add(i);
            map.put(groupSizes[i], list);
        }
        List<List<Integer>> ans = new ArrayList<>();

        for (int key : map.keySet()) {
            List<Integer> list = map.get(key);
            int groups = list.size() / key;

            for (int i = 0; i < groups; i++) {
                List<Integer> tempList = new ArrayList<>();
                for (int j = 0; j < key; j++) {
                    tempList.add(list.get((key * i) + j));
                }
                ans.add(tempList);
            }

        }

        return ans;
    }

    public static List<List<Integer>> groupThePeople_v2(int[] groupSizes) {

        Map<Integer, List<Integer>> map = new HashMap<>();
        List<List<Integer>> ans = new ArrayList<>();
        int n = groupSizes.length;
        for (int i = 0; i < n; i++) {
            int groupSize = groupSizes[i];
            if (!map.containsKey(groupSize)) {
                map.put(groupSize, new ArrayList<>());
            }

            List<Integer> group = map.get(groupSize);
            group.add(i);
            if (group.size() == groupSize) {
                ans.add(group);
                map.put(groupSize, new ArrayList<>()); // reset the map
            }

        }

        return ans;
    }
}
