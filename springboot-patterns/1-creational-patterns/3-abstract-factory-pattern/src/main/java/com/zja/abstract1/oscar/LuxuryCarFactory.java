/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:13
 * @Since:
 */
package com.zja.abstract1.oscar;

import com.zja.abstract1.oscar.luxury.LuxuryCar;
import com.zja.abstract1.oscar.luxury.LuxuryEngine;
import com.zja.abstract1.oscar.luxury.LuxurySeat;
import com.zja.abstract1.oscar.luxury.LuxuryTire;

/**
 * 实现豪华型汽车工厂
 *
 * @author: zhengja
 * @since: 2023/10/08 10:13
 */
public class LuxuryCarFactory implements CarFactory {
    @Override
    public Car createCar() {
        return new LuxuryCar();
    }

    @Override
    public Engine createEngine() {
        return new LuxuryEngine();
    }

    @Override
    public Tire createTire() {
        return new LuxuryTire();
    }

    @Override
    public Seat createSeat() {
        return new LuxurySeat();
    }
}
