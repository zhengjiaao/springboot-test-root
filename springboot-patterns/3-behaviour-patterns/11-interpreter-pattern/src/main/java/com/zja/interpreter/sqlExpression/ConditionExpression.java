/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 9:38
 * @Since:
 */
package com.zja.interpreter.sqlExpression;

/**
 * @author: zhengja
 * @since: 2023/10/10 9:38
 */
public class ConditionExpression extends SqlExpression {
    private String condition;

    public ConditionExpression(String condition) {
        this.condition = condition;
    }

    @Override
    public void interpret(SqlContext context) {
        context.setWhereCondition(condition);
    }
}