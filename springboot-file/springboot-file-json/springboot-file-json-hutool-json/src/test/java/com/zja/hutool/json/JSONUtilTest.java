package com.zja.hutool.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: zhengja
 * @since: 2024/01/18 15:21
 */
public class JSONUtilTest {

    @Test
    public void test() {
        String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"上海\"}";

        // Parse JSON string to JSONObject
        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
        String name = jsonObject.getStr("name");
        int age = jsonObject.getInt("age");
        String city = jsonObject.getStr("city");

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("City: " + city);

        System.out.println("jsonObject:" + jsonObject);
    }

    // JSONUtil.toJsonStr可以将任意对象（Bean、Map、集合等）直接转换为JSON字符串。
    @Test
    public void test1() {
        // 如果对象是有序的Map等对象，则转换后的JSON字符串也是有序的。
        SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("attributes", "a");
                put("b", "b");
                put("c", "c");
            }
        };

        String jsonStr = JSONUtil.toJsonStr(sortedMap);
        String jsonStr2 = JSONUtil.toJsonPrettyStr(sortedMap); // 格式化后的数据
        System.out.println(jsonStr);
        System.out.println(jsonStr2);
    }

    // 解下 json、xml
    @Test
    public void test2() {
        // json
        String html = "{\"name\":\"Something must have been changed since you leave\"}";
        JSONObject jsonObject = JSONUtil.parseObj(html);
        jsonObject.getStr("name");

        // xml
        String s = "<sfzh>123</sfzh><sfz>456</sfz><name>aa</name><gender>1</gender>";
        JSONObject json = JSONUtil.parseFromXml(s);
        json.get("sfzh");
        json.get("name");

        // 转为 xml
        final JSONObject put = JSONUtil.createObj()
                .set("aaa", "你好")
                .set("键2", "test");
        final String xmlStr = JSONUtil.toXmlStr(put);
        System.out.println(xmlStr); // <aaa>你好</aaa><键2>test</键2>
    }

}
