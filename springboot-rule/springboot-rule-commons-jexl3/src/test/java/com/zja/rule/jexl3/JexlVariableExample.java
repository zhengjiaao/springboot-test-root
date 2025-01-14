package com.zja.rule.jexl3;

import org.apache.commons.jexl3.*;

/**
 * 示例 2：带变量的表达式求值
 *
 * @Author: zhengja
 * @Date: 2025-01-14 14:54
 */
public class JexlVariableExample {
    public static void main(String[] args) {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();


        // 定义带变量的表达式
        String expression = "a + b * c";

        // 解析表达式
        JexlExpression e = jexl.createExpression(expression);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();
        context.set("a", 2);
        context.set("b", 3);
        context.set("c", 4);

        // 执行表达式
        Object result = e.evaluate(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: 14
    }
}