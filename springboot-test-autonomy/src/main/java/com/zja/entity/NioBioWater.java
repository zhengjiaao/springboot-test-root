package com.zja.entity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:48
 */
public class NioBioWater extends AbstractBoilWater {
    @Override
    public void make() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        a_addWater();
        b_on();
        //Future只能用线程池去调用执行
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                c_boiling();
                return 1;
            }
        });
        executor.shutdown();
        while(!future.isDone()){
            playGame();//烧水过程中，玩一局游戏
        }
        int a = future.get(); //同步阻塞等待,等待Callable事件完成
        d_off();
    }
}
