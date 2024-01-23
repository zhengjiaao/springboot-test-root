package com.zja.thread.pools.rejected;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义的饱和策略来处理被拒绝的任务：定义在任务被拒绝执行时的处理逻辑，如记录日志、丢弃任务或等待一段时间后重试等。
 *
 * @author: zhengja
 * @since: 2024/01/23 10:22
 */
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        // 自定义处理逻辑
        System.out.println("Task has been rejected: " + runnable);
    }
}
