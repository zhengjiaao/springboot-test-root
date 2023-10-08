/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:21
 * @Since:
 */
package com.zja.bridge.draw;

/**
 * 颜色类-蓝色（Blue）
 *
 * @author: zhengja
 * @since: 2023/10/08 16:21
 */
public class Blue implements Color {
    @Override
    public void fill() {
        System.out.println("Fill with blue color.");
    }
}