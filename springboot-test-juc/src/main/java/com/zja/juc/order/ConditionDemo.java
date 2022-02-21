/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 13:42
 * @Since:
 */
package com.zja.juc.order;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition（条件变量）: 通常与一个锁关联。需要在多个Contidion中共享一个锁时，可以传递一个Lock/RLock实例给构造方法，否则它将自己生成一个RLock实例。
 *
 * Condition中await() 方法类似于Object类中的wait()方法。
 * Condition中await(long time,TimeUnit unit) 方法类似于Object类中的wait(long time)方法。
 * Condition中signal() 方法类似于Object类中的notify()方法。
 * Condition中signalAll() 方法类似于Object类中的notifyAll()方法。
 */
public class ConditionDemo {
    private static Lock lock = new ReentrantLock();
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();

    static class MyThread implements Runnable {

        String name;
        Condition startCondition;
        Condition endCondition;

        public MyThread(String name, Condition startCondition, Condition endCondition) {
            this.name = name;
            this.startCondition = startCondition;
            this.endCondition = endCondition;
        }

        @Override
        public void run() {
            //阻塞
            if (startCondition != null) {
                lock.lock();
                try {
                    startCondition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            //继续往下执行
            System.out.println(name + "开始执行");
            //todo 执行业务逻辑
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //唤醒
            if (endCondition != null) {
                lock.lock();
                try {
                    endCondition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 写法与wait、notify写法类似
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(new MyThread("第一个线程", null, condition1));
        Thread t2 = new Thread(new MyThread("第二个线程", condition1, condition2));
        Thread t3 = new Thread(new MyThread("第三个线程", condition2, null));
        //打乱顺序执行
        t3.start();
        t2.start();
        t1.start();
    }

}
