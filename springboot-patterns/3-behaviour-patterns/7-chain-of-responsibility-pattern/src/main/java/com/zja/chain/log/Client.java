/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:02
 * @Since:
 */
package com.zja.chain.log;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:02
 */
public class Client {
    public static void main(String[] args) {
        // 创建处理者对象
        Handler consoleHandler = new ConsoleHandler();
        Handler fileHandler = new FileHandler();
        Handler databaseHandler = new DatabaseHandler();

        // 设置处理者的顺序
        consoleHandler.setNextHandler(fileHandler);
        fileHandler.setNextHandler(databaseHandler);

        // 处理日志请求
        consoleHandler.handleRequest("This is a log message.");

        //输出结果：
        //Log message printed to console: This is a log message.
        //Log message written to file: This is a log message.
        //Log message saved to database: This is a log message.
    }
}
