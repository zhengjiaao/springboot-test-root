package com.dist.java8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/6 16:53
 */
public class Java8FunctionalInterface {

    public static void main(String[] args) {
        //那么就可以使用Lambda表达式来表示该接口的一个实现(注：JAVA 8 之前一般是用匿名类实现的)：
        GreetingService greetService1 = message -> System.out.println("Hello " + message);
        greetService1.sayMessage("mes");

        /**
         * 函数式接口实例
         * Predicate <T> 接口是一个函数式接口，它接受一个输入参数 T，返回一个布尔值结果
         * 该接口用于测试对象是 true 或 false
         */
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Predicate<Integer> predicate = n -> true
        // n 是一个参数传递到 Predicate 接口的 test 方法
        // n 如果存在则 test 方法返回 true

        System.out.println("输出所有数据:");

        // 传递参数 n
        eval(list, n->true);

        // Predicate<Integer> predicate1 = n -> n%2 == 0
        // n 是一个参数传递到 Predicate 接口的 test 方法
        // 如果 n%2 为 0 test 方法返回 true

        System.out.println("输出所有偶数:");
        eval(list, n-> n%2 == 0 );

        // Predicate<Integer> predicate2 = n -> n > 3
        // n 是一个参数传递到 Predicate 接口的 test 方法
        // 如果 n 大于 3 test 方法返回 true

        System.out.println("输出大于 3 的所有数字:");
        eval(list, n-> n > 3 );
    }

    public static void eval(List<Integer> list, Predicate<Integer> predicate) {
        for(Integer n: list) {

            if(predicate.test(n)) {
                System.out.println(n + " ");
            }
        }
    }

}
