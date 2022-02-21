/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:26
 * @Since:
 */
package com.zja.juc.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 多任务组合
 */
public class CompletableFutureDemo6 {

    public static void main(String[] args) {
        List<CompletableFuture> futures = Arrays.asList(CompletableFuture.completedFuture("hello"),
                CompletableFuture.completedFuture(" world!"),
                CompletableFuture.completedFuture(" hello"),
                CompletableFuture.completedFuture("java!"));

        //allOf：等待所有任务完成. 当最长的耗时的任务完成后，才会结束
        //anyOf：只要有一个任务完成.只要有一个任务执行完成后就返回future并将第一个完成的参数带着一起返回
        final CompletableFuture<Void> allCompleted = CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
        allCompleted.thenRun(() -> {
            futures.stream().forEach(future -> {
                try {
                    System.out.println("get future at:"+System.currentTimeMillis()+", result:"+future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
