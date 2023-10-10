/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 9:38
 * @Since:
 */
package com.zja.interpreter.sqlExpression;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/10 9:38
 */
public class Client {
    public static void main(String[] args) {
        SqlParser parser = new SqlParser();
        SqlContext context = parser.parse("SELECT name, age FROM users WHERE age > 18");

        List<String> fields = context.getSelectFields();
        String tableName = context.getTableName();
        String condition = context.getWhereCondition();

        System.out.println("Fields: " + fields);
        System.out.println("Table Name: " + tableName);
        System.out.println("Condition: " + condition);

        //输出结果：
        //Fields: [SELECT, name,, age]
        //Table Name: users
        //Condition: age
    }
}
