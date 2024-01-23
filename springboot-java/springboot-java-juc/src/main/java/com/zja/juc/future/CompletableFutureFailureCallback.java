package com.zja.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 失败回调（Failure Callback）
 *
 * @author: zhengja
 * @since: 2024/01/23 13:55
 */
public class CompletableFutureFailureCallback {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = CompletableFuture
                .supplyAsync(() -> {
                    // 执行线程任务的逻辑
                    // 如果任务执行失败，抛出异常
                    throw new RuntimeException("任务执行失败");
                })
                .exceptionally(ex -> {
                    // 处理任务执行失败的情况，例如记录错误日志
                    System.err.println("任务执行失败：" + ex.getMessage());
                    return null;
                });

        Object o = future.get();
        System.out.println(o);
    }
}
