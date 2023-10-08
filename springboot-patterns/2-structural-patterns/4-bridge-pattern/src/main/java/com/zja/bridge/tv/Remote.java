/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:28
 * @Since:
 */
package com.zja.bridge.tv;

/**
 * 遥控器（Remote）的抽象类，其中包含基本的控制方法，以及一个维护电视对象的引用
 *
 * @author: zhengja
 * @since: 2023/10/08 16:28
 */
public abstract class Remote {
    protected TV tv;

    public Remote(TV tv) {
        this.tv = tv;
    }

    public abstract void power();

    public abstract void volumeUp(); //音量增大

    public abstract void volumeDown(); //音量减少

    public abstract void nextChannel(); //下一个频道

    public abstract void previousChannel(); //上一个频道
}
