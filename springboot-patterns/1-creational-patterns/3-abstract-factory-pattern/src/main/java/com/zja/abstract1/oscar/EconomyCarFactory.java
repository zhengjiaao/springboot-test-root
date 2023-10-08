/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:13
 * @Since:
 */
package com.zja.abstract1.oscar;

import com.zja.abstract1.oscar.economy.EconomyCar;
import com.zja.abstract1.oscar.economy.EconomyEngine;
import com.zja.abstract1.oscar.economy.EconomySeat;
import com.zja.abstract1.oscar.economy.EconomyTire;

/**
 * 实现经济型汽车工厂
 *
 * @author: zhengja
 * @since: 2023/10/08 10:13
 */
public class EconomyCarFactory implements CarFactory {
    @Override
    public Car createCar() {
        return new EconomyCar();
    }

    @Override
    public Engine createEngine() {
        return new EconomyEngine();
    }

    @Override
    public Tire createTire() {
        return new EconomyTire();
    }

    @Override
    public Seat createSeat() {
        return new EconomySeat();
    }
}
