package com.zja.thread.pools;

import com.zja.thread.pools.service.AsyncTaskExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: zhengja
 * @since: 2024/01/23 9:19
 */
@SpringBootTest
public class AsyncTaskExecutorTest {

    @Autowired
    AsyncTaskExecutor asyncTaskExecutor;

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            asyncTaskExecutor.add("lisi_" + i);
        }

        Thread.sleep(1000 * 5);
    }

    @Test
    public void test2() throws InterruptedException {
        for (int i = 0; i < 5; i++) { // 超过5个任务时，会抛出[TaskRejectedException]任务被拒绝异常
            asyncTaskExecutor.add2("lisi_" + i); // 超过MaxPoolSize=5，且QueueCapacity=0时，会抛出[TaskRejectedException]任务被拒绝异常。
        }

        Thread.sleep(1000 * 5);
    }

    @Test
    public void test3() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            asyncTaskExecutor.add3("lisi_" + i); // MaxPoolSize=Integer.MAX_VALUE，每个任务会创建一个新的线程
        }

        Thread.sleep(1000 * 5);
    }

    @Test
    public void test4() throws InterruptedException {
        for (int i = 0; i < 35; i++) { // 超过35个任务时，会抛出[TaskRejectedException]任务被拒绝异常
            asyncTaskExecutor.add4("lisi_" + i); // 当任务数量超过[MaxPoolSize+QueueCapacity]时，会抛出[TaskRejectedException]任务被拒绝异常,队列已满。
        }

        Thread.sleep(1000 * 10);
    }

    @Test
    public void test5() throws InterruptedException {
        for (int i = 0; i < 1; i++) { // 超过1个任务时，会抛出[TaskRejectedException]任务被拒绝异常
            asyncTaskExecutor.add5("lisi_" + i);
        }

        Thread.sleep(1000 * 5);
    }


    // 测试：被拒绝的任务
    @Test
    public void test6() throws InterruptedException {
        for (int i = 0; i < 2; i++) { // 超过1个任务时，会抛出[TaskRejectedException]任务被拒绝异常
            asyncTaskExecutor.add6("lisi_" + i);
        }

        Thread.sleep(1000 * 5);
    }

}
