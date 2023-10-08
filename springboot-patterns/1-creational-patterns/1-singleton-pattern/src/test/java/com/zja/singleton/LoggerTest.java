/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 17:17
 * @Since:
 */
package com.zja.singleton;

import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2023/10/07 17:17
 */
public class LoggerTest {

    @Test
    public void test() {
        Logger logger = Logger.getInstance();
        logger.log("Application started.");

        // 在其他类中也可以通过全局访问点获取Logger实例
        Logger anotherLogger = Logger.getInstance();
        anotherLogger.log("An event occurred.");

        // 输出：
        // Log: Application started.
        // Log: An event occurred.
    }


}
