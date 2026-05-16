package LearningStreamAPIs;

import java.util.*;
import java.util.stream.Collectors;

public class Stream_07_Aggregation {
    public static void main(String[] args) {
        //counting
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee("A", 100_000, 25));
        empList.add(new Employee("B", 200_000, 35));
        empList.add(new Employee("C", 250_000, 33));
        empList.add(new Employee("D", 150_000, 28));
        empList.add(new Employee("E", 350_000, 44));
        empList.add(new Employee("F", 270_000, 41));
        empList.add(new Employee("G", 250_000, 44));

        //count of employees age > 30
        long count = empList.stream()
                .filter(e -> e.getAge() > 30)
                .count();

        System.out.println("count of emp with age > 30 : " + count);

        //average of empSalary
        double avgSal = empList.stream()
                .collect(Collectors.averagingInt(Employee::getSal));
        System.out.println("Avg sal paid: " + (Math.round(avgSal * 100.0)/100.0));

        /**
         * maxBy(), minBy() --> results only one instance of matching
         * does not throw error if duplicate exists, simply return first occurrence
         */

        //Min sal employee
        Optional<Employee> minSalEmp = empList.stream()
                .collect(Collectors.minBy(Comparator.comparing(Employee::getSal)));

        if(minSalEmp.isPresent())
            System.out.println("min Sal employee" + minSalEmp.get());

        //max age employee
        Optional<Employee> maxAgeEmp = empList.stream()
                .max(Comparator.comparing(Employee::getAge));

        if(maxAgeEmp.isPresent())
            System.out.println("max Age empl: " + maxAgeEmp.get());

        /**
         * get list of all empl with max age
         * 1. I know how to get max age --> mapToInt --> max()
         * 2. I know how to filter list --> filter() --> toList()
         */

        int maxAge = empList.stream()
                .mapToInt(Employee::getAge)
                .max().orElse(0);

        if(maxAge != 0);
        {
            List<Employee> maxAgeEmpList = empList.stream()
                    .filter(e -> e.getAge() == maxAge)
                    .collect(Collectors.toList());
            System.out.println("maxAgeEmpList : " + maxAgeEmpList);
        }


        //String joining
        List<String> strArr = new ArrayList<>(List.of("Hello", "World", "I am", "learning","stream APIs"));

        String joinedStr = strArr.stream()
                .collect(Collectors.joining(" "));
        System.out.println(joinedStr);

        //OR
        String newStr = String.join(" ", strArr);
        System.out.println(newStr);

    }
}


class Employee{
    String name;
    int sal;
    int age;
    String dept;
    Employee(String name, int sal, int age){
        this.name = name;
        this.sal = sal;
        this.age = age;
    }

    Employee(String name, int sal, int age, String dept){
        this(name, sal, age);
        this.dept = dept;
    }

    public int getSal(){
        return this.sal;
    }

    public String getName(){
        return this.name;
    }

    public int getAge() { return this.age; }

    public String getDept() { return this.dept; }


    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", sal=" + sal +
                ", age=" + age +
                '}';
    }
}