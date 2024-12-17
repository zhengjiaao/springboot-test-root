package com.zja.jdk.string;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: zhengja
 * @Date: 2024-12-17 14:16
 */
public class StringFormatTest {

    // String.format
    @Test
    public void test_1(){
        String str = String.format("hello %s", "world");
        System.out.println(str);

        String template = "Hello, my name is %s and I am %d years old.";
        Object[] replageArgs ={"Alice",25};

        String message = String.format(template, replageArgs);
        System.out.println(message); // 输出：Hello, my name is Alice and I am 25 years old.
    }

    // MessageFormat.format
    @Test
    public void test_2(){
        String template = "Hello, my home is {0} and I am {1} years old.";
        Object[] replageArgs ={"Alice",25};
        String message = MessageFormat.format(template, replageArgs);
        System.out.println(message);
    }

    private static String TEMPLATE = "姓名：${name!}，年龄：${age!}，手机号：${phone!}";
    private static final Map<String, String> ARGS = new HashMap<String, String>() {{
        put("name", "张三");
        put("phone", "10086");
        put("age", "21");
    }};

    // StringUtils.replace
    @Test
    public void test_3(){
        String remark = TEMPLATE;
        for (Map.Entry<String, String> entry : ARGS.entrySet()) {
            remark = StringUtils.replace(remark, "${" + entry.getKey() + "!}", entry.getValue());
        }
        System.out.println(remark);
    }

    // 正则 Pattern.compile
    @Test
    public void test_4(){
        Pattern TEMPLATE_ARG_PATTERN = Pattern.compile("\\$\\{(.+?)!}"); // ${param!}
        Matcher m = TEMPLATE_ARG_PATTERN.matcher(TEMPLATE);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String arg = m.group(1);
            String replaceStr = ARGS.get(arg);
            m.appendReplacement(sb, replaceStr != null ? replaceStr : "");
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
    }
}
