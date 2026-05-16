package LearningStreamAPIs;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Supplier_01_learn {
    public static void main(String[] args) {
        Person p1 = new Person(10, "Arjun");
        Person p2 = new Person(10, "Arjun");


        Predicate<Person> samePersonPredicate = Predicate.isEqual(p1);
        System.out.println("samePersonPredicate - " + samePersonPredicate.test(p2));

        //Supplier --> return value when get() is called
        Supplier<Person> personSupplier = () -> new Person(20, "Krishna");
        Person p3 = personSupplier.get();


        DoubleUnaryOperator operator = num -> num * num;

        System.out.println(operator.applyAsDouble(25));

    }

    static class Person{
        int age;
        String name;

        Person(int age, String name){
            this.age = age;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj){
            if(obj instanceof Person other) {
                return this.age == other.age && this.name.equals(other.name);
            }

            return false;
        }
    }

}
