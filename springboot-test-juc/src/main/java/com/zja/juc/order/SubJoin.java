/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:20
 * @Since:
 */
package com.zja.juc.order;

public class SubJoin {
    static class MyThread implements Runnable {

        Thread thread;

        String name;

        public MyThread(Thread thread, String name) {
            this.thread = thread;
            this.name = name;
        }


        @Override
        public void run() {
            try {
                //先让其他线程插队执行
                if (thread != null) {
                    thread.join();
                }
                System.out.println(name + "开始执行");
                //todo 结束以后再执行自己的逻辑
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 在写run方法的时候先让其他的线程join插队，等其他线程执行完以后再执行自己的逻辑。
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(new MyThread(null, "第一个线程"));
        Thread t2 = new Thread(new MyThread(t1, "第二个线程"));
        Thread t3 = new Thread(new MyThread(t2, "第三个线程"));
        //打乱顺序执行
        t3.start();
        t1.start();
        t2.start();
    }
}
