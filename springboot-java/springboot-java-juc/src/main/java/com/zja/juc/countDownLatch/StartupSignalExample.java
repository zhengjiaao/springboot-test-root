package com.zja.juc.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * 启动信号
 * <p>
 * 启动信号：当某个系统或服务需要等待其他组件初始化完成后才能启动时，可以使用 CountDownLatch。主组件可以等待其他组件初始化完成，而其他组件在初始化完成后调用 countDown() 方法通知主组件。
 *
 * @author: zhengja
 * @since: 2024/03/19 11:19
 */
public class StartupSignalExample {

    public static void main(String[] args) throws InterruptedException {
        int numComponents = 3;
        CountDownLatch latch = new CountDownLatch(numComponents);

        // 启动其他组件
        for (int i = 0; i < numComponents; i++) {
            Thread componentThread = new Thread(new Component(latch, "Component " + (i + 1)));
            componentThread.start();
        }

        // 等待所有组件初始化完成
        latch.await();

        System.out.println("所有组件已初始化完成。启动主组件。");
    }

    static class Component implements Runnable {
        private final CountDownLatch latch;
        private final String componentName;

        public Component(CountDownLatch latch, String componentName) {
            this.latch = latch;
            this.componentName = componentName;
        }

        @Override
        public void run() {
            // 模拟组件初始化
            try {
                Thread.sleep(2000);
                System.out.println(componentName + " 初始化完成。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }
}
