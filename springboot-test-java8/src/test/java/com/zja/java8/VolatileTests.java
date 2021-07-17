package com.zja.java8;

import com.zja.BaseTests;
import lombok.Data;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-10 15:26
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：未写完
 */
public class VolatileTests extends BaseTests {

    /**
     * 内存可见性问题：当多个线程操作共享数据时，彼此不可见
     *
     * ThreadDemo线程将flag改为true,主线程读取到的应该也是true，循环应该会结束.可以看到，该程序并没有结束，也就是死循环
     * 说明主线程读取到的flag还是false
     *
     * @param args
     * 参考: https://www.jianshu.com/p/1f19835e05c0
     */
    public static void main(String[] args) { //这个线程是用来读取flag的值的
        ThreadDemo threadDemo = new ThreadDemo();
        Thread thread = new Thread(threadDemo);
        thread.start();
        while (true) {
            //解决多线程内存数据不共享问题,可以加锁
            //加锁,让while循环每次都从主存中去读取数据，这样就能读取到true
            //synchronized (threadDemo) {
                if (threadDemo.isFlag()) {
                    System.out.println("主线程读取到的flag = " + threadDemo.isFlag());
                    break;
                }
            //}
        }
    }
}

@Data
class ThreadDemo implements Runnable { //这个线程是用来修改flag的值的
    public boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("ThreadDemo线程修改后的flag = " + isFlag());
    }
}
