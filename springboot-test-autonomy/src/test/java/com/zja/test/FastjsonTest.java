package com.zja.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/30 13:55
 */
public class FastjsonTest {


    /**
     * fastjson提供了JSONField对序列化与反序列化进行定制
     */
    @Test
    public void test2(){
        SexDeserialize sexDeserialize = new SexDeserialize();
        System.out.println(sexDeserialize.getFastMatchToken());

    }

    /**
     * 反序列化类
     */
    class SexDeserialize implements ObjectDeserializer {

        public <T> T deserialze(DefaultJSONParser parser,
                                Type type,
                                Object fieldName) {
            String sex = parser.parseObject(String.class);
            if ("男".equals(sex)) {
                return (T) Boolean.TRUE;
            } else {
                return (T) Boolean.FALSE;
            }
        }

        public int getFastMatchToken() {
            return JSONToken.UNDEFINED;
        }

    }


    /**
     * 序列化类
     * User实体上加上个sex属性，类型为boolean
     */
    class SexSerializer implements ObjectSerializer {

        public void write(JSONSerializer serializer,
                          Object object,
                          Object fieldName,
                          Type fieldType,
                          int features)
                throws IOException {
            Boolean value = (Boolean) object;
            String text = "女";
            if (value != null && value == true) {
                text = "男";
            }
            serializer.write(text);
        }
    }


    /**
     * SerializerFeature特性:对生成的json格式的数据进行一些定制，常用的属性有很多
     * 如可以输入的格式更好看，使用单引号而非双引号等
     * SerializerFeature.WriteMapNullValue 是否输出null字符串，默认false 'name':''
     */
    @Test
    public void SerializerFeature(){
        User user = new User();
        user.setId(11L);
        user.setCreateTime(new Date());
        String jsonString = JSON.toJSONString(user, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.UseSingleQuotes,SerializerFeature.WriteMapNullValue);
        System.out.println(jsonString);
    }


    /**
     * 测试fastjson的序列化与反序列
     */
    @Test
    public void test(){
        serialize();
        deserialize();
    }

    public static void serialize() {
        User user = new User();
        user.setId(11L);
        user.setName("西安");
        user.setCreateTime(new Date());
        String jsonString = JSON.toJSONString(user);
        System.out.println(jsonString);
    }

    public static void deserialize() {
        String jsonString = "{\"createTime\":\"2018-08-17 14:38:38\",\"id\":11,\"name\":\"西安\"}";
        User user = JSON.parseObject(jsonString, User.class);
        System.out.println(user);
    }

    @Data
    static class  User {
        private Long   id;
        private String name;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    }
}
