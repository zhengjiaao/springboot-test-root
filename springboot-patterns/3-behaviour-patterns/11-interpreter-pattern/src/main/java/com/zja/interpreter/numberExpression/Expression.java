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
// 抽象表达式
interface Expression {
    int interpret(Context context);
}