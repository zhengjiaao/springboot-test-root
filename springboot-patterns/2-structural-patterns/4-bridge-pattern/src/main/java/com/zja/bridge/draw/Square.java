/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:19
 * @Since:
 */
package com.zja.bridge.draw;

/**
 * 形状类-正方形（Square）
 *
 * @author: zhengja
 * @since: 2023/10/08 16:19
 */
public class Square extends Shape {
    public Square(Color color) {
        super(color);
    }

    @Override
    public void draw() {
        System.out.print("Draw a square. ");
        color.fill();
    }
}
