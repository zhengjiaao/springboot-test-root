package com.dist.entity;

/**方法一：同步等待
 *
 a.加入适量的水
 b.打开煤气
 c.暗中观察,等待水烧开 —同步阻塞等待
 d.关闭煤气
 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:42
 */
public class BioBioWater extends AbstractBoilWater{
    @Override
    public void make() throws Exception {
        a_addWater();
        b_on();
        c_boiling();//同步阻塞等待
        d_off();
    }
    //结论：由代码可见在烧水的过程中，一直在同步阻塞等待，烧水的这一段时间就白白浪费了
}
