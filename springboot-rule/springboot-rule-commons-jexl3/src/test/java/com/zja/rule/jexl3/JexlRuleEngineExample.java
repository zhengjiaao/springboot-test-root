package com.zja.rule.jexl3;

import org.apache.commons.jexl3.*;

/**
 * 示例 4：规则引擎应用
 *
 * @Author: zhengja
 * @Date: 2025-01-14 14:56
 */
public class JexlRuleEngineExample {
    public static void main(String[] args) {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义规则表达式，使用三元运算符替代 if-else
        String rule = "temperature > 37.5 ? 'Fever Detected' : 'Normal Temperature'";

        // 解析表达式
        JexlExpression e = jexl.createExpression(rule);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();
        context.set("temperature", 38.2);

        // 执行表达式
        Object result = e.evaluate(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: Fever Detected
    }
}