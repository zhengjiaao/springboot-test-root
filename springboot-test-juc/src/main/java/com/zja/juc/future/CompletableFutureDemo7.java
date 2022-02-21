/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:31
 * @Since:
 */
package com.zja.juc.future;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo7 {
    @SneakyThrows
    public static void main(String[] args) {
        CompletableFuture cf = CompletableFuture.supplyAsync(() ->{
            throw new  RuntimeException("Occur exception");
        }).whenComplete((rs,th) -> { //whenComplete 表示当任务执行完成后，会触发的方法.不论前置的CompletionStage任务是正常执行结束还是出现异常，都能够触发特定的 action 方法
            if (th !=null){
                System.out.println("前置任务出现异常");
            }else {
                System.out.println("前置任务正常执行"+rs);
            }
        }).handle((rs,th) -> th !=null ? "出现异常":rs) //handle 表示前置任务执行完成后，不管前置任务执行状态是正常还是异常，都会执行handle中的fn 函数
                .exceptionally(ex ->{ //exceptionally：接受一个 fn 函数，当上一个CompletionStage出现异常时，会把该异常作为参数传递到 fn 函数
                    System.out.println(ex);
                    return "Exceptionally";
                });
        System.out.println(cf.get());
    }
}
