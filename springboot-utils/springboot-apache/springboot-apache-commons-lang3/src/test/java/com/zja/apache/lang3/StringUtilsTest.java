package com.zja.apache.lang3;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * StringUtils： 提供了丰富的字符串处理方法，包括字符串比较、拆分、连接、大小写转换、去除空格、判空、替换等。这些方法使字符串操作更加方便和可读。
 *
 * @Author: zhengja
 * @Date: 2024-10-16 9:36
 */
public class StringUtilsTest {

    @Test
    public void Empty_test1() {
        // String str = null;
        // String str = "";
        // String str = " ";
        String str = "null";

        System.out.println(StringUtils.isNotEmpty(str));
        System.out.println(StringUtils.isEmpty(str));
        System.out.println(StringUtils.isBlank(str));
        System.out.println(StringUtils.isNotBlank(str));
        System.out.println(StringUtils.isNoneBlank(str));
        System.out.println(StringUtils.isAnyBlank(str));
        System.out.println(StringUtils.isAnyEmpty(str));
        System.out.println(StringUtils.isWhitespace(str));
        System.out.println(StringUtils.isNoneEmpty(str));

        System.out.println(StringUtils.isNotEmpty(null));
    }

    @Test
    public void _test2() {
        // 字符串为空时，设置默认值
        System.out.println(StringUtils.defaultIfEmpty("", "default")); // default
        System.out.println(StringUtils.defaultIfBlank("", "default")); // default

        // 反转字符串
        System.out.println(StringUtils.reverse("hello world")); // dlrow olleh
    }

    @Test
    public void _test3() {
        // 重复字符串，返回重复的字符串
        System.out.println(StringUtils.repeat("a", 3)); // aaa
        System.out.println(StringUtils.repeat("b", ",", 5)); // b,b,b,b,b
        System.out.println(StringUtils.repeat("hello", 3)); // hellohellohello
    }

    @Test
    public void _test4() {
        // 大小写转换
        System.out.println(StringUtils.swapCase("Hello World")); // hELLO wORLD
        System.out.println(StringUtils.capitalize("hello world")); // Hello world
        System.out.println(StringUtils.upperCase("hello world")); // HELLO WORLD
        System.out.println(StringUtils.lowerCase("HELLO WORLD")); // hello world
    }

    @Test
    public void _test5() {
        // 去除字符串前后空格
        System.out.println(StringUtils.strip("  hello world  ")); // hello world
        System.out.println(StringUtils.stripStart("  hello world  ", " ")); // hello world
        System.out.println(StringUtils.stripEnd("  hello world  ", " ")); //  hello world
    }

    @Test
    public void _test6() {
        String str = "hello world you";
        System.out.println(StringUtils.length(str)); // 11

        // 截取字符串, 从指定位置开始截取(不包含), 截取到指定位置的字符串(包含，默认 length)
        System.out.println(StringUtils.substring(str, 6)); // world you
        System.out.println(StringUtils.substring(str, 6, 11)); // world
        System.out.println(StringUtils.substring(str, 6, 6)); // "" 空字符串

        // 截取指定位置之后的字符串,最后一个字符串出现的位置
        System.out.println(StringUtils.substringBeforeLast(str, " ")); // hello world
        // 截取指定位置之后的字符串,最后一个字符串出现的位置
        System.out.println(StringUtils.substringAfterLast(str, " ")); // you
        System.out.println(StringUtils.substringAfterLast(str, "o")); // u

        // 截取指定位置之前的字符串,首个字符串出现的位置
        System.out.println(StringUtils.substringBefore(str, " ")); // hello
        // 截取指定位置之后的字符串,首个字符串出现的位置
        System.out.println(StringUtils.substringAfter(str, " ")); // world you

        // 截取指定位置之间的字符串, 第一个出现的位置和最后一个出现的位置(两头都不包含)
        System.out.println(StringUtils.substringBetween(str, "h", "o")); // ell
        System.out.println(StringUtils.substringBetween(str, "h", "h")); // null
        System.out.println(StringUtils.substringBetween(str, " ", "d")); // worl
        System.out.println(StringUtils.substringBetween(str, "h", "d")); // ello worl
    }

