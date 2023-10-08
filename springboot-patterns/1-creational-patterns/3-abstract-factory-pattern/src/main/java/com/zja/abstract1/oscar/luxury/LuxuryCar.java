/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:09
 * @Since:
 */
package com.zja.abstract1.oscar.luxury;

import com.zja.abstract1.oscar.Car;

/**
 * 定义豪华型汽车及其组件的具体类
 *
 * @author: zhengja
 * @since: 2023/10/08 10:09
 */
public class LuxuryCar implements Car {
    @Override
    public void assemble() {
        System.out.println("组装豪华型汽车");
    }
}

