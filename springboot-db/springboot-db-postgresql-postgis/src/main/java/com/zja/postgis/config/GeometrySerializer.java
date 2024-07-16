package com.zja.postgis.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-15 14:50
 */
public class GeometrySerializer extends StdSerializer<Geometry> {

    protected GeometrySerializer() {
        super(Geometry.class);
    }

    @Override
    public void serialize(Geometry value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.toText());
    }
}
