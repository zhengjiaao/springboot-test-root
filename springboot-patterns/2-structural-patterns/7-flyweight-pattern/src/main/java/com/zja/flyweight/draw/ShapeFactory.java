/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:13
 * @Since:
 */
package com.zja.flyweight.draw;

import java.util.HashMap;
import java.util.Map;

/**
 * ShapeFactory 是享元工厂类，负责创建和管理圆形对象，并通过一个 shapeCache 哈希映射来维护已经创建的圆形对象。
 * 通过享元模式，当需要绘制相同颜色的圆形时，只需共享已经创建的对象，避免了重复创建相同的对象，从而减少了内存消耗。
 * @author: zhengja
 * @since: 2023/10/09 10:13
 */
// FlyweightFactory（享元工厂）
class ShapeFactory {
    private static final Map<String, Shape> shapeCache = new HashMap<>();

    public static Shape getCircle(String color) {
        Circle circle = (Circle) shapeCache.get(color);

        if (circle == null) {
            circle = new Circle(color);
            shapeCache.put(color, circle);
            System.out.println("Creating a new circle with color " + color);
        }

        return circle;
    }
}
