package com.zja.postgis.config;

import org.locationtech.jts.geom.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Jackson 配置自定义的【序列化、反序列化】
 * @Author: zhengja
 * @Date: 2024-07-15 14:41
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(Geometry.class, new GeometrySerializer());
        // 反序列化需要指定具体类型，而不是Geometry
        builder.deserializerByType(Geometry.class, new GeometryDeserializer());
        builder.deserializerByType(Point.class, new GeometryDeserializer());
        builder.deserializerByType(LineString.class, new GeometryDeserializer());
        builder.deserializerByType(Polygon.class, new GeometryDeserializer());
        builder.deserializerByType(MultiPoint.class, new GeometryDeserializer());
        builder.deserializerByType(MultiLineString.class, new GeometryDeserializer());
        builder.deserializerByType(MultiPolygon.class, new GeometryDeserializer());
        return builder;
    }
}
