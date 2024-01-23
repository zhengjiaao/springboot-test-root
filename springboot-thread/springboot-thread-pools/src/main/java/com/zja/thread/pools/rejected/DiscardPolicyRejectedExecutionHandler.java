package com.zja.thread.pools.rejected;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 被拒绝任务：丢弃任务
 *
 * @author: zhengja
 * @since: 2024/01/23 10:41
 */
public class DiscardPolicyRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        // 什么都不做，直接丢弃任务
    }
}