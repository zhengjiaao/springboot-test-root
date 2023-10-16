/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 10:47
 * @Since:
 */
package com.zja.junit5;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 进行断言的完整测试
 * <p>
 * assertEquals 断言用于验证两个值是否相等。
 * assertArrayEquals 断言用于验证两个数组是否相等。
 * assertThrows 断言用于验证给定的代码块是否抛出了预期的异常。
 * assertTimeout 断言用于验证给定的代码块是否在超时时间内完成。
 * assertIterableEquals 断言用于验证两个可迭代对象是否相等。
 * assertLinesMatch 断言用于验证两个文件的行内容是否匹配。
 * </p>
 *
 * @author: zhengja
 * @since: 2023/10/16 10:47
 */
public class JUnitAssertionTest {

    @Test
    void testAddition() {
        int result = Calculator.add(2, 3);
        //assertEquals 断言用于验证两个值是否相等。
        assertEquals(5, result, "Addition result is incorrect");
    }

    @Test
    void testDivision() {
        double result = Calculator.divide(10, 2);
        assertEquals(5.0, result, "Division result is incorrect");
    }

    @Test
    void testArray() {
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};
        //assertArrayEquals 断言用于验证两个数组是否相等。
        assertArrayEquals(expected, actual, "Array elements are not equal");
    }

    @Test
    void testException() {
        //assertThrows 断言用于验证给定的代码块是否抛出了预期的异常。
        assertThrows(ArithmeticException.class, () -> {
            int result = (int) Calculator.divide(10, 0);
        });
    }

    @Test
    void testTimeout() {
        //assertTimeout 断言用于验证给定的代码块是否在超时时间内完成。
        assertTimeout(Duration.ofSeconds(1), () -> {
            Thread.sleep(500);
        });
    }

    @Test
    void testList() {
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = Arrays.asList("apple", "banana", "cherry");
        //assertIterableEquals 断言用于验证两个可迭代对象是否相等。
        assertIterableEquals(expected, actual, "Lists are not equal");
    }

    @Test
    void testStream() {
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = Arrays.stream(new String[]{"apple", "banana", "cherry"})
                .collect(Collectors.toList());
        assertIterableEquals(expected, actual, "Streams are not equal");
    }

    @Test
    void testFile() throws IOException {
        Path expectedFile = Paths.get("expected.txt");
        Path actualFile = Paths.get("actual.txt");

        // 创建预期文件和实际文件
        Files.write(expectedFile, Arrays.asList("apple", "banana", "cherry"), StandardCharsets.UTF_8);
        Files.write(actualFile, Arrays.asList("apple", "banana", "cherry"), StandardCharsets.UTF_8);

        // 验证文件内容是否相等
        //assertLinesMatch 断言用于验证两个文件的行内容是否匹配。
        assertLinesMatch(Files.readAllLines(expectedFile), Files.readAllLines(actualFile), "File contents are not equal");

        // 删除文件
        Files.delete(expectedFile);
        Files.delete(actualFile);
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
