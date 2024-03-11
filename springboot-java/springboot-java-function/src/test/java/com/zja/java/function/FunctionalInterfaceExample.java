package com.zja.java.function;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 函数抽象注解 @FunctionalInterface 用法示例
 * <p>
 * 注解 @FunctionalInterface是Java中的一个注解，用于标识一个接口是函数式接口。函数式接口是指只包含一个抽象方法的接口，它可以被用作lambda表达式或方法引用的目标。
 * <p>
 * 作用：提供更多的灵活性和方便性
 *
 * @author: zhengja
 * @since: 2024/03/11 14:07
 */
public class FunctionalInterfaceExample {

    // 演示了@FunctionalInterface的用法以及函数式接口的应用
    @Test
    public void test_1() {
        // 创建了三个具体实现：addition、subtraction和multiplication。每个实现都使用lambda表达式定义了接口方法的具体实现
        Calculator addition = (a, b) -> a + b;
        int sum = addition.calculate(5, 3);
        System.out.println("Sum: " + sum);

        Calculator subtraction = (a, b) -> a - b;
        int difference = subtraction.calculate(5, 3);
        System.out.println("Difference: " + difference);

        Calculator multiplication = (a, b) -> a * b;
        int product = multiplication.calculate(5, 3);
        System.out.println("Product: " + product);
    }

    // 使用Predicate函数式接口
    // 流程：
    // 1.使用Predicate函数式接口和@FunctionalInterface注解
    // 2.定义了一个startsWithAPredicate谓词，它判断字符串是否以"A"开头
    // 3.使用filterList方法过滤字符串列表，只保留以"A"开头的字符串
    @Test
    public void test_2() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        Predicate<String> startsWithAPredicate = name -> name.startsWith("A");
        List<String> filteredNames = filterList(names, startsWithAPredicate);

