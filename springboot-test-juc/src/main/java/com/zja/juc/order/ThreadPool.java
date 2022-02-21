/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:25
 * @Since:
 */
package com.zja.juc.order;

import java.util.concurrent.*;

/**
 * 多线程执行顺序：线程池写法
 */
public class ThreadPool {
    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L
            , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
            , Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    static class MyThread implements Runnable {

        String name;

        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + "开始执行");
            try {
                //todo 执行业务逻辑
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + "执行完毕");
        }
    }

    /**
     * 顺序执行是巧妙的运用了1个核心线程数，1个最大线程数，保证串行执行所有任务。
     */
    public static void main(String[] args) {
        executorService.submit(new MyThread("第一个线程"));
        executorService.submit(new MyThread("第二个线程"));
        executorService.submit(new MyThread("第三个线程"));
        //线程池shutdown方法和shutdownNow有区别：
        //shutdown要等所有线程执行完后再关闭
        //shutdownNow将线程池内正在执行的线程强制停掉
        executorService.shutdown();
    }

}
