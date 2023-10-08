/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:09
 * @Since:
 */
package com.zja.abstract1.oscar.luxury;

import com.zja.abstract1.oscar.Engine;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:09
 */
public class LuxuryEngine implements Engine {
    @Override
    public void design() {
        System.out.println("设计豪华型引擎");
    }
}
