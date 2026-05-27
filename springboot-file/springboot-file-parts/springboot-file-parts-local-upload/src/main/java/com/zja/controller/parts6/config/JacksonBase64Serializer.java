package com.zja.controller.parts6.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Base64;

/**
 * Base64编码序列化器
 * 用于将字符串字段进行Base64编码后返回给前端
 *
 * @author: zhengja
 * @since: 2026/04/27
 */
public class JacksonBase64Serializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            // 将字符串进行Base64编码
            String encoded = Base64.getEncoder().encodeToString(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            gen.writeString(encoded);
        }
    }
}
