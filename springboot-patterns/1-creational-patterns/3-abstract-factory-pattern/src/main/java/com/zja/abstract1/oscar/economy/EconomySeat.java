/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:07
 * @Since:
 */
package com.zja.abstract1.oscar.economy;

import com.zja.abstract1.oscar.Seat;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:07
 */
public class EconomySeat implements Seat {
    @Override
    public void install() {
        System.out.println("安装经济型座椅");
    }
}
