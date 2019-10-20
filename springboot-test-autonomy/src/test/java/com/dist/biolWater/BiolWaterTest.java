package com.dist.biolWater;

import com.dist.entity.AioBoiIWater;
import com.dist.entity.CompletableFutureBiolWater;
import org.junit.Test;

/** ----异步执行，异步回调----
    a.加入适量的水
    b.打开煤气
    c.playGame // 打一局小游戏
    d.主动提醒，水已烧开 —- //主动通知-回调
    e.关闭煤气
 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:10
 */
public class BiolWaterTest {

    /**
     *FutureTask-异步执行，异步回调
     *
     * FutureTask实现了Future和Runnable接口， 它可以不适用线程池，直接通过线程启动。
     它最大的特点是，它有一个done()回调函数,当前任务状态为isDone时，触发
     * @throws Exception
     */
    @Test
    public void TestBiolWater3() throws Exception {
        AioBoiIWater aioBoiIWater = new AioBoiIWater();
        aioBoiIWater.make();
    }


    /**
     * 方法四：jdk8新特性 CompletableFuture-异步执行，异步回调
     * @throws Exception
     */
    @Test
    public void TestBiolWater4() throws Exception {
        CompletableFutureBiolWater completableFutureBiolWater = new CompletableFutureBiolWater();
        completableFutureBiolWater.make();
    }
}
