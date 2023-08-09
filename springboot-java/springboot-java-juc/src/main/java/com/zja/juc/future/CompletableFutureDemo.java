/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 14:51
 * @Since:
 */
package com.zja.juc.future;

import com.alibaba.fastjson.JSON;
import com.zja.juc.future.auxiliary.OrgService;
import com.zja.juc.future.auxiliary.RoleService;
import com.zja.juc.future.auxiliary.UserService;
import com.zja.juc.future.entity.Org;
import com.zja.juc.future.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * CompletableFuture 多线程-异步编排
 * 知识点：CompletableFuture 支持自定义线程池,默认是 ForkJoinPool线程池。
 * 四个静态方法：
 * CompletableFuture.runAsync(Runnable runnable)    				   不支持返回值
 * CompletableFuture.runAsync(Runnable runnable, Executor executor)    不支持返回值
 * CompletableFuture.supplyAsync(Supplier<U> supplier)     				   支持返回值
 * CompletableFuture.supplyAsync(Supplier<U> supplier, Executor executor)  支持返回值
 *
 * @author zhengjiaao
 */
@Slf4j
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //简单示例，创建异步对象，使用默认ForkJoinPool线程池。
//        completableFutureTest01();

        //实战应用
        Map<String, Object> userInfo = getUserInfo();
        System.out.println(JSON.toJSONString(userInfo, true));
    }

    /**
     * 创建异步对象，使用默认ForkJoinPool线程池。
     * 知识点：
     * get() 和 join() 作用是一样的，阻塞，一直到执行完成 或 出现异常。
     * get()  需要手动抛异常或捕获异常，支持设置超时。
     * join() 不需要手动抛异常。
     */
    public static void completableFutureTest01() throws ExecutionException, InterruptedException, TimeoutException {

        //1.runAsync() 无法返回值
        CompletableFuture<Void> future01 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "  测试1");
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "  测试2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //2.supplyAsync() 有返回值
        CompletableFuture<Integer> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "  测试3");
            //返回结果 result
            return 333;
        });

        //阻塞，一直到执行完成 或 出现异常
        Integer result = future02.get();
        System.out.println(result);

        //todo 执行其他业务

        //注：此处为了测试，业务系统运行时，可以移除
        future01.get();

        //知识点：get() 和 join() 作用是一样的，阻塞，一直到执行完成 或 出现异常
        // get()  需要手动抛异常或捕获异常，支持设置超时
        // join() 不需要手动抛异常
//        future02.get();
//        future02.get(2, TimeUnit.SECONDS);
//        future02.join();


       /* CompletableFuture<Void> future03 = CompletableFuture.supplyAsync(() -> "Hello World").thenAccept(result -> {
            System.out.println(result);
            System.out.println(Thread.currentThread().getName() + "333");
        });*/

    }


    private static Map<String, Object> getUserInfo() throws ExecutionException, InterruptedException {
        //模拟三个服务，查询接口 get* 耗时 1s， get*List 耗时 2s
        UserService userService = new UserService();
        OrgService orgService = new OrgService();
        RoleService roleService = new RoleService();

        long startTime = System.currentTimeMillis();

        //耗时 1s
        CompletableFuture<User> getUser = CompletableFuture.supplyAsync(userService::getUser);
        //耗时 2s
        CompletableFuture<List<User>> getUserList = CompletableFuture.supplyAsync(userService::getUserList);
        //耗时 1s
        CompletableFuture<Org> getOrg = CompletableFuture.supplyAsync(() -> orgService.getOrg("001"));

        //合并异步等待
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(getUser, getUserList, getOrg);
        //等待所有异步方法执行完成
        allFutures.join();

        long endTime = System.currentTimeMillis();

        //耗时结果：2053毫秒
        System.out.println(endTime - startTime + "毫秒");

        Map<String, Object> map = new HashMap<>();
        map.put("getUser", getUser.get());
        map.put("getUserList", getUserList.get());
        map.put("getOrg", getOrg.get());
        return map;
    }


    /**
     * thenApply：串行化执行，thenApply方法执行需要依赖supplyAsync的结果时可以使用thenApply
     * 当supplyAsync发生异常时，会直接执行exceptionally或直接抛出异常，不会继续执行thenApply
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void thenApplyTest() throws ExecutionException, InterruptedException {
        log.info("test......start");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply async.......");
            int i = 10 / 0;
            return 10;
        }).thenApply(new Function<Integer, String>() {
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
}
