package com.hjzgg.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomDateSerializer extends JsonSerializer {

    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if(value instanceof String) {
            if("true".equals(value) || "false".equals(value)) {
                jgen.writeBoolean(Boolean.getBoolean((String) value));
            }
        }
    }

}