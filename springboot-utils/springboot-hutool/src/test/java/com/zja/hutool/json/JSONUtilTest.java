/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 14:11
 * @Since:
 */
package com.zja.hutool.json;

import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Json工具-单元测试类
 *
 * @author: zhengja
 * @since: 2023/10/16 14:11
 */
public class JSONUtilTest {

    //JSON转Bean
    @Test
    public void test_toBean() {
        String json = "{\"ADT\":[[{\"BookingCode\":[\"N\",\"V\"]}]]}";

        Price price = JSONUtil.toBean(json, Price.class);

        String s = price.getADT().get(0).get(0).getBookingCode().get(0);
        System.out.println(s); // N
    }

    //Bean转JSON
    //JSONUtil.toJsonStr可以将任意对象（Bean、Map、集合等）直接转换为JSON字符串。
    // 如果对象是有序的Map等对象，则转换后的JSON字符串也是有序的。
    @Test
    public void test_toJsonStrV1() {
        User user = new User();
        user.setName("handy");
        user.setSex("男");

        //转换，默认未json格式化
        String json = JSONUtil.toJsonStr(user);
        System.out.println(json); //{"name":"handy","aliasSex":"男"}

        //json格式化
        String jsonPrettyStr = JSONUtil.toJsonPrettyStr(user);
        System.out.println(jsonPrettyStr);
    }

    @Test
    public void test_toJsonStrSortedMap() {
        SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("attributes", "a");
                put("b", "b");
                put("c", "c");
            }
        };

        //JSONUtil.toJsonStr可以将任意对象（Bean、Map、集合等）直接转换为JSON字符串。
        // 如果对象是有序的Map等对象，则转换后的JSON字符串也是有序的。
        String jsonStr = JSONUtil.toJsonStr(sortedMap);
        System.out.println(jsonStr);
        String jsonPrettyStr = JSONUtil.toJsonPrettyStr(sortedMap);
        System.out.println(jsonPrettyStr);
    }

    @Test
    public void test_parseObj() {
        String html = "{\"name\":\"Something must have been changed since you leave\"}";
        JSONObject jsonObject = JSONUtil.parseObj(html);
        jsonObject.getStr("name");
    }


    @Test
    public void test_parseFromXml() {
        String s = "<sfzh>123</sfzh><sfz>456</sfz><name>aa</name><gender>1</gender>";
        JSONObject json = JSONUtil.parseFromXml(s);

        json.get("sfzh");
        json.get("name");
    }

    @Test
    public void test_toXmlStr() {
        final JSONObject put = JSONUtil.createObj()
                .set("aaa", "你好")
                .set("键2", "test");

        // <aaa>你好</aaa><键2>test</键2>
        final String s = JSONUtil.toXmlStr(put);
    }


    @Data
    public class ADT {
        private List<String> BookingCode;
    }

    @Data
    public class Price {
        private List<List<ADT>> ADT;
    }

    @Data
    public class User {
        private String name;

        @Alias("aliasSex")
        private String sex;
    }

}