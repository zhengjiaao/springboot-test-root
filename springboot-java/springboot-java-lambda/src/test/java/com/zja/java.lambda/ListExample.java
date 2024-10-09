package com.zja.java.lambda;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhengja
 * @since: 2024/03/27 13:31
 */
public class ListExample {

    // 集合元素的转换
    @Test
    public void test_1() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dave");


        // 使用 Lambda 表达式遍历集合
        // names.forEach(name -> System.out.println(name));
        names.forEach(System.out::println);

        // 使用 Lambda 表达式进行集合过滤和转换
        List<String> upperCaseNames = names.stream()
                .filter(name -> name.length() > 3)
                // .map(name -> name.toUpperCase())
                .map(String::toUpperCase) // 使用Lambda表达式将集合中的元素转换为大写
                .collect(Collectors.toList());

        System.out.println(upperCaseNames);
    }

    // 条件判断和筛选
    @Test
    public void test_2() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 使用Lambda表达式筛选集合中的偶数
        List<Integer> evenNumbers = numbers.stream()
                .filter(number -> number % 2 == 0)
                .collect(Collectors.toList());
        System.out.println(evenNumbers); // [2,4]

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dave");

        // 使用Lambda表达式进行条件判断和过滤
        List<String> filteredNames = names.stream()
                .filter(name -> name.startsWith("A"))
                .collect(Collectors.toList());

        System.out.println(filteredNames); // [Alice]
    }

    // 自定义函数式接口和操作
    @Test
    public void test_3() {
        // 使用Lambda表达式实现自定义函数式接口
        StringOperation reverse = str -> new StringBuilder(str).reverse().toString();

        String reversedString = reverse.operate("Hello");
        System.out.println(reversedString); // olleH


        // 使用Lambda表达式进行函数式接口的组合和操作
        MathOperation add = (a, b) -> a + b;
        MathOperation subtract = (a, b) -> a - b;

        int result1 = add.operate(5, 3);
        int result2 = subtract.operate(8, 2);
        System.out.println(result1); // 8
        System.out.println(result2); // 4
    }

    // 定义一个自定义的函数式接口
    interface StringOperation {
        String operate(String str);
    }

    // 定义一个函数式接口
    interface MathOperation {
        int operate(int a, int b);
    }

    // 集合的分组和聚合操作
    @Test
    public void test_4() {
        List<Person> people = Arrays.asList(
                new Person("Alice", 25),
                new Person("Bob", 30),
                new Person("Charlie", 35),
                new Person("Dave", 40)
        );

        // 使用Lambda表达式对人员按年龄分组
        Map<Integer, List<Person>> peopleByAge = people.stream()
                // .collect(Collectors.groupingBy(person -> person.getAge()));
                .collect(Collectors.groupingBy(Person::getAge));
        System.out.println(peopleByAge);

        // 使用Lambda表达式对人员按年龄进行聚合计算
        Map<Integer, Integer> sumOfAgesByAge = people.stream()
                .collect(Collectors.groupingBy(Person::getAge,
                        Collectors.summingInt(Person::getAge)));

        System.out.println(sumOfAgesByAge); //{35=35, 40=40, 25=25, 30=30}
    }

    // 异常处理
    @Test
    public void test_5() {
        // 使用Lambda表达式处理异常
        try {
            Files.lines(Paths.get("file.txt"))
                    .forEach(line -> System.out.println(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 多线程和并行处理
    @Test
    public void test_6() {
        // 使用 Lambda 表达式创建线程
        Thread thread = new Thread(() -> {
            System.out.println("Thread is running");
        });
        thread.start();

        // 使用 Lambda 表达式进行并行处理
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .mapToInt(n -> n)
                .sum();
        System.out.println(sum);
    }

    // 使用函数式接口和Lambda表达式进行排序
    @Test
    public void test_7() {
        List<String> names = Arrays.asList("Alice", "Charlie", "Bob", "Dave");

        // 使用Lambda表达式进行自定义排序
        // names.sort((name1, name2) -> name1.compareTo(name2));
        names.sort(String::compareTo);
        System.out.println(names);
    }

    // 集合的聚合操作
    // 使用Stream API对集合元素进行聚合操作，如求和、平均值、最大值、最小值等。
    @Test
    public void test_8() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 使用Lambda表达式计算集合元素的总和
        int sum = numbers.stream()
                // .reduce(0, (a, b) -> a + b);
                .reduce(0, Integer::sum);
        System.out.println(sum); // 15

        // 平均值
        OptionalDouble average = numbers.stream()
                .mapToInt(Integer::intValue)
                .average();
        System.out.println(average);

        // 最大值
        Optional<Integer> max = numbers.stream()
                .max(Integer::compare);
        System.out.println(max);
    }


    @Getter
    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
