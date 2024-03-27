package com.zja.java.lambda;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author: zhengja
 * @since: 2024/03/27 13:57
 */
public class LambdaListPersonExample {

    // 条件筛选
    @Test
    public void test_1() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式筛选出满足特定条件的人员对象
        List<Person> filteredPeople = people.stream()
                .filter(person -> person.getAge() > 30)
                .collect(Collectors.toList());
        filteredPeople.forEach(p -> {
            System.out.println(p.name);
        });

        System.out.println();

        // 使用Lambda表达式查找满足特定条件的人员对象
        Optional<Person> findFirstPeople = people.stream()
                .filter(person2 -> person2.getAge() > 30)
                .findFirst();
        findFirstPeople.ifPresent(p2 -> System.out.println(p2.getName()));

        System.out.println();

        // 使用Lambda表达式进行多条件的筛选和排序
        List<Person> filteredPeople2 = people.stream()
                .filter(person -> person.getAge() > 30 && person.getCity().equals("北京"))
                .sorted(Comparator.comparing(Person::getName).thenComparing(Person::getAge))
                .collect(Collectors.toList());

        filteredPeople2.forEach(System.out::println);
    }

    // 对象转换
    @Test
    public void test_2() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式将List<Person>中的对象转换为其他类型或提取特定属性
        List<String> names = people.stream()
                // .map(person -> person.getName())
                .map(Person::getName)
                .collect(Collectors.toList());
        System.out.println(names);
    }

    // 排序
    @Test
    public void test_3() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式对List<Person>中的对象进行排序
        people.sort((person1, person2) -> person1.getName().compareTo(person2.getName()));
        System.out.println(people);
    }

    // 统计和聚合操作
    @Test
    public void test_4() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式对List<Person>中的对象进行聚合计算，如求和、平均值等
        int sumOfAges = people.stream()
                // .mapToInt(person -> person.getAge())
                .mapToInt(Person::getAge)
                .sum();
        System.out.println(sumOfAges);

        // 使用Lambda表达式进行更复杂的统计和聚合操作，如求平均值、拼接字符串等。
        double averageAge = people.stream()
                .collect(Collectors.averagingInt(Person::getAge));

        String allNames = people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", "));
    }

    // 分组
    @Test
    public void test_5() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式对List<Person>中的对象进行分组
        Map<String, List<Person>> peopleByCity = people.stream()
                // .collect(Collectors.groupingBy(person -> person.getCity()));
                .collect(Collectors.groupingBy(Person::getCity));
        System.out.println(peopleByCity);
    }

    // 统计操作
    @Test
    public void test_6() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式对List<Person>中的对象进行统计，如计数、最大值、最小值等。
        long count = people.stream()
                .filter(person -> person.getAge() > 30)
                .count();
        System.out.println(count);

        Optional<Integer> maxAge = people.stream()
                .map(person -> person.getAge())
                .max(Integer::compare);
        System.out.println(maxAge);
    }

    // 批量操作
    @Test
    public void test_7() {
        List<Person> people = getPersonList();
        System.out.println(people);

        // 使用Lambda表达式对List<Person>中的对象进行批量操作，如批量更新、删除等。
        people.forEach(person -> person.setAge(person.getAge() + 1));
        System.out.println(people);
    }

    // 并行处理
    @Test
    public void test_8() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式并行处理List<Person>中的对象
        // 使用Stream API并行处理大数据集
        people.parallelStream()
                .forEach(person -> processPerson(person));
    }

    private void processPerson(Person person) {
        System.out.println(person);
    }

    // 转换为Map,对象关联和映射
    @Test
    public void test_9() {
        List<Person> people = getPersonList();
        // 使用Lambda表达式将List<Person>转换为Map
        Map<String, Person> personMap = people.stream()
                .collect(Collectors.toMap(Person::getName, person -> person));

        // 使用Lambda表达式进行对象之间的关联和映射
        Map<String, List<String>> cityToNamesMap = people.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.mapping(Person::getName,
                                Collectors.toList())));
    }

    // 分页和分块处理
    @Test
    public void test_10() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式进行分页和分块处理
        int pageSize = 2;
        int currentPage = 1;

        // 分页
        List<Person> pageOfPeople = people.stream()
                .skip((currentPage - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        System.out.println(pageOfPeople);

        // 分块
        List<List<Person>> chunksOfPeople = IntStream.range(0, people.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index / pageSize))
                .values()
                .stream()
                .map(indices -> indices.stream().map(people::get).collect(Collectors.toList()))
                .collect(Collectors.toList());

        System.out.println(chunksOfPeople);
    }

    // 嵌套集合处理
    @Test
    public void test_11() {
        List<Person> people = getPersonList();

        // 使用Lambda表达式处理嵌套的集合属性。
        List<String> allHobbies = people.stream()
                .flatMap(person -> person.getHobbies().stream()) // [A, C, C, B, D]
                .distinct()
                .collect(Collectors.toList());

        System.out.println(allHobbies); // [A, C, B, D]
    }

    private List<Person> getPersonList() {
        return Arrays.asList(
                new Person("Alice", 25, "上海"),
                new Person("Charlie", 30, "上海"),
                new Person("Bob", 35, "北京"),
                new Person("Eud", 32, "北京"),
                new Person("Dave", 40, "广州"));
    }

    static class Person {
        private String name;
        private String city;
        private int age;

        private List<String> hobbies = Arrays.asList("A", "C", "C", "B", "D");

        public Person(String name, int age, String city) {
            this.name = name;
            this.city = city;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public String getCity() {
            return city;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<String> getHobbies() {
            return hobbies;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", city='" + city + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
