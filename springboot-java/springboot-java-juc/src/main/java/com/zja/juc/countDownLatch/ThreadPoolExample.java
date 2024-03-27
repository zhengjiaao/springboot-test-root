package com.zja.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理
 * <p>
 * 线程池管理：在使用线程池执行一组任务时，可以使用 CountDownLatch 来等待所有任务完成。主线程可以调用 await() 方法等待所有任务的完成，而每个任务在执行完成时调用 countDown() 方法递减计数。
 *
 * @author: zhengja
 * @since: 2024/03/19 11:20
 */
public class ThreadPoolExample {

    public static void main(String[] args) throws InterruptedException {
        int numTasks = 5;
        CountDownLatch latch = new CountDownLatch(numTasks);
        ExecutorService executor = Executors.newFixedThreadPool(numTasks);

        for (int i = 0; i < numTasks; i++) {
            Runnable task = new Task(latch, "Task " + (i + 1));
            executor.execute(task);
        }

        // 关闭线程池
        executor.shutdown();

        // 等待所有任务完成
        latch.await();

        System.out.println("所有任务已完成。继续执行主线程。");
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
            try {
                Thread.sleep(2000);
                System.out.println(taskName + " 完成任务。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }
}
