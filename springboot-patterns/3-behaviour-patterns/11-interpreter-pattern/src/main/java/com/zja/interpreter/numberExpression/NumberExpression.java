/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 9:15
 * @Since:
 */
package com.zja.interpreter.numberExpression;

/**
 * @author: zhengja
 * @since: 2023/10/10 9:15
 */
// 终结符表达式 - 数字表达式
class NumberExpression implements Expression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int interpret(Context context) {
        return number;
    }
}