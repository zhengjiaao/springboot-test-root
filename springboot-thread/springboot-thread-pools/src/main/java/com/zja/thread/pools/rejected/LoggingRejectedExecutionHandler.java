package com.zja.thread.pools.rejected;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 被拒绝任务：日子记录
 *
 * @author: zhengja
 * @since: 2024/01/23 10:42
 */
public class LoggingRejectedExecutionHandler implements RejectedExecutionHandler {
    private static final Logger logger = Logger.getLogger(LoggingRejectedExecutionHandler.class.getName());

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        // 日子记录
        logger.log(Level.WARNING, "Task has been rejected: " + runnable);
    }
}
