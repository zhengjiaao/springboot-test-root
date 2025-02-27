package com.zja.jdk.math;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: zhengja
 * @Date: 2025-02-26 14:58
 */
public class BigDecimalTest {

    // 将平方米转换为公顷，保留小数点后四位并逢五进一
    @Test
    public void test() {
        // String str = "1862.725771"; // 假设这是平方米的值
        String str = "132834.582489"; // 假设这是平方米的值
        BigDecimal number = new BigDecimal(str);

        // 将平方米转换为公顷
        BigDecimal hectares = number.divide(new BigDecimal("10000"), 8, RoundingMode.HALF_UP);

        // 保留小数点后四位并逢五进一
        hectares = hectares.setScale(4, RoundingMode.HALF_UP);

        System.out.println(hectares.toString());
        // 输出结果为：0.1863
        // 输出结果为：13.2835
    }
}
