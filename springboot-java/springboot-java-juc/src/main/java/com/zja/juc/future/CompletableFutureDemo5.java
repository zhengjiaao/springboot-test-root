/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:23
 * @Since:
 */
package com.zja.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 不消费也不返回的方法
 */
public class CompletableFutureDemo5 {

    /**
     * 新建两个任务，一个是return Both，一个是return Message，
     * 在都执行结束以后，因为run是不消费也不返回的，所以入参为0，不需要你们的参数，也不返回，所以没有return。
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture cxf = CompletableFuture.supplyAsync(() -> "Both")
                .runAfterBoth(CompletableFuture.supplyAsync(() -> "Message"), () -> {
                    System.out.println("Done");
                });
        System.out.println(cxf.get());
    }
}
