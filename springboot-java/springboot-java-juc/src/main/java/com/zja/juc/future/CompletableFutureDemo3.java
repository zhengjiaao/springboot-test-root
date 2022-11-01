/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:16
 * @Since:
 */
package com.zja.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 纯消费类型的方法
 */
public class CompletableFutureDemo3 {
    /**
     * thenAcceptBoth() 方法，当task1和task2都返回值以后，然后再一起打印出来。
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> "AcceptBot");
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> "Message");

        task1.thenAcceptBoth(task2, (r1, r2) -> {
            System.out.println("result 1: " + r1);
            System.out.println("result 2: " + r2);
        });
    }
}
