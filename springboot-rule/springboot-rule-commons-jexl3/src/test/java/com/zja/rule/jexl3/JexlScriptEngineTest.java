package com.zja.rule.jexl3;

import org.apache.commons.jexl3.*;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2025-01-14 15:02
 */
public class JexlScriptEngineTest {

    @Test
    public void test_1() {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义一个简单的脚本
        String scriptText = "var i = 0; do ; while((i+=1) < 10); i";

        // 创建脚本
        JexlScript script = jexl.createScript(scriptText);

        // 创建上下文
        JexlContext context = new MapContext();

        // 执行脚本
        Object result = script.execute(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: 10
    }

    @Test
    public void test_2() {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义条件判断表达式
        String expression = "if (age > 18) { 'Adult' } else { 'Minor' }";

        // 解析表达式
        JexlScript script = jexl.createScript(expression);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();
        context.set("age", 20);

        // 执行表达式
        Object result = script.execute(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: Adult
    }

    @Test
    public void test_3() {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义规则表达式
        String rule = "if (temperature > 37.5) { 'Fever Detected' } else { 'Normal Temperature' }";

        // 解析表达式
        JexlScript script = jexl.createScript(rule);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();
        context.set("temperature", 38.2);

        // 执行表达式
        Object result = script.execute(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: Fever Detected
    }

    @Test
    public void test_4() {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义规则表达式
        String rule = "if (temperature > 37.5) { 'Fever Detected' } else { 'Normal Temperature' }";

        // 解析表达式
        JexlScript script = jexl.createScript(rule);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();
        context.set("temperature", 38.2);

        // 执行表达式
        Object result = script.execute(context);
        System.out.println("Result: " + result);
    }
}
