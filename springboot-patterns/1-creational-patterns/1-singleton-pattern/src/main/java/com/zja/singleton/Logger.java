/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 17:16
 * @Since:
 */
package com.zja.singleton;

/**
 * @author: zhengja
 * @since: 2023/10/07 17:16
 */
public class Logger {
    private static Logger instance;

    // 私有化构造函数，防止外部实例化
    private Logger() {
        // 初始化日志记录器
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        // 记录日志
        System.out.println("Log: " + message);
    }

    // 其他日志相关的方法...
}