    @Test
    public void _test7() {
        // 分割字符串
        System.out.println(Arrays.toString(StringUtils.split("hello world", " "))); // [hello, world]
        System.out.println(Arrays.toString(StringUtils.splitByCharacterType("hello world"))); // [hello,  , world]
        System.out.println(Arrays.toString(StringUtils.splitByCharacterTypeCamelCase("hello world"))); // [hello,  , world]
        System.out.println(Arrays.toString(StringUtils.splitByWholeSeparator("hello world", " "))); // [hello, world]
    }

    @Test
    public void _test8() {
        // 连接字符串
        // 默认使用空格连接
        System.out.println(StringUtils.join(new String[]{"hello", "world"}, " ")); // hello world
        System.out.println(StringUtils.join(new String[]{"hello", "world"}, ",")); // hello,world
        System.out.println(StringUtils.join(new String[]{"hello", "world"}, "")); // helloworld

        // 截取指定位置的字符串
        // 默认从第一个开始，截取到指定位置的字符串
        System.out.println(StringUtils.join(new String[]{"hello", "world"}, " ", 1, 2)); // world
        System.out.println(StringUtils.join(new String[]{"hello", "world"}, " ", 0, 2)); // hello world
        System.out.println(StringUtils.join(new String[]{"hello", "world"}, " ", 0, 3)); // hello world
    }

    @Test
    public void _test9() {
        String str = "hello world";

        // 判断字符串是否包含指定字符串
        System.out.println(StringUtils.contains(str, "hello")); // true
        System.out.println(StringUtils.containsIgnoreCase(str, "hello")); // true

        // 判断字符串是否包含指定字符
        System.out.println(StringUtils.containsAny(str, "hello", "world")); // true
        System.out.println(StringUtils.containsAny(str, "hello", "java")); // true
        System.out.println(StringUtils.containsAny(str, "hello", "")); // true
        System.out.println(StringUtils.containsAny(str, "hello", null)); // true

        // 判断字符串是否不包含指定字符
        System.out.println(StringUtils.containsNone(str, "hello")); // false
        // 判断字符串是否只包含指定字符
        System.out.println(StringUtils.containsOnly(str, "hello")); // false

        // 判断字符串是否包含空格
        System.out.println(StringUtils.containsWhitespace(str)); // true

        // 统计字符串中指定字符出现的次数
        System.out.println(StringUtils.countMatches(str, "l"));
    }

    @Test
    public void _test10() {
        String str = "hello world";

        // 判断字符串是否相等
        System.out.println(StringUtils.equals(str, "hello world")); // true
        System.out.println(StringUtils.equalsAny(str, "hello", "world")); // false
        System.out.println(StringUtils.equalsAnyIgnoreCase(str, "hello", "world")); // false
        System.out.println(StringUtils.equalsIgnoreCase(str, "hello world")); // true

        // 判断字符串是否以指定字符串开头
        System.out.println(StringUtils.startsWith(str, "hello")); // true
        System.out.println(StringUtils.startsWithAny(str, "hello", "java")); // true
        System.out.println(StringUtils.startsWithIgnoreCase(str, "hello")); // true

        // 判断字符串是否以指定字符串结尾
        System.out.println(StringUtils.endsWith(str, "world")); // true
        System.out.println(StringUtils.endsWithAny(str, "world", "java")); // true
        System.out.println(StringUtils.endsWithIgnoreCase(str, "world")); // true
    }

    @Test
    public void _test11() {
        String str = "hello world";

        // 判断字符串是数字
        System.out.println(StringUtils.isNumeric(str)); // false
        System.out.println(StringUtils.isNumericSpace(str)); // false
        System.out.println(StringUtils.isAlpha(str)); // false
        System.out.println(StringUtils.isAlphanumeric(str)); // false
    }

