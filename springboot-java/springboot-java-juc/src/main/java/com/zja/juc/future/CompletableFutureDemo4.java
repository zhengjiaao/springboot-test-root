/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:19
 * @Since:
 */
package com.zja.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 有返回值类型的方法
 */
public class CompletableFutureDemo4 {
    /**
     * thenApply在拿到值以后再和world拼接，然后再返回值，然后通过get获取到值。
     */
    /*public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture cf = CompletableFuture.supplyAsync(() -> "hello ").thenApply(r -> {
            return r + "world";
        });
        System.out.println(cf.get());
    }*/

    //thenCombineAsync的作用就是将task1和task2的值都拿到以后返回值。
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "Combine")
                .thenCombineAsync(CompletableFuture.supplyAsync(() -> "Message"), (r1, r2) -> r1 + r2);
        System.out.println(cf.get());
    }

}
