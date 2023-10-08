/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:09
 * @Since:
 */
package com.zja.abstract1.oscar.luxury;

import com.zja.abstract1.oscar.Tire;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:09
 */
public class LuxuryTire implements Tire {
    @Override
    public void produce() {
        System.out.println("生产豪华型轮胎");
    }
}
