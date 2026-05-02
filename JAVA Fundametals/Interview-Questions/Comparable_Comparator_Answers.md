# Q1 - Q9

```java
import java.util.*;
class Person implements Comparable<Person> {
    int age;
    String name;
    double salary;

    public Person (int age, String name, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    // for Q3
//    @Override
//    public int compareTo(Person o) {
//        if (this.name == null && o.name == null) return 0;
//        if (this.name == null) return 1;
//        if (o.name == null) return -1;
//        return this.name.compareTo(o.name);
//    }

    // for Q4
    @Override
    public int compareTo(Person o) {
        int ageOrder = Integer.compare(this.age, o.age);
        if (ageOrder != 0) return ageOrder;
        if (this.name == null && o.name == null) return 0;
        if (this.name == null) return 1;
        if (o.name == null) return -1;
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return age + " " + name + " " + salary;
    }
}
public class Main {
    public static void main(String[] args) {
        List<Person> list = Arrays.asList(
                new Person(25, "Rahul", 50000),
                new Person(25, "Amit", 60000),
                new Person(30, "Zoya", 70000),
                new Person(25, "Amit", 40000),
                new Person(30, "amit", 80000),
                new Person(22, "Neha", 45000),
                new Person(22, null, 30000)
        );

        Comparator<Person> ageComparator = (a, b) -> Integer.compare(a.age, b.age);

        System.out.println("Q1. Sort a list of Person by age (ascending) using Comparator.");
        System.out.println("==========================");
        Collections.sort(list, ageComparator);
        display(list);

        System.out.println("Q2. Sort the same list by age (descending)");
        System.out.println("==========================");
        Collections.sort(list, ageComparator.reversed());
        display(list);

        System.out.println("Q3. Modify Person to implement Comparable and sort by name");
        System.out.println("==========================");
        Collections.sort(list); // use Q3 comapreTo()
        display(list);

        System.out.println("Q4. Sort Person by age (if same -> name)");
        System.out.println("==========================");
        Collections.sort(list); // use Q4 comapreTo()
        display(list);

        System.out.println("Q5. Sort Person by age, name and salary");
        System.out.println("==========================");
        Comparator<Person> ageNameSalaryComparator = Comparator.comparingInt(Person::getAge)
                                                    .thenComparing(Person::getName, Comparator.nullsLast(String::compareTo))
                                                    .thenComparingDouble(Person::getSalary);
        Collections.sort(list, ageNameSalaryComparator);
        display(list);


        System.out.println("Q6. Sort Person by age (asc) and salary (desc)");
        System.out.println("==========================");
        Comparator<Person> ageSalaryComparator = Comparator.comparingInt(Person::getAge)
                .thenComparing(Comparator.comparingDouble(Person::getSalary).reversed());
        Collections.sort(list, ageSalaryComparator);
        display(list);

        System.out.println("Q7. Age < 30 first, then salary ascending");
        System.out.println("==========================");
        Comparator<Person> ageSalaryComparator2 = Comparator.comparingInt((Person p) -> p.getAge() < 30 ? 0 : 1)
                .thenComparingDouble(Person::getSalary);
        Collections.sort(list, ageSalaryComparator2);
        display(list);

        System.out.println("Q8. Sort by name (nulls last), then age");
        System.out.println("==========================");
        Comparator<Person> nameAgeComparator = Comparator.comparing(Person::getName, Comparator.nullsLast(String::compareTo))
                .thenComparingInt(Person::getAge);
        Collections.sort(list, nameAgeComparator);
        display(list);

        System.out.println("Q9. Case-insensitive name sorting");
        System.out.println("==========================");
        Comparator<Person> caseSensitiveNameComparator = Comparator.comparing(Person::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
        Collections.sort(list, caseSensitiveNameComparator);
        display(list);

    }

    public static void display(List<Person> list) {
        for (Person p : list) {
            System.out.println(p.toString());
        }
        System.out.println();
        System.out.println();
    }
}
```
# Q15
```java
public static void main(String[] args) {

        int[] nums = new int[] {3, 30, 34, 5, 9};

        String[] arr = Arrays.stream(nums)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);


        Arrays.sort(arr, (a, b) -> (b + a).compareTo(a + b));

        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(s);
        }

        System.out.println(sb);


    }
```


