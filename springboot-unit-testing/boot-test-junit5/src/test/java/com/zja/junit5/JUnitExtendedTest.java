/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 10:37
 * @Since:
 */
package com.zja.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit5 扩展功能方法测试
 *
 * @Test：用于标识测试方法。
 * @Disabled：用于禁用测试方法。
 * @RepeatedTest：用于重复运行测试方法。
 * @BeforeEach：在每个测试方法之前执行的操作。
 * @AfterEach：在每个测试方法之后执行的操作。
 * @BeforeAll：在所有测试方法之前执行的操作。
 * @AfterAll：在所有测试方法之后执行的操作。
 * @Nested：用于创建嵌套的测试类。
 * @author: zhengja
 * @since: 2023/10/16 10:37
 */
public class JUnitExtendedTest {

    //@Test 注解表示一个测试方法，其中可以使用assertEquals断言来验证期望的结果
    //@DisplayName：用于为测试类或测试方法指定自定义的显示名称，以便更清晰地描述测试的目的。
    @Test
    @DisplayName("My TestAddition")
    void testAdditionV1() {
        System.out.println("测试方法：testAdditionV1()");
        int result = JUnitTest.Calculator.add(2, 3);
        assertEquals(5, result, "Addition result is incorrect");
    }

    //@RepeatedTest：用于指定重复运行同一个测试方法的次数。
    @RepeatedTest(3)
    void testAdditionV2() {
        System.out.println("测试方法：testAdditionV2()");
        int result = JUnitTest.Calculator.add(2, 3);
        assertEquals(5, result, "Addition result is incorrect");
    }

    //@ParameterizedTest：用于创建参数化测试，允许多次运行同一个测试方法，每次使用不同的参数。
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void parameterizedTest(int number) {
        // 使用参数运行的测试方法
        assertTrue(isPrime(number));
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"MONDAY", "WEDNESDAY", "FRIDAY"})
    void testIsWeekday(DayOfWeek dayOfWeek) {
        assertTrue(isWeekday(dayOfWeek));
    }

    @ParameterizedTest
    @CsvSource({"apple, 5", "banana, 6", "cherry, 6"})
    void testStringLength(String input, int expectedLength) {
        assertEquals(expectedLength, input.length());
    }

    @ParameterizedTest
    @MethodSource("provideNumbers")
    void testSquare(int number, int expectedSquare) {
        assertEquals(expectedSquare, square(number));
    }

    private static Stream<Arguments> provideNumbers() {
        return Stream.of(
                Arguments.of(2, 4),
                Arguments.of(3, 9),
                Arguments.of(4, 16)
        );
    }

    //@TestFactory：用于创建动态测试，允许在运行时动态生成一组测试。
    //@TestFactory 是 JUnit 5 中的一个注解，用于创建动态测试（Dynamic Tests）。动态测试是一种在运行时生成测试用例的方式，可以根据特定的数据源或配置生成多个测试实例。
    //
    //使用 @TestFactory 注解的方法必须返回一个 Stream、Iterable、Iterator、Collection、DynamicTest[] 或 DynamicNode 对象。
    @TestFactory
    Stream<DynamicTest> testPalindrome() {
        List<String> words = Arrays.asList("level", "radar", "hello", "world");

        // 动态生成测试的方法
        return words.stream()
                .map(word -> DynamicTest.dynamicTest("Test palindrome: " + word,
                        () -> assertTrue(isPalindrome(word))));
    }

    //使用一个循环来比较 left 和 right 指针所指向的字符是否相等。
    // 如果相等，将 left 向右移动一位，right 向左移动一位，继续比较下一对字符。
    // 如果不相等，说明单词不是回文，直接返回 false。
    //当 left 指针超过 right 指针时，意味着已经比较完了单词中的所有字符，且每对字符都相等，因此单词是回文，返回 true。
    private boolean isPalindrome(String word) {
        // 判断一个单词是否为回文的逻辑
        int left = 0;
        int right = word.length() - 1;

        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    //@Timeout：用于设置测试方法的超时时间，如果测试方法执行时间超过指定的时间，则测试将被视为失败。
    @Test
    @Timeout(5)
    // 5秒超时
    void timeoutTest() throws InterruptedException {
        // 可能会超时的测试方法
        Thread.sleep(6000);
    }

    //@Tag：用于为测试方法添加标签，以便对测试进行分组或筛选。
    @Test
    @Tag("slow") // 添加标签
    void slowTest() {
        // 慢速测试方法
    }

    //@Disabled  被禁用的测试方法
    @Test
    @Disabled("Skipping this test for now")
    void disabledTest() {
        // 被禁用的测试方法
    }

    //@TestTemplate：用于创建测试模板，允许在运行时生成一组相关的测试。
    @TestTemplate
    @DisplayName("Repeated Test")
    @RepeatedTest(3)
    void repeatedTest() {
        // 重复运行的测试模板
    }

    //@BeforeEach 和 @AfterEach：用于在每个测试方法之前和之后执行特定的操作。
    @BeforeEach
    void beforeEachTest() {
        // 在每个测试方法之前执行的操作
    }

    @AfterEach
    void afterEachTest() {
        // 在每个测试方法之后执行的操作
    }

    //@BeforeAll 和 @AfterAll：用于在所有测试方法之前和之后执行一次性的操作。
    @BeforeAll
    static void beforeAllTests() {
        // 在所有测试方法之前执行的操作
    }

    @AfterAll
    static void afterAllTests() {
        // 在所有测试方法之后执行的操作
    }

    //@Nested：用于创建嵌套的测试类
    @Nested
    @DisplayName("Math operations")
    class MathOperationsTest {
        // 嵌套的测试类
        @Test
        @DisplayName("Test Subtraction")
        void testSubtraction() {
            System.out.println("测试方法：testSubtraction");
            int result = Calculator.subtract(5, 3);
            assertEquals(2, result, "Subtraction result is incorrect");
        }

        @Test
        @DisplayName("Test Multiplication")
        void testMultiplication() {
            System.out.println("测试方法：testMultiplication");
            int result = Calculator.multiply(2, 3);
            assertEquals(6, result, "Multiplication result is incorrect");
        }
    }


    static class Calculator {
        static int add(int a, int b) {
            return a + b;
        }

        static int subtract(int a, int b) {
            return a - b;
        }

        static int multiply(int a, int b) {
            return a * b;
        }

        static double divide(int dividend, int divisor) {
            if (divisor == 0) {
                throw new ArithmeticException("Divisor cannot be zero");
            }
            return (double) dividend / divisor;
        }
    }

    private boolean isPrime(int number) {
        // 判断一个数是否为质数的逻辑
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    private boolean isWeekday(DayOfWeek dayOfWeek) {
        // 判断一个日期是否为工作日的逻辑
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    private int square(int number) {
        return number * number;
    }
}
