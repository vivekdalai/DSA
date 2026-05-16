package LearningStreamAPIs;

import java.util.ArrayList;
import java.util.List;

public class Stream_02_Basics {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("A", 12));
        list.add(new Person("B", 45));
        list.add(new Person("C", 32));
        list.add(new Person("D", 22));

        list.stream()
                .mapToInt((p) -> p.getAge())
                .forEach(System.out::println);

        //OR
        list.stream()
                .mapToInt(Person::getAge)
                .forEach((e) -> System.out.println("Age : " + e));

        long count = list.stream()
                .filter(e -> e.getAge() > 30)
                .count();
        System.out.println("count  > 30 : " + count);
    }

    static  class Person{
        String name;
        int age;
        Person(String name, int age){
            this.name = name;
            this.age = age;
        }

        public int getAge(){
            return this.age;
        }
    }
}
