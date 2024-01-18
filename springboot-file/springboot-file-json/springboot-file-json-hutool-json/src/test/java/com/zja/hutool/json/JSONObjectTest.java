package com.zja.hutool.json;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/18 15:21
 */
public class JSONObjectTest {

    // 创建 JSONObject
    @Test
    public void test() {
        JSONObject json1 = JSONUtil.createObj()
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3");

        JSONObject json2 = new JSONObject();
        System.out.println(json1);
        System.out.println(json2);
    }

    // 转换：JSON字符串解析
    @Test
    public void test2() {
        String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";
        // 方法一：使用工具类转换
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        System.out.println(jsonObject);
        // 方法二：new的方式转换
        JSONObject jsonObject2 = new JSONObject(jsonStr);
        System.out.println(jsonObject2);

        // JSON对象转字符串（一行）
        String objectString = jsonObject.toString();
        System.out.println(objectString);

        // 也可以美化一下，即显示出带缩进的JSON：
        String stringPretty = jsonObject.toStringPretty();
        System.out.println(stringPretty);
    }


    // 转换：JavaBean解析
    @Test
    public void test3() {
        User user = new User();
        user.setName("nameTest");
        user.setDate(new Date());
        user.setSqs(CollectionUtil.newArrayList(new Seq(null), new Seq("seq2")));

        // 第二个参数：false表示不跳过空值
        JSONObject json = JSONUtil.parseObj(user, false);
        // 第三个参数：true表示保持有序
        // JSONObject json = JSONUtil.parseObj(user, false, true);
        json.setDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期时间格式化

        Console.log(json.toStringPretty());
    }

    @Data
    static class User {
        private String name;
        private String a;
        private Date date;
        private List<Seq> sqs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Seq {
        private String seq;
    }
}
