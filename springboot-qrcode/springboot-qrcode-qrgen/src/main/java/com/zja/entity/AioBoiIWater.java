package com.zja.entity;

import java.util.concurrent.FutureTask;

/**FutureTask-异步执行，异步回调
 *
 a.加入适量的水
 b.打开煤气
 c.playGame // 打一局小游戏
 d.主动提醒，水已烧开 —- //主动通知-回调
 e.关闭煤气

 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:23
 */
public class AioBoiIWater extends AbstractBoilWater {
    @Override
    public void make() throws Exception {
        a_addWater(); //1.加水
        b_on(); //2.打开煤气
        FutureTask<Integer> futureTask = new FutureTask(() -> {
            c_boiling(); //3-1.烧水中 等待50000  3-2水烧好 true
            return 1; //返回1 是完成
        }){
            //当futureTask执行完之后,调用done()
            @Override
            protected void done() {
                d_off(); //4.关闭煤气
            }
        };

        new Thread(futureTask).start();
        while(!futureTask.isDone()){
            playGame(); //3-3.水还没烧开,玩一把游戏  等待1200
        }
      //futureTask.get();//阻塞，
        Thread.sleep(5000);
    }
}

