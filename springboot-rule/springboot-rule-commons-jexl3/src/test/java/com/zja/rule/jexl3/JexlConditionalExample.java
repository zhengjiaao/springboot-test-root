package com.zja.rule.jexl3;

import org.apache.commons.jexl3.*;

/**
 * 示例 3：条件判断表达式
 *
 * @Author: zhengja
 * @Date: 2025-01-14 14:55
 */
public class JexlConditionalExample {
    public static void main(String[] args) {
        // 创建 Jexl 引擎
        JexlEngine jexl = new JexlBuilder().create();

        // 定义条件判断表达式，使用三元运算符
        String expression = "age > 18 ? 'Adult' : 'Minor'";

        // 解析表达式
        JexlExpression e = jexl.createExpression(expression);

        // 创建上下文并添加变量
        JexlContext context = new MapContext();
        context.set("age", 20);

        // 执行表达式
        Object result = e.evaluate(context);

        // 输出结果
        System.out.println("Result: " + result);  // 输出: Result: Adult
    }
}