/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 10:17
 * @Since:
 */
package com.zja.junit5;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit5 基本方法测试
 *
 * @BeforeEach：在每个测试方法之前执行的操作。
 * @AfterEach：在每个测试方法之后执行的操作。
 * @BeforeAll：在所有测试方法之前执行的操作。
 * @AfterAll：在所有测试方法之后执行的操作。
 * @author: zhengja
 * @since: 2023/10/16 10:17
 */
public class JUnitTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("@BeforeAll：在所有测试方法之前执行的操作");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("@BeforeEach：在每个测试方法之前执行的操作");
    }

    @Test
    void testAddition() {
        System.out.println("测试方法：testAddition()");
        int result = Calculator.add(2, 3);
        assertEquals(5, result, "Addition result is incorrect");
    }

    @Test
    void testDivision() {
        System.out.println("测试方法：testDivision()");
        double result = Calculator.divide(10, 2);
        assertEquals(5.0, result, "Division result is incorrect");
    }

    //@Disabled 注解表示禁用一个测试方法，可以用于临时跳过某些测试。
    @Test
    @Disabled("Skipping this test for now")
    // 被禁用的测试方法
    void testSubtraction() {
        System.out.println("测试方法：testSubtraction()");
        int result = Calculator.subtract(5, 3);
        assertEquals(2, result, "Subtraction result is incorrect");
    }

    @AfterEach
    void afterEach() {
        System.out.println("@AfterEach：在每个测试方法之后执行的操作");
    }

    @AfterAll
    static void afterAll() {
        System.out.println(" @AfterAll：在所有测试方法之后执行的操作");
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
}
