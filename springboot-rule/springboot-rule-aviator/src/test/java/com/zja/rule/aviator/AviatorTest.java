package com.zja.rule.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @Author: zhengja
 * @Date: 2024-11-04 13:51
 */
public class AviatorTest {

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

    // 条件判断
    @Test
    public void testConditionalExpression() {
        Map<String, Object> env = new HashMap<>();
        env.put("x", 10);
        // String expression = "if(x > 5, 'greater', 'less')";
        String expression = "if(x > 5) {'greater'} else {'less'}";
        Object result = AviatorEvaluator.execute(expression, env);
        assertEquals("greater", result);
    }

    // 循环结构
    @Test
    @Deprecated
    public void testForLoop() {
        String expression = "sum = 0; for(i in [1..5]) { sum = sum + i }; sum";
        Object result = AviatorEvaluator.execute(expression);
        assertEquals(15L, result);
    }

    // 类型转换
    @Test
    public void testTypeConversion() {
        String expression = "'123' + 456";
        Object result = AviatorEvaluator.execute(expression);
        assertEquals("123456", result);
    }

    // 性能优化
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void testPerformance(int i) {
        String expression = "1 + 2 * 3";
        Object result = AviatorEvaluator.execute(expression);
        assertEquals(7L, result);
    }

    // 线程安全
    @Test
    public void testThreadSafety() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String expression = "1 + 2 * 3";

        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                Object result = AviatorEvaluator.execute(expression);
                assertEquals(7, result);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    // 错误处理
    @Test
    public void testErrorHandling() {
        String expression = "1 / 0";
        assertThrows(ArithmeticException.class, () -> {
            AviatorEvaluator.execute(expression);
        });
    }

    @BeforeEach
    public void setUp() {
        AviatorEvaluator.addFunction(new IsEvenFunction());
    }

    // 自定义函数
    @Test
    public void testCustomFunction() {
        String expression = "isEven(10)";
        Object result = AviatorEvaluator.execute(expression);
        assertEquals(true, result);
    }

    public static class IsEvenFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            int number = ((Number) arg1.getValue(env)).intValue();
            return AviatorBoolean.valueOf(number % 2 == 0);
        }

        @Override
        public String getName() {
            return "isEven";
        }
    }
}
