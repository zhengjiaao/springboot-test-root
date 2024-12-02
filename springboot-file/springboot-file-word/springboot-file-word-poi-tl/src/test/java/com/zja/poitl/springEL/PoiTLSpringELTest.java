package com.zja.poitl.springEL;

import com.deepoove.poi.config.Configure;
import com.zja.poitl.util.WordPoiTLUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring EL表达式
 *
 * @Author: zhengja
 * @Date: 2024-11-25 19:14
 */
public class PoiTLSpringELTest {

    public static final Map<String, Object> data = new HashMap<>();

    private static void put(String key, Object value) {
        data.put(key, value);
    }

    private String getPath(String fileName) {
        return getClass().getResource("/" + fileName).getPath();
    }

    // 字符串操作：替换、截取、判空、包含等常用操作
    @Test
    public void test_1() throws IOException {
        put("title", "Spring EL表达式(需启动)");
        put("name", "李四");
        // put("name", "poi-tl");
        put("age", 19);
        put("position", "长沙市-天心区");

        put("sex", true);
        put("empty", "");
        put("empty", null);
        put("time", new Date());
        put("price", 100000);
        put("localDate", LocalDateTime.now());
        put("dogs", Arrays.asList(data, data));

        Configure configure = Configure.builder()
                .useSpringEL(true) // 启用 Spring EL表达式
                .build();
        WordPoiTLUtil.generateWord("target/out_springEL_1.docx", data, "templates/word/springEL/springEL_1.docx", configure);
    }

    @Test
    public void test_2() {

    }

    @Test
    public void test_3() {

    }

    @Test
    public void test_4() {

    }

}
