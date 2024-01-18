package com.zja.hutool.json;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/18 15:21
 */
public class JSONArrayTest {

    // 创建 JSONArray
    @Test
    public void test() {
        // 方法1
        JSONArray array = JSONUtil.createArray();
        // 方法2
        // JSONArray array = new JSONArray();

        array.add("value1");
        array.add("value2");
        array.add("value3");

        // 转为JSONArray字符串
        String jsonStr = array.toString();
        System.out.println(jsonStr);
    }


    // 解析
    @Test
    public void test2() {
        // 1.从Bean列表解析
        KeyBean b1 = new KeyBean();
        b1.setAkey("aValue1");
        b1.setBkey("bValue1");
        KeyBean b2 = new KeyBean();
        b2.setAkey("aValue2");
        b2.setBkey("bValue2");

        ArrayList<KeyBean> list = CollUtil.newArrayList(b1, b2);

        JSONArray jsonArray = JSONUtil.parseArray(list);
        System.out.println(jsonArray); // [{"akey":"aValue1","bkey":"bValue1"},{"akey":"aValue2","bkey":"bValue2"}]

        String akey = jsonArray.getJSONObject(0).getStr("akey");
        System.out.println(akey); // aValue1


        // 2.从json字符串解析
        String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
        JSONArray array = JSONUtil.parseArray(jsonStr);
        System.out.println(array);
    }

    // 转换
    @Test
    public void test3() {
        String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
        JSONArray array = JSONUtil.parseArray(jsonArr);

        // 转换为 Bean
        List<User> userList = JSONUtil.toList(array, User.class);
        Integer id = userList.get(0).getId();
        System.out.println(id); // 111


        // 转换为 Dict 特殊Map
        List<Dict> list = JSONUtil.toList(array, Dict.class);
        Integer id1 = list.get(0).getInt("id");
        System.out.println(id1); // 111

        // 转换为 数组
        User[] userlist = array.toArray(new User[0]);
        System.out.println(userlist);
    }

    // JSON路径: 处理JSON的层级特别深
    @Test
    public void test4() {
        String jsonStr = "[{\"id\": \"1\",\"name\": \"a\"},{\"id\": \"2\",\"name\": \"b\"}]";
        final JSONArray jsonArray = JSONUtil.parseArray(jsonStr);

        Object byPath = jsonArray.getByPath("[1].name");
        System.out.println(byPath); // b
    }


    @Data
    static class KeyBean {
        private String akey;
        private String bkey;
    }

    @Data
    static class User {
        private Integer id;
        private String name;
    }
}
