package com.zja.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * 测试并发性能
 * <p>
 * 测试并发性能：在进行并发性能测试时，可以使用 CountDownLatch 来控制并发线程的启动和等待。测试线程可以等待所有并发线程准备就绪，然后一起开始执行任务。
 *
 * @author: zhengja
 * @since: 2024/03/19 11:22
 */
public class ConcurrentPerformanceTest {

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 5;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch endSignal = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            Thread workerThread = new Thread(new Worker(startSignal, endSignal, "Worker " + (i + 1)));
            workerThread.start();
        }

        // 等待所有工作线程准备就绪
        Thread.sleep(2000);
        System.out.println("所有工作线程已准备就绪。开始执行任务。");
        startSignal.countDown();

        // 等待所有工作线程完成任务
        endSignal.await();

        System.out.println("所有工作线程已完成任务。继续执行主线程。");
    }

    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch endSignal;
        private final String workerName;

        public Worker(CountDownLatch startSignal, CountDownLatch endSignal, String workerName) {
            this.startSignal = startSignal;
            this.endSignal = endSignal;
            this.workerName = workerName;
        }

        @Override
        public void run() {
            try {
                startSignal.await(); // 等待开始信号

                // 执行任务
                System.out.println(workerName + " 执行任务。");
                Thread.sleep(2000);

                endSignal.countDown(); // 任务完成
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
