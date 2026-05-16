package LearningStreamAPIs;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class Stream_09_Parallel {
    public static void main(String[] args) {
        System.out.println("---------Serial Stream----------");
        Stream.of(1,2,3,4,5,6,7,8,9)
                .forEach(num -> System.out.println(Thread.currentThread().getName() + " :: " + num));

        System.out.println("---------Parallel Stream----------");
        Stream.of(1,2,3,4,5,6,7,8,9)
                .parallel()
                .forEach(num -> System.out.println(Thread.currentThread().getName() + " :: " + num));



        //Lazy evaluation in streams
        int val = Stream.of(1,2,3,4,5,6,7,8,9)
                .filter(e -> {
                    System.out.println("First filter : " + e);
                    return e > 5;
                })
                .filter(e -> {
                    System.out.println("Second filter : " + e);
                    return e % 7 == 0;
                })
                .findFirst()
                .orElse(-1);


        System.out.println("val : " + val);

    }
}
