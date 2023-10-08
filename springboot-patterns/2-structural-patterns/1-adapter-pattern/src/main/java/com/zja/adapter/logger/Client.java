/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:35
 * @Since:
 */
package com.zja.adapter.logger;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:35
 */
// 客户端代码
public class Client {
    public static void main(String[] args) {
        Logger logger = new SimpleLogger();
        AdvancedLogger advancedLogger = new LoggerAdapter(logger);

        advancedLogger.logInfo("This is an information message.");
        advancedLogger.logError("This is an error message.");
        //输出结果：
        //Logging: [INFO] This is an information message.
        //Logging: [ERROR] This is an error message.
    }
}