/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:14
 * @Since:
 */
package com.zja.flyweight.draw;

/**
 * Client 是客户端代码，它使用享元工厂获取圆形对象，并在循环中绘制不同颜色的圆形。
 *
 * @author: zhengja
 * @since: 2023/10/09 10:14
 */
// Client（客户端）
public class Client {
    private static final String[] colors = {"Red", "Green", "Blue", "Yellow"};

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String color = colors[(int) (Math.random() * colors.length)];
            Shape circle = ShapeFactory.getCircle(color);
            circle.draw();
        }
    }
}