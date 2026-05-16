package LearningStreamAPIs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * reduce stream to single value
 * identity - initial value
 * accumulator - function takes 2 params
 * combiner - function to combine partial result
 *
 *
 * Optional<T> reduce(BinaryOperator<T> accumulator)#
 */
public class Stream_05_Reduce {
    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Dave", 100));
        list.add(new Employee("Joe",200));
        list.add(new Employee("Ryan",300));
        list.add(new Employee("Iyan", 200));
        list.add(new Employee("Ray", 200));

        OptionalInt total = list.stream()
                .mapToInt(Employee::getSal)
                .reduce((p,q) -> p + q);

        System.out.println("total - " + total.getAsInt());

        //OR
        int total2 = list.stream()
                .mapToInt(Employee::getSal)
                .sum();
        System.out.println("total2 : " + total2);

        //providing identity (base sum)
        int total3 = list.stream()
                .mapToInt(Employee::getSal)
                .reduce(1, (p,q) -> p + q);

        System.out.println("total3 : " + total3);


        //Max sal
        OptionalInt maxSal = list.stream()
                .mapToInt(Employee::getSal)
                .max();
        System.out.println("maxSal : " + maxSal.getAsInt());
    }
    static class Employee{
        String name;
        int sal;
        Employee(String name, int sal){
            this.name = name;
            this.sal = sal;
        }

        public int getSal(){
            return this.sal;
        }

        public String getName(){
            return this.name;
        }


        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", sal=" + sal +
                    '}';
        }
    }
}
