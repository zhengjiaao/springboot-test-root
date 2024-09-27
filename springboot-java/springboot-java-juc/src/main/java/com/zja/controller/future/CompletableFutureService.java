package com.zja.controller.future;

import com.zja.juc.future.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: zhengja
 * @Date: 2024-09-24 9:40
 */
@Slf4j
@Component
public class CompletableFutureService {

    public String getUserInfo(String userId) {
        log.info("getUserInfo:{}", userId);
        return "userInfo";
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runAsync() throws ExecutionException, InterruptedException {
        log.info("runAsync");

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            sleep(1000);
            log.info("run async.......");
        });

        // 等待任务运行结束
        future.get();
    }

    public Object supplyAsync() throws ExecutionException, InterruptedException {
        log.info("supplyAsync");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            log.info("supply async.......");
            return "supplyAsync";
        });

        // 等待任务运行结束
        // future.get();
        // log.info("result:{}", future.get());

        return "成功";
    }

    public Object supplyAsyncV2() throws ExecutionException, InterruptedException {
        log.info("supplyAsyncV2");

        // 当supplyAsync执行完成（无论是否发生异常），继续执行 whenComplete、handle，若异常，不会执行exceptionally
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync 若此方法异常，则返回数据null，并返回异常");

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return 10;
        }).handle((params, throwable) -> {
            log.info("handle 若之前的任何方法异常，则参数为null，不会执行exceptionally");
            log.info("handle params:{}", params);
            if (throwable != null) {
                log.error("handle error:", throwable);
            }

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return "handle";
        }).whenComplete((params, throwable) -> {
            // 任务（正常/异常）执行完后才会执行此方法，处理上一步的处理结果，无返回值，下一个方法的参数是上一步的结果
            log.info("whenComplete 若之前的任何方法异常，则参数为null，不会执行exceptionally");
            log.info("whenComplete params:{}", params);
            if (throwable != null) {
                log.error("whenComplete error:", throwable);
            }

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

        }).handle((params, throwable) -> {
            log.info("handle2 若之前的任何方法异常，则参数为null，不会执行exceptionally");
            log.info("handle2 params:{}", params);
            if (throwable != null) {
                log.error("handle2 error:", throwable);
            }

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return "handle2";
        }).thenRun(() -> {
            // 任务正常执行完后才会执行此方法，不关心且无法处理上一步的处理结果，无返回值，下一个方法的参数是null
            log.info("thenRun");

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

        }).thenApply(params -> {
            // 任务正常执行完后才会执行此方法，消费上一步的处理结果，有返回值
            log.info("thenApply 若前一个方法异常，则不会执行");
            log.info("thenApply params:{}", params);

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return "thenApply";
        }).thenAccept(params -> {
            // 任务正常执行完后才会执行此方法，消费上一步的处理结果，无返回值，下一个方法的参数是null
            log.info("thenAccept 若之前的任何方法异常，则不会执行");
            log.info("thenAccept params:{}", params);

            // 自定义抛出异常程序
            // int i = 10 / 0;

        }).thenApply(params -> {
            // 任务正常执行完后才会执行此方法，消费上一步的处理结果，有返回值
            log.info("thenApply2 若前一个方法异常，则不会执行");
            log.info("thenApply2 params:{}", params);

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return "thenApply2";
        }).exceptionally(throwable -> {
            log.info("exceptionally 若异常的方法之后，存在捕获异常的方法，则不会执行，例如：(params, throwable)");
            log.error("exceptionally error:", throwable);

            return "exceptionally";
            // return null;
        });

        future.get();
        log.info("result:{}", future.get());

        return "成功";
    }

    public Object supplyAsyncV3() throws ExecutionException, InterruptedException {
        log.info("supplyAsyncV3");

        // 当supplyAsync执行完成（无论是否发生异常），继续执行 whenComplete、handle，若异常，不会执行exceptionally
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync 若此方法异常，则返回数据null，并返回异常");

            // 自定义抛出异常程序
            int i = 10 / 0;

            return 10;
        }).handleAsync((params, throwable) -> {
            log.info("handleAsync 若之前的任何方法异常，则参数为null，不会执行exceptionally");
            log.info("handleAsync params:{}", params);
            if (throwable != null) {
                log.error("handle error:", throwable);
            }

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return "handleAsync";
        }).whenCompleteAsync((params, throwable) -> {
            // 任务（正常/异常）执行完后才会执行此方法，处理上一步的处理结果，无返回值，下一个方法的参数是上一步的结果
            log.info("whenCompleteAsync 若之前的任何方法异常，则参数为null，不会执行exceptionally");
            log.info("whenCompleteAsync params:{}", params);
            if (throwable != null) {
                log.error("whenCompleteAsync error:", throwable);
            }

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

        }).handleAsync((params, throwable) -> {
            log.info("handleAsync2 若之前的任何方法异常，则参数为null，不会执行exceptionally");
            log.info("handleAsync2 params:{}", params);
            if (throwable != null) {
                log.error("handleAsync2 error:", throwable);
            }

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

            return "handleAsync2";
        }).thenRunAsync(() -> {
            // 任务正常执行完后才会执行此方法，不关心且无法处理上一步的处理结果，无返回值，下一个方法的参数是null
            log.info("thenRunAsync");

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

        }).thenApplyAsync(params -> {
            // 任务正常执行完后才会执行此方法，消费上一步的处理结果，有返回值
            log.info("thenApplyAsync 若前一个方法异常，则不会执行");
            log.info("thenApplyAsync params:{}", params);

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

            return "thenApplyAsync";
        }).thenAcceptAsync(params -> {
            // 任务正常执行完后才会执行此方法，消费上一步的处理结果，无返回值，下一个方法的参数是null
            log.info("thenAcceptAsync 若之前的任何方法异常，则不会执行");
            log.info("thenAcceptAsync params:{}", params);

            // 自定义抛出异常程序
            // int i = 10 / 0;

            // 自定义定时
            // sleep(2000);

        }).thenApplyAsync(params -> {
            // 任务正常执行完后才会执行此方法，消费上一步的处理结果，有返回值
            log.info("thenAcceptAsync2 若前一个方法异常，则不会执行");
            log.info("thenAcceptAsync2 params:{}", params);

            // 自定义抛出异常程序
            // int i = 10 / 0;

            return "thenAcceptAsync2";
        }).exceptionally(throwable -> {
            log.info("exceptionally 若异常的方法之后，存在捕获异常的方法，则不会执行，例如：(params, throwable)");
            log.error("exceptionally error:", throwable);

            return "exceptionally";
            // return null;
        });

        future.get();
        log.info("result:{}", future.get());

        return "成功";
    }

}
