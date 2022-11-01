/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:20
 * @Since:
 */
package com.zja.juc.order;

/**
 * 多线程执行顺序：Join
 * 参考：https://juejin.cn/post/7053828761359220773
 */
public class MainJoin {
    static class MyThread implements Runnable {

        String name;

        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + "开始执行");
            try {
                //todo 业务逻辑
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + "执行完毕");
        }
    }

    /**
     * 在main方法中，先是调用了t1.start方法，启动t1线程，随后调用t1的join方法，main所在的主线程就需要等待t1子线程中的run方法运行完成后才能继续运行，所以主线程卡在t2.start方法之前等待t1程序。
     * 等t1运行完后，主线程重新获得主动权，继续运行t2.start和t2.join方法，与t1子线程类似，main主线程等待t2完成后继续执行，如此执行下去，join方法就有效的解决了执行顺序问题。
     * 因为在同一个时间点，各个线程是同步状态。
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MyThread("第一个线程"));
        Thread t2 = new Thread(new MyThread("第二个线程"));
        Thread t3 = new Thread(new MyThread("第三个线程"));
        t1.start();
        t1.join();

        t2.start();
        t2.join();
        t3.start();
    }
}
