package LearningStreamAPIs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stream_03_slicing {
    public static void main(String[] args) {
        List<String> countries = new ArrayList<>();
        countries.add("India");
        countries.add("USA");
        countries.add("China");
        countries.add("India");
        countries.add("UK");
        countries.add("China");

        countries.stream()
                .distinct()
                .forEach(System.out::println);

        String result = countries.stream()
                .distinct()
                .collect(Collectors.joining(","));
        System.out.println("countries : " + result );

        //limit
//        System.out.println("limit");
        String result2 = countries.stream()
                .distinct()
                .limit(2)
                .collect(Collectors.joining(","));
        System.out.println("countries (limit 2): " + result2 );

        //SKIP
        String result3 = countries.stream()
                .distinct()
                .skip(2)
                .collect(Collectors.joining(","));
        System.out.println("countries (skip 2): " + result3 );

    }
}
