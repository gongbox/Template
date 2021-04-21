package com.gongbo.template.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConfig {

    @JsonComponent
    public static class PageJsonSerializer extends JsonSerializer<IPage<?>> {

        @Override
        public void serialize(IPage<?> ipage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeNumberField("total", ipage.getTotal());
            jsonGenerator.writeNumberField("size", ipage.getSize());
            jsonGenerator.writeNumberField("page", ipage.getCurrent());
            jsonGenerator.writeObjectField("data", ipage.getRecords());
            if (ipage instanceof Page) {
                Page<?> page = (Page<?>) ipage;
                jsonGenerator.writeBooleanField("hasPrevious", page.hasPrevious());
                jsonGenerator.writeBooleanField("hasNext", page.hasNext());
            }

            jsonGenerator.writeEndObject();
        }
    }
}
