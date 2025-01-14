# springboot-rule-commons-jexl3

- [commons-jexl | github](https://github.com/apache/commons-jexl)

Apache Commons JEXL 是一个库，有助于在用 Java 编写的应用程序和框架中实现脚本功能。

## commons-jexl3 应用场景

commons-jexl3 在不同场景下的应用：

1. 基本表达式求值：用于简单的数学运算。
2. 带变量的表达式求值：支持动态变量的计算。
3. 条件判断表达式：实现逻辑判断和分支处理。
4. 规则引擎应用：适用于复杂的业务规则判断。

通过这些示例，你可以更好地理解如何在 Java 应用中使用 commons-jexl3 来简化复杂的逻辑判断和数据处理。

## 依赖引入

```xml

<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-jexl3</artifactId>
    <version>3.4.0</version>
</dependency>
```

## 代码实例

示例 1：基本表达式求值

```java
/**
 * 示例 1：基本表达式求值
 *
 * @Author: zhengja
 * @Date: 2025-01-14 14:43
 */
public class JexlBasicExample {
    public static void main(String[] args) {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义表达式
        String expression = "2 + 3 * 4";

        // 解析表达式
        JexlExpression e = jexl.createExpression(expression);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();

        // 执行表达式
        Object result = e.evaluate(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: 14
    }
}
```