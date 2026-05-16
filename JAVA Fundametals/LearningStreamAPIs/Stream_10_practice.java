package LearningStreamAPIs;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.*;

public class Stream_10_practice {
    public static void main(String[] args) {
//        practice1();
//        practice2();
        practice3();
    }

    private static void practice1(){
        /**
         * Primitive Datatype to Stream
         */

        int[] arr = new int[]{4,1,5,7,1,4,7,2,7,9,4,2,5,4,8,6,8,8,6,3};
        IntStream integerStream = Arrays.stream(arr); // primitive dataType returns `IntStream`
        long sum = integerStream.distinct()
                .sorted()
                .peek(System.out::println)
                .sum();

    }

    private static void practice2(){
        /**
         * Streams over collection
         */
        List<Integer> integers  = new ArrayList<>(List.of(4,1,5,7,1,4,7,2,7,9,4,2,5,4,8,6,8,8,6,3));
        System.out.println(integers);

        Stream<Integer> integerStream = integers.stream(); //Collections returns `Stream<Integer>`
        List<Integer> distinctEven =  integerStream.distinct()
                .filter(e -> e % 2 == 0)
//                .sorted(Collections.reverseOrder())
                .sorted((a,b) -> Integer.compare(b, a))
                .toList();
        System.out.println(distinctEven);
    }

    private static void practice3(){
        /**
         * Operation sequence in stream :: elements goes down as much as possible . Stops where it requires complete stream
         *  filter -> map :: requires individual elements
         *  sorted :: requires complete stream
         */
        List<Integer> arrList = new ArrayList<>(List.of(4, 7, 3, 6, 1, 5, 2));
        Stream<Integer> arrStream = arrList.stream()
                .filter(e -> e > 3)
                .peek(e -> System.out.println("peek after filter: " + e))
                .map(e -> e * -1)
                .peek(e -> System.out.println("peek after map: " + e))
                .sorted()
                .peek(e -> System.out.println("peek after sorted: " + e));

        List<Integer> result = arrStream.toList();
        System.out.println("result : " + result);


        /**
         * Output ::
         * peek after filter: 4
         * peek after map: -4
         * peek after filter: 7
         * peek after map: -7
         * peek after filter: 6
         * peek after map: -6
         * peek after filter: 5
         * peek after map: -5
         * peek after sorted: -7
         * peek after sorted: -6
         * peek after sorted: -5
         * peek after sorted: -4
         * result : [-7, -6, -5, -4]
         */


        Optional<Integer> minVal = arrList.stream()
                .filter(e -> e > 4)
                .min(Integer::compare);
        System.out.println(minVal.get());
    }
}
