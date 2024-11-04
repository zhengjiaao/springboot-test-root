# springboot-rule-aviator

- [aviatorscript | github](https://github.com/killme2008/aviatorscript)
- [aviatorscript | 文档](https://www.yuque.com/boyan-avfmj/aviatorscript)

aviator 表达式引擎(规则引擎)：

## 快速开始

依赖

```xml
<!-- 规则引擎 -->
<dependency>
    <groupId>com.googlecode.aviator</groupId>
    <artifactId>aviator</artifactId>
    <version>5.4.3</version>
</dependency>
```

代码示例

```java
public class AviatorTest {

    // 编译脚本文本
    @Test
    public void test_1() {
        // Compile a script
        Expression script = AviatorEvaluator.getInstance().compile("println('Hello, AviatorScript!');");
        script.execute();
    }

    // 编译脚本文本
    @Test
    public void test_2() throws IOException {
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/hello.av", true);
        exp.execute();
    }

    // 动态执行表达式
    @Test
    public void testDynamicExpression() {
        String expression = "1 + 2 * 3";
        Object result = AviatorEvaluator.execute(expression);
        assertEquals(7L, result);
    }

    // 使用变量
    @Test
    public void testVariableUsage() {
        Map<String, Object> env = new HashMap<>();
        env.put("x", 10);
        env.put("y", 20);
        String expression = "x + y";
        Object result = AviatorEvaluator.execute(expression, env);
        assertEquals(30L, result);
    }

}
```