/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:13
 * @Since:
 */
package com.zja.flyweight.draw;

/**
 * Circle 类是具体享元类，它实现了 Shape 接口并提供了绘制圆形的实现。
 *
 * @author: zhengja
 * @since: 2023/10/09 10:13
 */
// ConcreteFlyweight（具体享元）
class Circle implements Shape {
    private String color;

    public Circle(String color) {
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a circle with color " + color);
    }
}
