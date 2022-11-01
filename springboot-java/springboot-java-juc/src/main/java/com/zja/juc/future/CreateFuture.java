/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 14:42
 * @Since:
 */
package com.zja.juc.future;


import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CreateFuture {

    @SneakyThrows
    public static void main(String[] args) {
        //方式一
        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<Integer> future = service.submit(new CallableTask());
        //阻塞获得结果
        Integer rs = future.get();
        System.out.println(rs);


        //方式二
        FutureTask<Integer> integerFutureTask = new FutureTask<>(new CallableTask());
        //启动线程
        new Thread(integerFutureTask).start();
        //阻塞获得结果
        Integer rs2 = integerFutureTask.get();
        System.out.println(rs2);
    }

}
