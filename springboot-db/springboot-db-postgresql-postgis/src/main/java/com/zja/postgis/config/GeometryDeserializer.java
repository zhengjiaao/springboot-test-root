package com.zja.postgis.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import java.io.IOException;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 14:50
 */
public class GeometryDeserializer extends StdDeserializer<Geometry> {
    private final WKTReader wktReader = new WKTReader();

    protected GeometryDeserializer() {
        super(Geometry.class);
    }

    @Override
    public Geometry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String wkt = p.getText();
        try {
            return wktReader.read(wkt);
        } catch (Exception e) {
            throw new JsonProcessingException(e) {
            };
        }
    }
}