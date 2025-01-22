/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-24 13:59
 * @Since:
 */
package com.zja.onlyoffice.config;

import feign.Response;
import feign.jackson.JacksonDecoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 解决返回数据String类型再转json时失败
 * 描述：Feign Decoder使用json解码器后，若请求返回 String 类型，此时无论使用 String 或 Object 类型接收数据都会报错无法解析
 */
public class MyJacksonDecoder extends JacksonDecoder {
    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (response.body() == null) {
            return null;
        }
        if (type == String.class) {
            return StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
        }
        return super.decode(response, type);
    }
}
