/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:12
 * @Since:
 */
package com.zja.abstract1.oscar;

/**
 * 汽车工厂的抽象类或接口
 *
 * @author: zhengja
 * @since: 2023/10/08 10:12
 */
public interface CarFactory {
    Car createCar();

    Engine createEngine();

    Tire createTire();

    Seat createSeat();
}