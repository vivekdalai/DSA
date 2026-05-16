package LearningStreamAPIs;

import java.util.function.Predicate;

/**
 * Predicate --> Functional Interface
 * Predicate has ONLY one abstract class - test (returns boolean)
 * default methods --> and, isEqual, negate, or
 */
public class Predicate_01_learn {
    public static void main(String[] args) {

        //
        Predicate<Person> greaterThanEighteen = new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.age >= 18;
            }
        };

        Person p1 = new Person("A", 65);
        Person p3 = new Person("C", 35);
        Person p2 = new Person("B", 15);

        System.out.println("p1 greaterThanEighteen - " + greaterThanEighteen.test(p1));
        System.out.println("p2 greaterThanEighteen - " + greaterThanEighteen.test(p2));

        //Lambda functions are used instead of anonymous functions
        Predicate<Person> greaterThanSixty = (p) ->  p.age >= 60;
        System.out.println("p1 greaterThanSixty ? - "  + greaterThanSixty.test(p3));

        //predicate default inbuilt method.
        //AND
        Predicate<Person> newPredicateAND = greaterThanEighteen.and(greaterThanSixty);
        System.out.println("and Test : " + newPredicateAND.test(p3));

        //or + negate
        Predicate<Person> newPredicateOR = greaterThanSixty.or(greaterThanEighteen.negate());
        System.out.println("p3 greater than 60 or less than 18 : " + newPredicateOR.test(p3));
        System.out.println("p1 greater than 60 or less than 18 : " + newPredicateOR.test(p1));

        // Equals
        Predicate<String> stringPredicate = (str) -> str.equalsIgnoreCase("Hello");
        Predicate<String> stringPredicate1 = Predicate.isEqual("World");
        System.out.println("HELLO : " + stringPredicate.test("HELLO"));
        System.out.println("World : " + stringPredicate.test("World"));
        System.out.println(stringPredicate1.test("World"));


        Predicate<Person> isEqualPredicate = Predicate.isEqual(p1);
        System.out.println("p2 isEqual - " + isEqualPredicate.test(p2));
    }

    static class Person{
        String name;
        int age;
        Person(String name, int age){
            this.name = name;
            this.age = age;
        }
    }
}
