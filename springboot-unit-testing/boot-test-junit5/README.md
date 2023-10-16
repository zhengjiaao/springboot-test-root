# boot-test-junit5

**说明**

基于springboot test + junit5 单元测试实例。

JUnit 5（也称为 JUnit Jupiter）进行单元测试时，你可以使用 @Test 注解来标记测试方法，使用断言来验证测试结果。

## 依赖引入

```xml

<dependencies>
    <!-- spring-boot-starter-test 单元测试-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <!--已包含junit5依赖junit-jupiter-->
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!--可选地，boot-test已经包含了junit-->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.6.3</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

## JUnit 5 实例

### JUnit 5 基础单元测试实例

* @Test：用于标识测试方法。
* @BeforeEach：在每个测试方法之前执行的操作。
* @AfterEach：在每个测试方法之后执行的操作。
* @BeforeAll：在所有测试方法之前执行的操作。
* @AfterAll：在所有测试方法之后执行的操作。

```java
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
```

### JUnit 5 进行断言的完整测试

当使用 JUnit 5 进行断言的完整测试时，你可以结合使用测试方法和断言方法来验证预期结果和实际结果之间的差异。

* assertEquals 断言用于验证两个值是否相等。
* assertArrayEquals 断言用于验证两个数组是否相等。
* assertThrows 断言用于验证给定的代码块是否抛出了预期的异常。
* assertTimeout 断言用于验证给定的代码块是否在超时时间内完成。
* assertIterableEquals 断言用于验证两个可迭代对象是否相等。
* assertLinesMatch 断言用于验证两个文件的行内容是否匹配。

代码示例：

```java
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionTest {

    @Test
    void testAddition() {
        int result = Calculator.add(2, 3);
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
        assertArrayEquals(expected, actual, "Array elements are not equal");
    }

    @Test
    void testException() {
        assertThrows(ArithmeticException.class, () -> {
            int result = Calculator.divide(10, 0);
        });
    }

    @Test
    void testTimeout() {
        assertTimeout(Duration.ofSeconds(1), () -> {
            Thread.sleep(500);
        });
    }

    @Test
    void testList() {
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = Arrays.asList("apple", "banana", "cherry");
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
        assertLinesMatch(Files.readAllLines(expectedFile), Files.readAllLines(actualFile), "File contents are not equal");

        // 删除文件
        Files.delete(expectedFile);
        Files.delete(actualFile);
    }

    class Calculator {
        static int add(int a, int b) {
            return a + b;
        }

        static double divide(int dividend, int divisor) {
            if (divisor == 0) {
                throw new ArithmeticException("Divisor cannot be zero");
            }
            return (double) dividend / divisor;
        }
    }
}
```

### JUnit 5 扩展功能实例

* @Test：用于标识测试方法。
* @Disabled：用于禁用测试方法。
* @RepeatedTest：用于重复运行测试方法。
* @BeforeEach：在每个测试方法之前执行的操作。
* @AfterEach：在每个测试方法之后执行的操作。
* @BeforeAll：在所有测试方法之前执行的操作。
* @AfterAll：在所有测试方法之后执行的操作。
* @Nested：用于创建嵌套的测试类。

```java
//代码太多，参考单元测试示例：JUnitExtendedTest.java
```











