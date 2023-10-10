/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 9:16
 * @Since:
 */
package com.zja.interpreter.numberExpression;

/**
 * @author: zhengja
 * @since: 2023/10/10 9:16
 */
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 构建解析树
        Expression expression = new SubtractExpression(
                new AddExpression(new NumberExpression(10), new NumberExpression(5)),
                new NumberExpression(2)
        );

        // 创建上下文
        Context context = new Context();

        // 解释并执行表达式
        int result = expression.interpret(context);
        System.out.println("Result: " + result); //输出：Result: 13
    }
}
