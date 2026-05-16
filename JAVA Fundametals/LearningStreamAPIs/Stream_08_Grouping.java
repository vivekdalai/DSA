package LearningStreamAPIs;

import java.util.*;
import java.util.stream.Collectors;

public class Stream_08_Grouping {
    public static void main(String[] args) {
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee("A", 100_000, 25, "ENGG"));
        empList.add(new Employee("B", 200_000, 35, "FINA"));
        empList.add(new Employee("C", 250_000, 33, "ENGG"));
        empList.add(new Employee("D", 150_000, 25, "ENGG"));
        empList.add(new Employee("E", 350_000, 44, "MRKT"));
        empList.add(new Employee("F", 270_000, 41, "MRKT"));
        empList.add(new Employee("G", 250_000, 44, "SUPP"));

        //Create Map of employees wrt Dept
        Map<String, List<Employee>> empMap = empList.stream()
                .collect(Collectors.groupingBy(Employee::getDept));

        empMap.forEach((key, val) -> System.out.println(key + " : " + val));

        // The employees are grouped by dept and age by using the groupingBy() method twice.
        Map<String, Map<Integer,List<Employee>>> employeeMap = empList.stream()
                .collect(Collectors.groupingBy(Employee::getDept, Collectors.groupingBy(Employee::getAge)));

        System.out.println(employeeMap);

        // The employees are grouped by dept and count by dept .
        Map<String, Long> empCountMap = empList.stream()
                .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));

        System.out.println(empCountMap);

        // find total salaries paid for each dept
        //Grouping by allows chaining of Collectors

        Map<String, Integer> totalSalMap = empList.stream()
                .collect(Collectors.groupingBy(Employee::getDept,
                        Collectors.summingInt(Employee::getSal)));

        System.out.println("totalSalMap : " +totalSalMap);


        //Max salary in each dept
        //we can provide the impl of map we want also -- HashMap::new
        // else will return Map<K,V>
        HashMap<String, Optional<Employee>> maxSalInDept = empList.stream()
                .collect(Collectors.groupingBy(Employee::getDept,
                        HashMap::new,
                        Collectors.maxBy(Comparator.comparingInt(Employee::getSal))));

        System.out.println(maxSalInDept);

    }
}

