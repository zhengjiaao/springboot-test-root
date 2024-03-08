package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhengja
 * @since: 2024/03/08 16:17
 */
public class OptionalListExample {

    // 从可能为空的List中获取第一个元素
    @Test
    public void test_1() {
        List<String> list = Arrays.asList("apple", "banana", "orange");

        Optional<String> optionalElement = list.stream().findFirst();
        optionalElement.ifPresent(element -> System.out.println("First element: " + element));
    }

    // 使用Optional获取满足条件的第一个元素
    @Test
    public void test_2() {
        List<String> list = Arrays.asList("apple", "banana", "orange");

        Optional<String> optionalElement = list.stream()
                .filter(element -> element.startsWith("b"))
                .findFirst();
        optionalElement.ifPresent(element -> System.out.println("Element starting with 'b': " + element));
    }

    // 从可能为空的List中获取满足条件的所有元素
    @Test
    public void test_5() {
        List<String> list = Arrays.asList("apple", "banana", "orange");

        List<String> filteredList = list.stream()
                .filter(element -> element.startsWith("a"))
                .collect(Collectors.toList());

        filteredList.forEach(element -> System.out.println("Filtered element: " + element));
    }


    // 使用Optional获取列表中的最大值 or 最小值
    @Test
    public void test_3() {
        List<Integer> numbers = Arrays.asList(10, 5, 8, 12, 3);

        Optional<Integer> optionalMax = numbers.stream()
                .max(Integer::compareTo);
        optionalMax.ifPresent(max -> System.out.println("Max value: " + max));

        // or 使用Optional获取列表中的最小值

        Optional<Integer> optionalMin = numbers.stream()
                .min(Integer::compareTo);
        optionalMin.ifPresent(min -> System.out.println("Min value: " + min));
    }

    // 使用Optional结合flatMap处理嵌套的List结构
    @Test
    public void test_4() {
        List<List<String>> nestedList = Arrays.asList(
                Arrays.asList("apple", "banana", "orange"),
                Arrays.asList("cat", "dog", "elephant"),
                Arrays.asList("red", "green", "blue")
        );

        Optional<String> optionalElement = nestedList.stream()
                .flatMap(List::stream)
                .filter(element -> element.startsWith("b"))
                .findFirst();
        optionalElement.ifPresent(element -> System.out.println("Element starting with 'b': " + element));
    }


    //  使用Optional结合flatMap处理多层嵌套的List结构
    @Test
    public void test_6() {
        List<List<List<String>>> nestedList = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("apple", "banana"),
                        Arrays.asList("orange", "grape")
                ),
                Arrays.asList(
                        Arrays.asList("cat", "dog"),
                        Arrays.asList("elephant", "lion")
                )
        );

        Optional<String> optionalElement = nestedList.stream()
                .flatMap(List::stream)
                .flatMap(List::stream)
                .findFirst();
        optionalElement.ifPresent(element -> System.out.println("First element: " + element));
    }

    // 使用Optional结合reduce操作对列表进行聚合
    @Test
    public void test_7() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Optional<Integer> optionalSum = numbers.stream()
                .reduce(Integer::sum);
        optionalSum.ifPresent(sum -> System.out.println("Sum: " + sum));
    }

    // 从可能为空的List中获取满足条件的任意一个元素
    @Test
    public void test_8() {
        List<String> list = Arrays.asList("apple", "banana", "orange");

        Optional<String> optionalElement = list.stream()
                .filter(element -> element.length() > 5)
                .findAny();
        optionalElement.ifPresent(element -> System.out.println("Element with length > 5: " + element));
    }

    // 使用Optional获取列表中的平均值
    @Test
    public void test_9() {
        List<Integer> numbers = Arrays.asList(10, 5, 8, 12, 3);

        OptionalDouble optionalAverage = numbers.stream()
                .mapToDouble(Integer::doubleValue)
                .average();
        optionalAverage.ifPresent(average -> System.out.println("Average: " + average));
    }

    // 使用Optional结合collect操作将列表分组
    @Test
    public void test_10() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Map<Boolean, List<Integer>> evenOddMap = numbers.stream()
                .collect(Collectors.partitioningBy(num -> num % 2 == 0));

        Optional.ofNullable(evenOddMap.get(true))
                .ifPresent(evenNumbers -> System.out.println("Even numbers: " + evenNumbers));
    }
}