    @Test
    public void _test12() {
        String str = "hello world";

        // 从前往后，从0开始，找到指定字符串第一次出现的位置，返回位置，如果没有找到，返回 -1
        System.out.println(StringUtils.indexOf(str, "java")); // -1
        System.out.println(StringUtils.indexOf(str, "h")); // 0
        System.out.println(StringUtils.indexOf(str, "hello")); // 0
        System.out.println(StringUtils.indexOf(str, "hello world")); // 0
        System.out.println(StringUtils.indexOf(str, "ello")); // 1
        System.out.println(StringUtils.indexOf(str, " ")); // 5
        System.out.println(StringUtils.indexOf(str, "l")); // 2
        System.out.println(StringUtils.indexOfAny(str, "l", "o")); // 2
        System.out.println(StringUtils.indexOfIgnoreCase(str, "l")); // 2

        System.out.println("--------------------------------");

        // 从后往前，从0开始，找到指定字符串最后一次出现的位置，返回位置，如果没有找到，返回 -1
        System.out.println(StringUtils.lastIndexOf(str, "java")); // -1
        System.out.println(StringUtils.lastIndexOf(str, "h")); // 0
        System.out.println(StringUtils.lastIndexOf(str, "hello")); // 0
        System.out.println(StringUtils.lastIndexOf(str, "hello world")); // 0
        System.out.println(StringUtils.lastIndexOf(str, "ello")); // 1
        System.out.println(StringUtils.lastIndexOf(str, " ")); // 5
        System.out.println(StringUtils.lastIndexOf(str, "l")); // 9
        System.out.println(StringUtils.lastIndexOfAny(str, "l", "o")); // 9
        System.out.println(StringUtils.lastIndexOfIgnoreCase(str, "l")); // 9

        // 找到指定字符串最后一次出现的位置，从指定位置ordinal开始查找，返回位置，如果没有找到，返回 -1
        System.out.println(StringUtils.ordinalIndexOf("hello world", "o", 2)); // 7
    }

    @Test
    public void _test13() {
        // 替换字符串
        System.out.println(StringUtils.replace("hello world", "l", "L")); // heLLo worLd
        System.out.println(StringUtils.replaceChars("hello world", "l", "L")); // heLLo worLd
        System.out.println(StringUtils.replaceEach("hello world", new String[]{"l", "o"}, new String[]{"L", "O"})); // heLLO wOrLd
        System.out.println(StringUtils.replaceEachRepeatedly("hello world", new String[]{"l", "o"}, new String[]{"L", "O"})); // heLLO wOrLd
        System.out.println(StringUtils.replaceOnce("hello world", "l", "L")); // heLlo world
        System.out.println(StringUtils.replacePattern("hello world", "l", "L"));
    }

    @Test
    public void _test14() {
        String str = "hello world";

        // 删除字符串中指定字符
        System.out.println(StringUtils.remove(str, "l")); // heo word

        // 删除字符串中指定字符串
        System.out.println(StringUtils.removeStart(str, "hello")); //  world
        System.out.println(StringUtils.removeStartIgnoreCase(str, "hello")); //  world

        System.out.println("------------------------------------");

        // 删除字符串中指定字符
        System.out.println(StringUtils.removeEnd(str, "world")); // hello
        System.out.println(StringUtils.removeEndIgnoreCase(str, "world")); // hello
        System.out.println(StringUtils.removeIgnoreCase(str, "hello")); //  world

        System.out.println("------------------------------------");

        // 删除字符串中指定位置的空格
        System.out.println(StringUtils.deleteWhitespace("hello world")); // helloworld
        System.out.println(StringUtils.deleteWhitespace("  hello world  ")); // helloworld
        System.out.println(StringUtils.deleteWhitespace("hello world")); // helloworld
        System.out.println(StringUtils.deleteWhitespace("hello world  ")); // helloworld
        System.out.println(StringUtils.deleteWhitespace("  hello world")); // helloworld
        System.out.println(StringUtils.deleteWhitespace("hello world   ")); // helloworld
    }

    // 查询字符串中的所有出现位置，并在这些位置上替换为指定的字符串。使用replace方法结合循环。
    @Test
    public void _test15() {
        String originalString = "This is an example string with some text.";
        String searchString = "some";
        String replacementString = "example";

        // 查找所有出现的位置
        int index = StringUtils.indexOf(originalString, searchString);
        StringBuilder sb = new StringBuilder(originalString);

        while (index >= 0) {
            // 使用StringBuilder进行替换操作
            sb.replace(index, index + searchString.length(), replacementString);

            // 更新索引以查找下一个匹配项
            index = StringUtils.indexOf(sb.toString(), searchString, index + replacementString.length());
        }

        String result = sb.toString();
        System.out.println(result); // 输出替换后的字符串
    }

    @Test
    public void _test16() {

    }
}
