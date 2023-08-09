/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-09 14:03
 * @Since:
 */
package com.zja.juc.future;

import com.zja.juc.future.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * @author: zhengja
 * @since: 2023/08/09 14:03
 */
@Slf4j
public class CompletableFutureTests {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);


    /**
     * runAsync无返回值
     * future.get()会阻塞主线程接着向下执行
     */
    @Test
    public void runAsyncTest() throws ExecutionException, InterruptedException {
        log.info("test......start");

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("run async.......");
        }, executorService);

        future.get();

        log.info("test......end");
    }


    /**
     * applyAsync有返回值
     * future.get()会阻塞祝线程接着向下执行，且可以获取返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void applyAsyncTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            log.info("apply async.......");
            return new User("123", "李四");
        }, executorService);

        User user = future.get();
        log.info("test------->result:{}", user);

        log.info("test.......end");
    }


    /**
     * whenComplete：在异步 正常/非正常执行完后，都会接着执行 whenComplete
     * whenComplete：用以继续处理异步正常执行的结果，或异常情况
     * （示例中：r为异步执行的结果，t为异常）
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenCompleteTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            int a = 10 / 0;
            return new User("123", "李四");
        }, executorService).whenComplete((r, t) -> {
            log.info("whenComplete.......");
            r.setUserName("李四名字更新成功");
            return;
        });

        User user = future.get();
        log.info("test------>result:{}", user);

        log.info("test.......end");
    }


    /**
     * exceptionally: 在异步执行发生异常时，会接着执行exceptionally
     * exceptionally：用以继续处理异常的情况
     * （示例中：t为异常）
     */
    @Test
    public void exceptionallyTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            int i = 10 / 0;
            return new User("123", "李四");
        }, executorService).exceptionally(t -> {
            log.info("exceptionally.......e:{}", t);
            return null;
        });

        User user = future.get();
        log.info("test.......result:{}", user);

        log.info("test.......end");
    }


    /**
     * thenApply：串行化执行，thenApply方法执行需要依赖supplyAsync的结果时可以使用thenApply
     * 当supplyAsync发生异常时，会直接执行exceptionally或直接抛出异常，不会继续执行thenApply
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenApplyTest() throws ExecutionException, InterruptedException {
        log.info("test......start");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            int i = 10 / 0;
            return 10;
        }, executorService).thenApply(new Function<Integer, String>() {
            //当supplyAsync发生异常时，不会继续执行thenApply
            @Override
            public String apply(Integer params) {
                log.info("then apply.......");
                return params + " append 5";
            }
        }).exceptionally(t -> {
            log.info("exception deal");
            return null;
        });

        String res = future.get();
        log.info("test.......result:{}", res);

        log.info("test......end");
    }


    /**
     * handle 是执行任务完成时对结果的处理。
     * handle 方法和 thenApply 方法处理方式基本一样。
     * <p>
     * 不同的是 handle 是在任务完成后再执行，还可以处理异常的任务。
     * thenApply 只可以执行正常的任务，任务出现异常则不执行 thenApply 方法。
     * <p>
     * whenComplete接收的是BiConsumer,handler接收的是BiFunction;
     * BiConsumer是直接消费的,而BiFunction是有返回值的,
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void handleTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            int i = 10 / 0;
            return 10;
        }, executorService).handle(new BiFunction<Integer, Throwable, String>() {
            //当supplyAsync发生异常时，会继续执行handle
            @Override
            public String apply(Integer params, Throwable throwable) {
                //null  append 5
                return params + "  append 5";
            }
        }).exceptionally(t -> {
            log.info("exceptionally deal");
            return null;
        });

        String res = future.get();
        log.info("test.......res:{}", res);

        log.info("test.......end");
    }


    /**
     * 任务正常执行完后，消费上一步的处理结果，无返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenAcceptTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            //int i = 10 / 0;
            return 10;
        }, executorService).thenAccept(result -> {
            log.info("then accept.......1 " + result);
            if (result == 10) {
                return;
            }
            log.info("then accept.......2 " + result);
        }).exceptionally(t -> {
            log.info("exceptionally.......");
            return null;
        });

        future.get();

        log.info("test.......end");
    }


    /**
     * 任务正常执行完后，不关心且无法处理上一步的处理结果，
     * 只要上一步正常执行完后就开始执行thenRun方法，无返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenRunTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            //int i = 10 / 0;
            return 10;
        }, executorService).thenRun(() -> {

            log.info("then run.......");

        }).exceptionally(t -> {
            log.info("exceptionally.......");
            return null;
        });

        future.get();

        log.info("test.......end");
    }


    /**
     * 处理两个completableStage的处理结果,并且返回（有返回值）
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenCombineTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 2 , supply async.......");
            int i = 10 / 0;
            return "future2=====>20 ";
        }, executorService).exceptionally(t -> {
            log.info("future2  exceptionally");
            return "exceptionally====>20";
        });

        CompletableFuture<Object> result = future1.thenCombine(future2, new BiFunction<String, String, Object>() {
            @Override
            public Object apply(String result1, String result2) {
                log.info("");
                return result1 + result2;
            }
        });
        log.info("test.......time test");
        String res = (String) result.get();
        log.info("test.......result:{}", res);

        log.info("test.......end");
    }


    /**
     * 处理两个completableStage的处理结果（无返回值）
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenAcceptBothTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 2 , supply async.......");
            int i = 10 / 0;
            return "future2=====>20 ";
        }, executorService).exceptionally(t -> {
            log.info("future2  exceptionally");
            return "exceptionally====>20";
        });

        CompletableFuture<Void> results = future1.thenAcceptBoth(future2, new BiConsumer<String, String>() {
            @Override
            public void accept(String result1, String reuslt2) {
                log.info(result1 + reuslt2);
            }
        });

        results.get();

        log.info("test.......end");
    }


    /**
     * 两个CompletableStage,谁处理快就用谁的结果进行下一步操作，有返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void applyToEitherTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 2 , supply async.......");
            int i = 10 / 0;
            return "future2=====>20 ";
        }, executorService).exceptionally(t -> {
            log.info("future2  exceptionally");
            return "exceptionally====>20";
        });

        CompletableFuture<Object> results = future1.applyToEither(future2, new Function<String, Object>() {
            @Override
            public Object apply(String firstResult) {
                return "谁快就用谁的result:" + firstResult;
            }
        });

        String res = (String) results.get();
        log.info("test.......res:{}", res);

        log.info("test.......end");
    }


    /**
     * 两个CompletableStage,谁处理快就用谁的结果进行下一步处理，无返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void acceptEitherTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 2 , supply async.......");
            int i = 10 / 0;
            return "future2=====>20 ";
        }, executorService).exceptionally(t -> {
            log.info("future2  exceptionally");
            return "exceptionally====>20";
        });

        CompletableFuture<Void> results = future1.acceptEither(future2, new Consumer<String>() {
            @Override
            public void accept(String firstResult) {
                log.info("谁快就用谁的result:" + firstResult);
            }
        });

        results.get();

        log.info("test.......end");
    }


    /**
     * 两个CompletableStage,任何一个执行完了就可以执行runAferEither里的run方法，
     * 无法使用上面的处理结果，且无返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void runAfterEitherTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 2 , supply async.......");
            int i = 10 / 0;
            return "future2=====>20 ";
        }, executorService).exceptionally(t -> {
            log.info("future2  exceptionally");
            return "exceptionally====>20";
        });

        CompletableFuture<Void> result = future1.runAfterEither(future2, new Runnable() {
            @Override
            public void run() {
                log.info("上面任何一个future，执行完就可以执行这里的操作");
            }
        });

        result.get();

        log.info("test.......end");
    }


    /**
     * 两个complatableStage,都执行完成了，才能执行runAfterBoth,无法使用上面的处理结果，且无返回值
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void runAfterBothTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 2 , supply async.......");
            int i = 10 / 0;
            return "future2=====>20 ";
        }, executorService).exceptionally(t -> {
            log.info("future2  exceptionally");
            return "exceptionally====>20";
        });

        CompletableFuture<Void> result = future1.runAfterBoth(future2, new Runnable() {
            @Override
            public void run() {
                log.info("上面两个都处理完成了，才能执行这里");
            }
        });

        result.get();

        log.info("test.......end");
    }


    /**
     * thenCompose 方法允许你对两个 CompletionStage 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenComposeTest() throws ExecutionException, InterruptedException {
        log.info("test.......start");

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("future 1 , supply async.......");
            return "future1====>10 ";
        }, executorService).thenCompose(new Function<String, CompletionStage<String>>() {
            @Override
            public CompletionStage<String> apply(String param) {
                return CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "deal with res1:" + param + ", return res2";
                    }
                });
            }
        });

        String res = future1.get();
        log.info("test.......result:{}", res);

        log.info("test.......end");

    }
}
