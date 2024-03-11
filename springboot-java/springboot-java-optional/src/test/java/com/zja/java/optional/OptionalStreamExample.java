package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zhengja
 * @since: 2024/03/11 9:47
 */
public class OptionalStreamExample {

    // 查找操作：使用 findFirst() 或 findAny() 方法查找满足条件的元素。例如，从列表中查找第一个满足某个条件的元素
    @Test
    public void test_1() {
        List<Optional<String>> optionalList = getOptionalList();
        optionalList.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .filter(str -> str.startsWith("A"))
                .findFirst()
                .ifPresent(System.out::println);
    }

    // 聚合操作：使用 reduce() 方法对包含在 Optional 中的值进行聚合操作。例如，计算一组数字的总和
    @Test
    public void test_2() {
        List<Optional<Integer>> optionalNumbers = getOptionalNumbers();
        int sum = optionalNumbers.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .reduce(0, Integer::sum);
        System.out.println(sum);
    }

    // 排序操作：使用 sorted() 方法对包含在 Optional 中的值进行排序。例如，对包含数字的 Optional 进行排序
    @Test
    public void test_3() {
        List<Optional<Integer>> optionalNumbers = getOptionalNumbers();
        optionalNumbers.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .sorted()
                .forEach(System.out::println);

        // ro 转为 集合
       /* List<Integer> collect = optionalNumbers.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .sorted().collect(Collectors.toList());
        collect.forEach(System.out::println);*/
    }

    // 统计操作：使用 collect() 方法进行元素收集和统计。例如，收集列表中非空的元素，并统计个数
    @Test
    public void test_4() {
        List<Optional<String>> optionalList = getOptionalList();
        long count = optionalList.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.counting());
        System.out.println(count);
    }

    // 过滤操作：使用 filter() 方法过滤包含在 Optional 中的值。例如，过滤出包含特定条件的非空字符串
    @Test
    public void test_5() {
        List<Optional<String>> optionalList = getOptionalList();
        optionalList.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .filter(str -> str.length() > 5)
                .forEach(System.out::println);
    }

    // 映射操作：使用 map() 方法映射包含在 Optional 中的值。例如，将包含的字符串转换为大写
    @Test
    public void test_6() {
        List<Optional<String>> optionalList = getOptionalList();
        optionalList.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    // 聚合操作：使用 flatMap() 方法将多个 Optional 对象聚合成一个。例如，将多个可能为空的元素连接为一个字符串
    @Test
    public void test_7() {
        List<Optional<String>> optionalList = getOptionalList();
        String concatenatedStr = optionalList.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.joining());
        System.out.println(concatenatedStr);
    }

    // 默认值操作：使用 orElse() 或 orElseGet() 方法提供默认值。例如，获取列表中的第一个非空字符串或提供默认值
    @Test
    public void test_8() {
        List<Optional<String>> optionalList = getOptionalList();
        String firstNonEmptyStr = optionalList.stream()
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .findFirst()
                .orElse("Default Value");
        System.out.println(firstNonEmptyStr);
    }

    public static List<Optional<String>> getOptionalList() {
        // 模拟获取包含 Optional 对象的列表
        Optional<String> optional1 = Optional.of("Apple");
        Optional<String> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.of("Banana");
        Optional<String> optional4 = Optional.of("Cherry");

        return Arrays.asList(optional1, optional2, optional3, optional4);
    }

    public static List<Optional<Integer>> getOptionalNumbers() {
        // 模拟获取包含 Optional 对象的列表
        Optional<Integer> optional1 = Optional.of(1);
        Optional<Integer> optional2 = Optional.empty();
        Optional<Integer> optional3 = Optional.of(3);
        Optional<Integer> optional4 = Optional.of(2);

        return Arrays.asList(optional1, optional2, optional3, optional4);
    }


}
