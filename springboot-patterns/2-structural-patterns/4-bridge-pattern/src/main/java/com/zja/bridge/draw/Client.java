/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:22
 * @Since:
 */
package com.zja.bridge.draw;

/**
 * @author: zhengja
 * @since: 2023/10/08 16:22
 */
public class Client {
    public static void main(String[] args) {
        //在应用程序中动态地创建不同的形状和颜色，并将它们组合起来

        Shape redCircle = new Circle(new Red());
        Shape blueSquare = new Square(new Blue());

        redCircle.draw();
        blueSquare.draw();

        //输出结果：
        //Draw a circle. Fill with red color.
        //Draw a square. Fill with blue color.
    }
}
