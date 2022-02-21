/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:45
 * @Since:
 */
package com.zja.juc.order;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    static class MyThread implements Runnable {

        CyclicBarrier startCyclicBarrier;

        CyclicBarrier endCyclicBarrier;

        public MyThread(CyclicBarrier startCyclicBarrier, CyclicBarrier endCyclicBarrier) {
            this.startCyclicBarrier = startCyclicBarrier;
            this.endCyclicBarrier = endCyclicBarrier;
        }

        @Override
        public void run() {
            //阻塞
            if (startCyclicBarrier != null) {
                try {
                    startCyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //执行自己的业务逻辑
            System.out.println(Thread.currentThread().getName() + "开始执行");
            //todo 执行业务逻辑
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (endCyclicBarrier != null) {
                try {
                    endCyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * CyclicBarrier就是栅栏，它只有一个方法await()，相当于-1，然后阻塞，当减到0的时候，然后一起往下执行，相当于万箭齐发。
     */
    public static void main(String[] args) {
        CyclicBarrier barrier1 = new CyclicBarrier(2);
        CyclicBarrier barrier2 = new CyclicBarrier(2);
        Thread t1 = new Thread(new MyThread(null, barrier1), "线程1");
        Thread t2 = new Thread(new MyThread(barrier1, barrier2), "线程2");
        Thread t3 = new Thread(new MyThread(barrier2, null), "线程3");
        //打乱顺序执行
        t3.start();
        t2.start();
        t1.start();
    }

}
