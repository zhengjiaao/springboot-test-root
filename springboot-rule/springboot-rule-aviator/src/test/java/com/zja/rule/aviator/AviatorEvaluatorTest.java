package com.zja.rule.aviator;

import com.googlecode.aviator.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author: zhengja
 * @Date: 2024-11-04 14:25
 */
public class AviatorEvaluatorTest {

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

    // 执行
    @Test
    public void test_3() {
        String expression = "a-(b-c) > 100";
        Expression compiledExp = AviatorEvaluator.compile(expression);
        // Execute with injected variables.
        Boolean result =
                (Boolean) compiledExp.execute(compiledExp.newEnv("a", 100.3, "b", 45, "c", -199.100));
        System.out.println(result);

        // a-(b-c) > 100
        // => 100.3 - (45 - -199.100) > 100
        // => 100.3 - 244.1 > 100
        // => -143.8 > 100
        // => false
    }

    // 语法校验
    @Test
    public void test_4() {
        AviatorEvaluator.validate("1 +* 2");
    }

    // 引擎模式
    @Test
    public void test_5() {
        // 默认是：运行期优化优先
        AviatorEvaluator.getInstance()
                .setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.COMPILE); // 改为了：编译优先模式
    }

    // 创建解释器：建一个解释器并尝试打开跟踪执行（生产环境不建议打开跟踪执行）
    @Test
    public void test_6() {
        // 创建解释器
        AviatorEvaluatorInstance engine = AviatorEvaluator.newInstance(EvalMode.INTERPRETER);
        // 打开跟踪执行
        engine.setOption(Options.TRACE_EVAL, true);

        Expression exp = engine.compile("score > 80 ? 'good' : 'bad'");
        System.out.println(exp.execute(exp.newEnv("score", 100)));
        System.out.println(exp.execute(exp.newEnv("score", 50)));
    }

}
