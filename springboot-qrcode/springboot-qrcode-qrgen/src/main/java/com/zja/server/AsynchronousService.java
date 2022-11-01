package com.zja.server;

import java.util.concurrent.Future;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/7 14:04
 */
public interface AsynchronousService {
    /**
     * 异步
     * @return
     */
    String testAsynchronous();

    /**
     * 同步
     * @return
     */
    void testSynchronize();

    /**
     * 自定义线程池
     */
    void asyncEvent() throws InterruptedException;

    /**
     * 异步回调及超时处理
     * @return
     * @throws InterruptedException
     */
    Future<String> asyncFuture() throws InterruptedException;
}
