package com.zja.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * 并行任务的等待
 * <p>
 * 并行任务的等待：当一个任务需要依赖于多个并行执行的子任务时，可以使用 CountDownLatch 来等待所有子任务完成。主任务可以调用 await() 方法等待子任务的完成，而子任务在完成时调用 countDown() 方法递减计数。
 *
 * @author: zhengja
 * @since: 2024/03/19 11:17
 */
public class ParallelTaskExample {

    public static void main(String[] args) throws InterruptedException {
        int numTasks = 5;
        CountDownLatch latch = new CountDownLatch(numTasks);

        for (int i = 0; i < numTasks; i++) {
            Thread taskThread = new Thread(new Task(latch, "Task " + (i + 1)));
            taskThread.start();
        }

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
