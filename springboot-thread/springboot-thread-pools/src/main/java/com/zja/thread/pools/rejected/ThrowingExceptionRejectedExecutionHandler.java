package com.zja.thread.pools.rejected;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 被拒绝任务：抛出异常
 *
 * @author: zhengja
 * @since: 2024/01/23 10:41
 */
public class ThrowingExceptionRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        // 抛出异常
        throw new RejectedExecutionException("Task has been rejected: " + runnable);
    }
}
