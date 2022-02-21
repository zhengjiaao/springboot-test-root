/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:12
 * @Since:
 */
package com.zja.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture 获取结果方法
 */
public class CompletableFutureDemo2 {
    static class ClientThread implements Runnable {

        private CompletableFuture completableFuture;

        public ClientThread(CompletableFuture completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + ":" +
                        completableFuture.get()); //阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 创建两个线程t1和t2，线程里面通过completableFuture.get()方法阻塞，当我们调用cf.complete("Finish")方法的时候，相当于往里面赋值了，get()方法取到值了，才能继续往下走。
     */
    public static void main(String[] args) {
        CompletableFuture cf = new CompletableFuture();
        new Thread(new ClientThread(cf), "t1").start();
        new Thread(new ClientThread(cf), "t2").start();
        //执行某段逻辑
        cf.complete("Finish");
        //exception
        //cf.completeExceptionally(e);
    }
}
