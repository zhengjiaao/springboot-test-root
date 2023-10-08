/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:07
 * @Since:
 */
package com.zja.abstract1.oscar.economy;

import com.zja.abstract1.oscar.Car;

/**
 * 定义经济型汽车及其组件的具体类
 *
 * @author: zhengja
 * @since: 2023/10/08 10:07
 */
//汽车
public class EconomyCar implements Car {
    @Override
    public void assemble() {
        System.out.println("组装经济型汽车");
    }
}
