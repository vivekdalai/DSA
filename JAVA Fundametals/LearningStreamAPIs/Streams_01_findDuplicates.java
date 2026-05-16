package LearningStreamAPIs;

import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Streams_01_findDuplicates {
    public static void main(String[] args) {
        Map<Integer, Integer> freqCount = new HashMap<>();
        //custom comparator
        Map<Integer, Integer> treeMapCount = new TreeMap<>(new Comparator<Integer>(){
            @Override
            public int compare(Integer a, Integer b){
                return Integer.compare(b,a); //desc
            }
        });
        /*
        * replace with lambda
            Map<Integer, Integer> treeMapCount = new TreeMap<>((a, b) -> {
                return Integer.compare(b, a); //desc
            });
         */
        List<Integer> nums = List.of(9,2,4,2,3,1,2,4,6,1,2,4,5,8,7,1);
        for(int n : nums){
            freqCount.put(n, freqCount.getOrDefault(n,0) + 1);
            treeMapCount.put(n, treeMapCount.getOrDefault(n,0) + 1);
        }

        for(Map.Entry<Integer, Integer> entry : freqCount.entrySet()){
            System.out.println("Key : " + entry.getKey() + ", count: " + entry.getValue());
        }

        //using iterator
        Iterator<Map.Entry<Integer, Integer>> itr = treeMapCount.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<Integer, Integer> entry = itr.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // fetch duplicate entries in nums

        System.out.print("duplicates : ");

        Map<Integer, Long> newMap = nums.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        //-->collect method is used to Store result in some List

        newMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .forEach(entry -> System.out.print(entry.getKey() + ", "));
        System.out.println();

        //store
        List<Integer> duplicates = newMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList(); // --> inplace of .collect(Collectors.toList())
        System.out.println("duplicates : " + duplicates);

        Predicate<Integer> isEven = n -> n % 2 == 0;
        /*
            Predicate<Integer> isEven = new Predicate<Integer>() {
                @Override
                public boolean test(Integer integer) {
                    return integer % 2 == 0;
                }
            }
         */

        System.out.println(isEven.test(100));
        System.out.println(isEven.test(23));

        //print occurance once
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> finalHashSet = nums.stream()
                                .filter(n -> hashSet.add(n))
                                .collect(Collectors.toSet());
        System.out.println(finalHashSet);

    }
}
