package com.zja.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务等待
 * <p>
 * 定时任务等待：当需要等待一组定时任务全部执行完成后再执行后续操作时，可以使用 CountDownLatch。每个定时任务在执行完成时调用 countDown() 方法递减计数，而主线程可以在 await() 方法中等待所有定时任务的完成。
 *
 * @author: zhengja
 * @since: 2024/03/19 11:23
 */
public class ScheduledTaskExample {

    public static void main(String[] args) throws InterruptedException {
        int numTasks = 3;
        CountDownLatch latch = new CountDownLatch(numTasks);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(numTasks);

        for (int i = 0; i < numTasks; i++) {
            Runnable task = new Task(latch, "Task " + (i + 1));
            executor.schedule(task, (i + 1) * 2, TimeUnit.SECONDS);
        }

        // 等待所有任务完成
        latch.await();

        System.out.println("所有定时任务已完成。继续执行主线程。");

        // 关闭线程池
        executor.shutdown();
    }

    static class Task implements Runnable {
        private final CountDownLatch latch;
        private final String taskName;

        public Task(CountDownLatch latch, String taskName) {
            this.latch = latch;
            this.taskName = taskName;
        }

        @Override
        public void run() {
            // 模拟任务执行
            System.out.println(taskName + " 执行任务。");
            latch.countDown();
        }
    }
}
