/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:18
 * @Since:
 */
package com.zja.bridge.draw;

/**
 * 形状（Shape）的抽象类
 *
 * @author: zhengja
 * @since: 2023/10/08 16:18
 */
public abstract class Shape {
    protected Color color;

    public Shape(Color color) {
        this.color = color;
    }

    public abstract void draw();
}
