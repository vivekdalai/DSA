package LearningStreamAPIs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stream_04_Matching {
    public static void main(String[] args) {
        /**
         * anyMatch() :: boolean anyMatch(Predicate<? super T> predicate)
             * true if at least one element matches the criteria.
             * false if no element matches the criteria.
             * false if the stream is empty.

         * allMatch()

         * noneMatch()

         */
        Person p1 =  new Person("A", 28, "IND");
        Person p2 = new Person("B", 32, "UK");
        Person p3 =  new Person("D", 20, "AUS");
        Person p4 = new Person("E", 65, "US");

        List<Person> list = new ArrayList<>(List.of(p1, p2, p3, p4));

        boolean anyMatch = list.stream()
                .anyMatch((p) -> p.country.equals("IND"));
        System.out.println("anyMatch-1 :" + anyMatch);

        anyMatch = list.stream()
                .anyMatch((p) -> p.country.equals("EUR"));
        System.out.println("anyMatch-2 :" + anyMatch);

        //allMatch() -> all satisfy condition
        boolean allMatch = list.stream()
                .allMatch((p) -> p.age > 18);
        System.out.println("allMatch : " + allMatch);


//        noneMatch() --> no element satisfy condition - return true
        System.out.println("noneMatch " + list.stream().noneMatch(p -> p.country.equals("EUR")));

        Optional<Person> p5 = list.stream()
                .filter(p -> p.country.equals("IND"))
                .findAny();

        if(p5.isPresent())
            System.out.println(p5.get());

    }

    static class Person{
        String name;
        int age;
        String country;
        Person(String name, int age, String country){
            this.name = name;
            this.age = age;
            this.country = country;
        }

        public String getCountry(){
            return this.country;
        }

        public int getAge(){
            return this.age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
}
