/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 15:57
 * @Since:
 */
package com.zja.chain.log;

/**
 * @author: zhengja
 * @since: 2023/10/09 15:57
 */
public class ConsoleHandler extends Handler {
    @Override
    protected void processLog(String log) {
        System.out.println("Log message printed to console: " + log);
    }
}