/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 14:26
 * @Since:
 */
package com.zja.hutool.json;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * -单元测试类
 * @author: zhengja
 * @since: 2023/10/16 14:26
 */
public class JSONObjectTest {

    @Test
    public void test_createObj() {
        JSONObject json1 = JSONUtil.createObj()
                .put("a", "value1")
                .put("b", "value2")
                .put("c", "value3");
    }


    @Test
    public void test_parseObj() {
        String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";
        //方法一：使用工具类转换
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        //方法二：new的方式转换
        JSONObject jsonObject2 = new JSONObject(jsonStr);

        //JSON对象转字符串（一行）
        jsonObject.toString();

        // 也可以美化一下，即显示出带缩进的JSON：
        jsonObject.toStringPretty();
    }

    @Test
    public void test_parseObj2() {
        UserA userA = new UserA();
        userA.setName("nameTest");
        userA.setDate(new Date());

        // false表示不跳过空值
        JSONObject json = JSONUtil.parseObj(userA, false, true);
        json.setDateFormat("yyyy-MM-dd HH:mm:ss");  //可选的，时间格式化
        Console.log(json.toStringPretty());
    }

    // 注解使用Lombok
    @Data
    public class UserA {
        private String name;
        private String a;
        private Date date;
    }

}