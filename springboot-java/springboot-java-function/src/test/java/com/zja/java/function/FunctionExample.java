package com.zja.java.function;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: zhengja
 * @since: 2024/03/11 10:57
 */
public class FunctionExample {

    // 映射：你可以使用Function将对象从一种类型映射到另一种类型
    @Test
    public void test_1() {
        // 创建一个将字符串转换为大写表示的函数
        Function<String, String> toUpperCase = String::toUpperCase;

        // 应用函数以转换字符串
        String result = toUpperCase.apply("hello");

        System.out.println("大写: " + result);
    }

    // 函数链式调用：你可以使用andThen方法将多个Function实例链接在一起，形成一个连续的转换序列
    // 流程：
    // 1.有两个函数：doubleFunction将输入值加倍，toStringFunction将输入值转换为字符串
    // 2.使用andThen方法将这两个函数链接在一起，创建一个新的函数combinedFunction
    // 3.当我们将combinedFunction应用于输入值5时，它首先将值加倍为10，然后将其转换为字符串"10"
    @Test
    public void test_2() {
        // 创建一个将输入值加倍的函数
        Function<Integer, Integer> doubleFunction = x -> x * 2;

        // 创建一个将输入值转换为字符串的函数
        Function<Integer, String> toStringFunction = Object::toString;

        // 将函数链接在一起
        Function<Integer, String> combinedFunction = doubleFunction.andThen(toStringFunction);

        // 应用组合函数
        String result = combinedFunction.apply(5);

        System.out.println("结果: " + result);
    }

    // 函数组合：你可以使用compose方法将两个函数组合起来，创建一个表示这两个函数组合的新函数
    // 流程：
    // 1.有两个函数：doubleFunction将输入值加倍，squareFunction将输入值平方
    // 2.使用compose方法将这两个函数组合起来，创建一个新的函数composedFunction
    // 3.当我们将composedFunction应用于输入值5时，它首先将值平方为25，然后将其加倍为50
    @Test
    public void test_3() {
        // 创建一个将输入值加倍的函数
        Function<Integer, Integer> doubleFunction = x -> x * 2;

        // 创建一个将输入值平方的函数
        Function<Integer, Integer> squareFunction = x -> x * x;

        // 将函数组合在一起
        Function<Integer, Integer> composedFunction = doubleFunction.compose(squareFunction);

        // 应用组合函数
        int result = composedFunction.apply(5);

        System.out.println("结果: " + result);
    }

    // 数据转换：Function接口可以用于将一种数据类型转换为另一种数据类型。例如，将字符串转换为整数
    // 流程：
    // 1.stringToInteger函数使用Integer::parseInt方法引用来实现字符串到整数的转换
    // 2.使用apply方法将字符串"42"转换为整数值42
    @Test
    public void test_4() {
        // 创建一个将字符串转换为整数的函数
        Function<String, Integer> stringToInteger = Integer::parseInt;

        // 应用函数进行数据转换
        int value = stringToInteger.apply("42");

        System.out.println("转换结果: " + value);
    }

    // 数据过滤：Function接口可以用于根据特定条件过滤数据。例如，过滤出大于某个阈值的整数
    // 流程：
    // 1.创建了一个函数filterFunction，它过滤出大于5的整数
    // 2.使用filter方法和filterFunction::apply进行数据过滤，将满足条件的整数收集到新的列表filteredNumbers中
    @Test
    public void test_5() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 创建一个函数，过滤出大于5的整数
        Function<Integer, Boolean> filterFunction = num -> num > 5;

        // 应用函数进行数据过滤
        List<Integer> filteredNumbers = numbers.stream()
                .filter(filterFunction::apply)
                .collect(Collectors.toList());

        System.out.println("过滤结果: " + filteredNumbers);
    }

    // 数据映射：Function接口可以用于对数据进行映射转换。例如，将一个对象的属性映射为另一个对象的属性
    @Test
    public void test_6() {
        // 创建一个函数，将Person对象映射为其名称的大写形式
        Function<Person, String> nameMappingFunction = person -> person.getName().toUpperCase();

        // 创建一个Person对象
        Person person = new Person("Alice", 25);

        // 应用函数进行数据映射
        String mappedName = nameMappingFunction.apply(person);

        System.out.println("映射结果: " + mappedName);
    }

    @Getter
    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // 省略构造函数和getter/setter

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    // 数据计算：Function接口可以用于执行各种数据计算操作。例如，计算两个整数的和
    // 流程：
    // 1.创建了一个Function函数addFunction，它将两个整数相加
    // 2.使用柯里化（currying）技术，使得该函数接受两个参数并返回它们的和
    // 3.使用apply方法依次将5和3作为参数应用于addFunction，得到它们的和8
    @Test
    public void test_7() {
        // 创建一个将两个整数相加的函数
        Function<Integer, Function<Integer, Integer>> addFunction = x -> y -> x + y;

        // 应用函数进行数据计算
        int sum = addFunction.apply(5).apply(3);

        System.out.println("和: " + sum);
    }

    // 缓存：Function接口可以用于实现简单的缓存机制，以提高性能。例如，缓存计算结果
    @Test
    public void test_8() {
        Map<Integer, Integer> cache = new HashMap<>();

        // 创建一个将整数的平方缓存起来的函数
        Function<Integer, Integer> squareFunction = x -> {
            if (cache.containsKey(x)) {
                return cache.get(x);
            } else {
                int result = x * x;
                cache.put(x, result);
                return result;
            }
        };

        // 应用函数进行数据计算
        int square1 = squareFunction.apply(5);
        int square2 = squareFunction.apply(5);

        System.out.println("平方1: " + square1);
        System.out.println("平方2: " + square2);
    }

}
