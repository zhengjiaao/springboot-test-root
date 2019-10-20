package com.dist.biolWater;

import com.dist.entity.BioBioWater;
import com.dist.entity.NioBioWater;
import org.junit.Test;

/**同步执行，同步回调
 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:45
 */
public class NioBioWaterTest {
    /**
     a.加入适量的水
     b.打开煤气
     c.暗中观察,等待水烧开 —同步阻塞等待
     d.关闭煤气
     *
     * 由代码可见在烧水的过程中，一直在同步阻塞等待，烧水的这一段时间就白白浪费了。
     * @throws Exception
     */
    @Test
    public void BioBioWater() throws Exception {
        BioBioWater bioBioWater =new BioBioWater();
        bioBioWater.make();
    }

    /**方法二：Future,Callable-异步执行，同步回调
     *
     - a.加入适量的水
     - b.打开煤气 — 老鸟有一定的经验,目测20分钟之后可以烧开，
     - c.playGame// 打一局小游戏
     - d.观察水是否烧开烧开
     - e.关闭煤气
     */
    @Test
    public void NioBioWater() throws Exception {
        NioBioWater nioBioWater =new NioBioWater();
        nioBioWater.make();

        /*
        启动一个新的线程去执行c_boiling(),执行过程中，无需同步的阻塞等待，
        可以去做一些其他一些有意思的事，打游戏playGame(); 最终使用future.get()，
        这是一个同步阻塞的操作，等待Callable事件完成
         */
    }
}