        System.out.println("Filtered names: " + filteredNames);
    }

    public static List<String> filterList(List<String> list, Predicate<String> predicate) {
        List<String> filteredList = new ArrayList<>();
        for (String item : list) {
            if (predicate.test(item)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    // 使用Consumer函数式接口
    // 流程：
    // 1.使用Consumer函数式接口和@FunctionalInterface注解
    // 2.定义了一个printNameConsumer消费者，它接收一个字符串并打印出"Hello, "加上该字符串
    // 3.使用forEachName方法对字符串列表中的每个元素应用该消费者
    @Test
    public void test_3() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        Consumer<String> printNameConsumer = name -> System.out.println("Hello, " + name);
        forEachName(names, printNameConsumer);
    }

    public static void forEachName(List<String> list, Consumer<String> consumer) {
        for (String item : list) {
            consumer.accept(item);
        }
    }

    // 注解 @FunctionalInterface的用法示例
    // 流程：
    // 1.定义了一个自定义函数式接口MyFunction，它包含一个抽象方法performAction()
    // 2.使用@FunctionalInterface注解，我们确保接口只有一个抽象方法
    // 3.创建了一个实现了MyFunction接口的lambda表达式，并调用了performAction()方法
    @Test
    public void test_4() {
        MyFunction myFunction = () -> System.out.println("Performing action");
        myFunction.performAction();
    }

    // 使用Consumer函数式接口和lambda表达式
    // 流程：
    // 1.使用了Consumer函数式接口和lambda表达式来遍历列表中的每个元素，并执行打印操作
    // 2.lambda表达式(name -> System.out.println("Hello, " + name))作为参数传递给forEach方法
    @Test
    public void test_5() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        names.forEach(name -> System.out.println("Hello, " + name));
    }

    // 使用Predicate函数式接口和lambda表达式
    // 流程：
    // 1.使用了Predicate函数式接口和lambda表达式来筛选以"A"开头的字符串
    // 2.使用流式操作和lambda表达式，我们可以更简洁地实现筛选逻辑
    @Test
    public void test_6() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        List<String> filteredNames = names.stream()
                .filter(name -> name.startsWith("A"))
                .collect(Collectors.toList());

        System.out.println("Filtered names: " + filteredNames);
    }

    // 使用Function函数式接口和lambda表达式
    // 流程：
    // 1.使用了Function函数式接口和lambda表达式将整数转换为字符串
    // 2.lambda表达式(number -> String.valueOf(number))接收一个整数并将其转换为字符串
    // 3.使用apply方法应用该函数式接口，将整数42转换为字符串
    @Test
    public void test_7() {
        Function<Integer, String> intToString = number -> String.valueOf(number);

        String convertedString = intToString.apply(42);
        System.out.println("Converted string: " + convertedString);
    }

    // 使用Comparator函数式接口和lambda表达式进行排序
    // 流程：
    // 1.使用了Comparator函数式接口和lambda表达式对字符串列表进行排序
    // 2.lambda表达式(name1, name2) -> name1.compareTo(name2)定义了比较两个字符串的逻辑
    // 3.使用sort方法和该lambda表达式对列表进行排序
    @Test
    public void test_8() {
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        names.sort((name1, name2) -> name1.compareTo(name2));

        System.out.println("Sorted names: " + names);
    }

    // 使用Runnable函数式接口和lambda表达式创建线程
    // 流程：
    // 1.使用了Runnable函数式接口和lambda表达式创建一个线程
    // 2.lambda表达式() -> { ... }定义了线程的执行逻辑
    // 3.创建了一个线程对象，并将该lambda表达式传递给线程的构造函数，然后启动线程
    @Test
    public void test_9() {
        Runnable runnable = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Hello, " + i);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    // 使用Function函数式接口和lambda表达式进行数据转换
    // 流程：
    // 1.使用了Function函数式接口和lambda表达式将字符串转换为整数
    // 2.lambda表达式str -> Integer.parseInt(str)定义了将字符串转换为整数的逻辑
    // 3.使用apply方法应用该函数式接口，将字符串"42"转换为整数
    @Test
    public void test_10() {
        Function<String, Integer> stringToInt = str -> Integer.parseInt(str);

        int number = stringToInt.apply("42");
        System.out.println("Number: " + number);
    }


    // 接收一个参数，无返回值的方法
    @Test
    public void test_11() {
        MyFunction1<String> myConsumer = (item) -> System.out.println("Consuming: " + item);

        myConsumer.accept("Hello, World!");
    }

    // 接收一个参数，返回一个值的方法
    @Test
    public void test_12() {
        MyFunction2<String, Integer> myFunction = (str) -> str.length();

        int length = myFunction.apply("Hello, World!");
        System.out.println("Length: " + length);
    }

    // 接收两个参数，返回一个值的方法
    @Test
    public void test_13() {
        MyFunction3<Integer, Integer> myFunction = (a, b) -> a + b;

        int result = myFunction.apply(5, 3);
        System.out.println("Result: " + result);
    }

    // 接收多个参数，无返回值的方法
    @Test
    public void test_14() {
        MyFunction4 myAction = (str, num) -> System.out.println("Performing action: " + str + ", " + num);

        myAction.perform("Hello", 42);
    }


    // 使用@FunctionalInterface注解，我们明确告诉编译器这是一个函数式接口，以便在编译时进行验证。
    // 如果接口不符合函数式接口的规范（即有多个抽象方法），编译器会报错
    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    // 无参数，无返回值
    // 允许静态方法、允许默认方法、允许参数为指定类型：String、Object等
    @FunctionalInterface
    interface MyFunction {
        void performAction();

        default void doSomeWork1() {
            // 方法体
        }

        static void doSomeWork2() {

        }

        default void doSomeWork3(String msg) {

        }

        default void doSomeWork4(Object obj) {

        }
    }

    // 接收一个参数，无返回值的方法
    @FunctionalInterface
    interface MyFunction1<T> {
        void accept(T item);
    }


    // 接收一个参数，返回一个值的方法
    @FunctionalInterface
    interface MyFunction2<T, R> {
        R apply(T arg);
    }

    // 接收两个参数，返回一个值的方法
    @FunctionalInterface
    interface MyFunction3<T, R> {
        R apply(T arg1, T arg2);
    }

    // 接收多个参数，无返回值的方法
    @FunctionalInterface
    interface MyFunction4 {
        void perform(String arg1, int arg2);
    }

}
