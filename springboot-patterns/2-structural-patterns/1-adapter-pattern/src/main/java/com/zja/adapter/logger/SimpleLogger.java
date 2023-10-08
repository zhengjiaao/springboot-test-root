/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:33
 * @Since:
 */
package com.zja.adapter.logger;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:33
 */
// 已有的Logger接口实现
public class SimpleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("Logging: " + message);
    }
}