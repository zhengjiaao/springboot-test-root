/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 14:29
 * @Since:
 */
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
 * -单元测试类
 *
 * @author: zhengja
 * @since: 2023/10/16 14:29
 */
public class JSONArrayTest {

    @Test
    public void test_parseArray() {
        KeyBean b1 = new KeyBean();
        b1.setAkey("aValue1");
        b1.setBkey("bValue1");
        KeyBean b2 = new KeyBean();
        b2.setAkey("aValue2");
        b2.setBkey("bValue2");

        ArrayList<KeyBean> list = CollUtil.newArrayList(b1, b2);

        // [{"akey":"aValue1","bkey":"bValue1"},{"akey":"aValue2","bkey":"bValue2"}]
        JSONArray jsonArray = JSONUtil.parseArray(list);

        // aValue1
        jsonArray.getJSONObject(0).getStr("akey");
    }

    @Test
    public void test_parseArray2() {
        String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
        JSONArray array = JSONUtil.parseArray(jsonStr);
    }

    @Test
    public void test_toList() {
        String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
        JSONArray array = JSONUtil.parseArray(jsonArr);

        List<User> userList = JSONUtil.toList(array, User.class);

        // 111
        userList.get(0).getId();
    }

    @Test
    public void test_toList2() {
        String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
        JSONArray array = JSONUtil.parseArray(jsonArr);

        List<Dict> list = JSONUtil.toList(array, Dict.class);

        // 111
        list.get(0).getInt("id");
    }

    //解决：JSON的层级特别深，那么获取某个值就变得非常麻烦
    @Test
    public void test_getByPath() {
        String jsonStr = "[{\"id\": \"1\",\"name\": \"a\"},{\"id\": \"2\",\"name\": \"b\"}]";
        final JSONArray jsonArray = JSONUtil.parseArray(jsonStr);

        // b
        jsonArray.getByPath("[1].name");
    }


    @Data
    public class KeyBean {
        private String akey;
        private String bkey;
    }

    @Data
    static class User {
        private Integer id;
        private String name;
    }
}