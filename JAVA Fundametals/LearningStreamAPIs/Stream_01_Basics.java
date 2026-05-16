package LearningStreamAPIs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stream_01_Basics {
    public static void main(String[] args) {
//        The error happens because a Stream can be used only once.
        Stream<Integer> numStream = Stream.of(1,2,3,4,5,6,7,8,9,10,11);
//        numStream.forEach((e) -> System.out.print(e + ", "));

        System.out.println();
        //Stream from List
        List<String> stringList = new ArrayList<>(List.of("a", "b","c","d"));
        Stream<String> stream2 = stringList.stream();
        stream2.forEach((s ) -> System.out.print(s + ", "));
        System.out.println();

        // 1. Filter (given a stream --> returns a stream)
        numStream
                .filter(num -> num % 2 == 0)
                .filter(num -> num > 5)
                .forEach(num -> System.out.print(num + ", "));
        System.out.println();


        //2. Map -> perform some operation over every individual element.
        List<String> nameList = new ArrayList<>();
        nameList.add("Vivek");
        nameList.add("Dalai");
        nameList.add("Oracle");

        nameList.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
        System.out.println();

        System.out.println("nameList: " + nameList.toString()); //DOES NOT change original array


        //Given list of word, print length of each word
        nameList.stream()
                .map(String::length)
                .forEach(System.out::println);

        //FlatMap --> Stream<Collection> --> Steam<Class>
        //eg. Stream<List<String>> --> flatMap --> Stream<String>


        List<List<String>> listOfString = new ArrayList<>();
        listOfString.add(Arrays.asList("a","b","c"));
        listOfString.add(Arrays.asList("d","e","a"));
        listOfString.add(Arrays.asList("g","h","i"));
        listOfString.add(Arrays.asList("j","a","l"));

        listOfString.stream()
                .flatMap(arr -> arr.stream().filter(e -> e.equals("a")))
                .forEach(System.out::println);


        listOfString.stream()
                .flatMap(Collection::stream) //Stream<List<String>> --> flatMap --> Stream<String>
//                .filter("a"::equals)
                .forEach((e) -> System.out.print(e + ", "));

        System.out.println();

        listOfString.stream()
                .forEach((e) -> System.out.print(e + ", "));
    }
}
