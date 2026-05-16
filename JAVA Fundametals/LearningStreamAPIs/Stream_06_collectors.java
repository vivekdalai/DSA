package LearningStreamAPIs;

import java.util.*;
import java.util.stream.Collectors;

import LearningStreamAPIs.Stream_05_Reduce.Employee;

public class Stream_06_collectors {
    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Dave", 100));
        list.add(new Employee("Joe",200));
        list.add(new Employee("Chandler",300));
        list.add(new Employee("Iyan", 200));
        list.add(new Employee("Ray", 200));

        List<String> empNames = list.stream()
                .map(Employee::getName)
                .toList();
        System.out.println("empName:  " + empNames);

        Set<String> empNames2 = list.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());
        System.out.println("empName set : " + empNames2);

        /**
         * Creating Map from List
         */

        Map<String, Integer> empMap = list.stream()
                .collect(Collectors.toMap(s -> s.name, s -> s.sal));

        for(Map.Entry<String, Integer> entry : empMap.entrySet())
            System.out.println(entry.getKey() + " : " + entry.getValue());

        empMap.forEach((key, val) -> System.out.println(key + "::" + val));


        //If list have duplicates. pass resolve function also
        list.add(new Employee("Dave", 500));

        Map<String, Integer> newMap = list.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getSal,(s1, s2) -> s1 ));
        //if duplicate name comes, keep the first salary (s1)

        newMap.forEach((key,val) -> System.out.println(key + "--" + val));

        Map<String, Integer> newMap3 = list.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getSal,(s1, s2) -> Math.max(s1, s2) ));
        //if duplicate name comes, keep the max salary

        newMap3.forEach((key,val) -> System.out.println(key + " --> " + val));

    }
}
