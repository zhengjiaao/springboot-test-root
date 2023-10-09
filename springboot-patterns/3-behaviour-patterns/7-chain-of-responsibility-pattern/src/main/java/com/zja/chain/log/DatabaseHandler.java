/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:01
 * @Since:
 */
package com.zja.chain.log;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:01
 */
public class DatabaseHandler extends Handler {
    @Override
    protected void processLog(String log) {
        System.out.println("Log message saved to database: " + log);
    }
}