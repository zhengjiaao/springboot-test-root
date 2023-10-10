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
public class SqlParser {
    public SqlContext parse(String sqlQuery) {
        SqlContext context = new SqlContext();

        // 解析SQL查询并构建SQL表达式对象
        // 这里使用简单的字符串分割进行解析，实际应用中可能需要更复杂的解析逻辑
        String[] parts = sqlQuery.split("\\s");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].replace(",", "");
            if (part.equalsIgnoreCase("SELECT")) {
                int j = i++;
                while (!parts[j].equalsIgnoreCase("FROM")) {
                    SqlExpression expression = new FieldExpression(parts[j]);
                    expression.interpret(context);
                    j++;
                }
            } else if (part.equalsIgnoreCase("FROM")) {
                i++;
                SqlExpression expression = new TableNameExpression(parts[i]);
                expression.interpret(context);
            } else if (part.equalsIgnoreCase("WHERE")) {
                i++;
                SqlExpression expression = new ConditionExpression(parts[i]);
                expression.interpret(context);
            }
        }

        return context;
    }
}