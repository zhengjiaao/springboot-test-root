package com.zja.thread.pools.rejected;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 被拒绝任务：重试
 * <p>
 * 当任务被拒绝执行时，RetryRejectedExecutionHandler会尝试将任务重新放入线程池的任务队列中，最多重试[MAX_RETRIES]次。如果重试次数超过最大重试次数，那么会抛出RejectedExecutionException异常。
 *
 * @author: zhengja
 * @since: 2024/01/23 10:49
 */
public class RetryRejectedExecutionHandler implements RejectedExecutionHandler {
    private static final int MAX_RETRIES = 3; // 最大重试次数

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        int retries = 0;
        boolean executed = false;

        while (retries < MAX_RETRIES && !executed) {
            try {
                executor.getQueue().put(runnable); // 将任务重新放入任务队列中
                executed = true;
            } catch (InterruptedException e) {
                // 可以根据实际需求进行异常处理
                e.printStackTrace();
            }

            retries++;
        }

        if (!executed) {
            throw new RejectedExecutionException("Task has been rejected and retries have been exhausted: " + runnable);
        }
    }
}