package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: zhengja
 * @since: 2024/03/11 10:08
 */
public class OptionalFutureExample {

    // 当在并发编程中使用 Optional 时，可以通过 Optional 对象来处理可能为空的结果或共享变量。
    @Test
    public void test_1() {
        Optional<String> optionalResult = performAsyncTask();
        if (optionalResult.isPresent()) {
            String result = optionalResult.get();
            System.out.println("Result: " + result);
        } else {
            System.out.println("No result available."); // 没有可用的结果
        }
    }

    // 该任务返回一个可能为空的结果
    public static Optional<String> performAsyncTask() {
        // 模拟一个异步任务，返回一个可能为空的结果
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个可能返回 null 的情况
            return null;
        });

        try {
            // 等待异步任务完成并获取结果
            String result = future.get();
            return Optional.ofNullable(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // 合并多个异步任务的结果
    @Test
    public void test_2() {
        Optional<String> combinedResult = combineAsyncResults();
        combinedResult.ifPresent(result -> System.out.println("Combined Result: " + result));
    }

    // 创建了两个异步任务 future1 和 future2，每个任务返回一个字符串结果。然后，我们使用 thenCombine() 方法将两个任务的结果合并为一个字符串。最后，我们将结果包装在 Optional 中并返回。
    public static Optional<String> combineAsyncResults() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Result 1");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Result 2");

        return Optional.ofNullable(future1.thenCombine(future2, (result1, result2) -> result1 + " " + result2)
                .toCompletableFuture()
                .join());
    }


    // 使用 Optional 处理异常情况
    @Test
    public void test_3() {
        Optional<String> optionalResult = performAsyncTaskException1();
        if (optionalResult.isPresent()) {
            System.out.println("Result: " + optionalResult.get());
        } else {
            System.out.println("Task failed or no result available.");
        }
    }

    public static Optional<String> performAsyncTaskException1() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个可能抛出异常的情况
            throw new RuntimeException("Task failed.");
        });

        try {
            String result = future.get();
            return Optional.of(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // 超时处理
    @Test
    public void test_4() {
        Optional<String> optionalResult = performAsyncTaskWithTimeout();
        if (optionalResult.isPresent()) {
            System.out.println("Result: " + optionalResult.get());
        } else {
            System.out.println("Task timed out or no result available.");
        }
    }

    public static Optional<String> performAsyncTaskWithTimeout() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个可能耗时较长的任务
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result";
        });

        try {
            String result = future.get(3, TimeUnit.SECONDS);
            return Optional.of(result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    // 并行执行多个任务，返回第一个非空结果
    @Test
    public void test_5() {
        Optional<String> optionalResult = getFirstNonEmptyResult();
        optionalResult.ifPresent(result -> System.out.println("First non-empty result: " + result));
    }

    public static Optional<String> getFirstNonEmptyResult() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个返回空结果的任务
            return null;
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个返回非空结果的任务
            return "Result";
        });

        List<CompletableFuture<String>> futures = Arrays.asList(future1, future2);
        CompletableFuture<Object> anyResult = CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]));

        try {
            Object result = anyResult.get();
            return Optional.ofNullable((String) result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // 使用 Optional 进行结果过滤和转换
    @Test
    public void test_6() {
        Optional<String> optionalResult = performAsyncTaskFilter();
        optionalResult.ifPresent(result -> System.out.println("Result: " + result));
    }

    public static Optional<String> performAsyncTaskFilter() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个返回结果的任务
            return "Result";
        });

        return Optional.ofNullable(future.thenApply(result -> {
            // 对结果进行过滤和转换
            if (result.startsWith("R")) {
                return result.toUpperCase();
            } else {
                return null; // 这里返回 null 表示结果不符合条件，将其过滤掉
            }
        }).join());
    }


    // 使用 Optional 处理多个异步任务的异常
    @Test
    public void test_7() {
        Optional<String> optionalResult = performAsyncTask();
        optionalResult.ifPresent(result -> System.out.println("Result: " + result));
    }

    public static Optional<String> performAsyncTaskException() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个可能抛出异常的任务
            throw new RuntimeException("Task 1 failed.");
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // 执行一些耗时的操作，返回结果
            // 这里模拟一个正常完成的任务
            return "Result 2";
        });

        CompletableFuture.allOf(future1, future2).join();

        if (future1.isCompletedExceptionally()) {
            // 处理任务1的异常情况
            return Optional.empty();
        } else {
            // 获取任务2的结果
            String result2 = future2.join();
            return Optional.of(result2);
        }
    }

}
