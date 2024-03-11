# springboot-java-function

**说明**

在Java 8中，Function接口是java.util.function包中的一部分，表示接受类型为T的输入并产生类型为R的输出的函数。它是一个函数式接口，意味着可以将其用作lambda表达式或方法引用的赋值目标。

注解 @FunctionalInterface是Java中的一个注解，用于标识一个接口是函数式接口。函数式接口是指只包含一个抽象方法的接口，它可以被用作lambda表达式或方法引用的目标。

## 函数格式

Function接口定义了一个名为apply的方法，具有以下签名：

```java
R apply(T t);
```

apply方法接受一个类型为T的参数，并返回一个类型为R的结果。

## 函数示例

```java
import java.util.function.Function;

public class FunctionExample {
    public static void main(String[] args) {
        // 创建一个将字符串转换为其长度的函数
        Function<String, Integer> stringLength = s -> s.length();

        // 应用函数以获取字符串的长度
        int length = stringLength.apply("Hello, World!");

        System.out.println("长度：" + length);
    }
}
```

## 函数注解@FunctionalInterface的用法

用法：

```java
public class FunctionalInterfaceExample {

    // 使用@FunctionalInterface注解，我们明确告诉编译器这是一个函数式接口，以便在编译时进行验证。
    // 如果接口不符合函数式接口的规范（即有多个抽象方法），编译器会报错
    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

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
}
```