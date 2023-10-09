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
public abstract class Handler {
    private Handler nextHandler;

    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }

    public void handleRequest(String log) {
        processLog(log);
        if (nextHandler != null) {
            nextHandler.handleRequest(log);
        }
    }

    protected abstract void processLog(String log);
}