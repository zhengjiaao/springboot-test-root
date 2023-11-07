/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 11:21
 * @Since:
 */
package com.zja.string;

import com.google.common.base.CharMatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: zhengja
 * @since: 2023/11/07 11:21
 */
public class CharMatcherUnitTest {

    @Test
    public void testCharMatcher() {
        String input = "Hello, 123 World!";

        // 匹配字母和数字
        CharMatcher letterOrDigitMatcher = CharMatcher.javaLetterOrDigit();
        String result = letterOrDigitMatcher.retainFrom(input);

        // 验证结果只包含字母和数字
        assertEquals("Hello123World", result);

        // 移除数字
        CharMatcher digitMatcher = CharMatcher.javaDigit();
        result = digitMatcher.removeFrom(input);

        // 验证结果移除了数字
        assertEquals("Hello,  World!", result);

        // 替换非字母和数字字符为空格
        CharMatcher nonLetterOrDigitMatcher = letterOrDigitMatcher.negate();
        result = nonLetterOrDigitMatcher.replaceFrom(input, ' ');

        // 验证结果替换非字母和数字字符为空格
        assertEquals("Hello   123 World ", result);

        // 修剪开头和结尾的空格
        result = CharMatcher.whitespace().trimFrom(input);

        // 验证结果修剪了开头和结尾的空格
        assertEquals("Hello, 123 World!", result);
    }
}
