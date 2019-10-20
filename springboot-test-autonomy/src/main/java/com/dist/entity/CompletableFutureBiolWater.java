package com.dist.entity;

import java.util.concurrent.CompletableFuture;

/**方法四：jdk8新特性 CompletableFuture-异步执行，异步回调
 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:08
 */
public class CompletableFutureBiolWater extends AbstractBoilWater {
    @Override
    public void make() throws Exception {
        a_addWater();  //1.加水
        b_on(); //2.打开煤气
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                c_boiling();  //3.烧水中 等待50000  水烧好 true
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        });

        //注册事件“监听”
        future.whenComplete((v, e) -> {
            System.out.println("v= "+v);
            System.out.println("e= "+e);
            d_off();  //4.关闭煤气
        });

        while (!future.isDone()) {
            playGame(); //5.水还没烧开,玩一把游戏  等待1200
        }
    }
}
