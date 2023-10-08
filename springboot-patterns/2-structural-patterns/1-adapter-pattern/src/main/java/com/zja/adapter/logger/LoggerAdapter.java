/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:34
 * @Since:
 */
package com.zja.adapter.logger;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:34
 */
// 适配器类：将Logger适配到AdvancedLogger接口
public class LoggerAdapter implements AdvancedLogger {
    private Logger logger;

    public LoggerAdapter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void logInfo(String message) {
        logger.log("[INFO] " + message);
    }

    @Override
    public void logError(String message) {
        logger.log("[ERROR] " + message);
    }
}