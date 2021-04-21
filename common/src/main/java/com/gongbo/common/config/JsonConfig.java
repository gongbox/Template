package com.gongbo.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.gongbo.common.constant.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@Configuration
public class JsonConfig {
    @Autowired
    private Collection<Module> moduleList;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        customObjectMapper(objectMapper);
        return objectMapper;
    }

    public void customObjectMapper(ObjectMapper objectMapper) {
        //序列化
        //this.setSerializationInclusion(JsonInclude.Include.NON_NULL);//设置null值不参与序列化
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); //禁用空对象转换json
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);//日期自动转化时间戳
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);//枚举类自动调用toString
        //objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);//是否Map的带有null值的entry被序列化（true）

        //反序列化
        //未知字段或没有getter and setter方法自动过滤
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false);//该特性决定对于json浮点数，是否使用BigDecimal来序列化。如果不允许，则使用Double序列化。
        objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false);//该特性决定对于json整形（非浮点），是否使用BigInteger来序列化。如果不允许，则根据数值大小来确定 是使用Integer}, {@link Long} 或者BigInteger
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);//该特性决定当遇到JSON null的对象是java 原始类型，则是否抛出异常。当false时，则使用0 for 'int', 0.0 for double 来设定原始对象初始值。
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false);//该特性决定JSON ARRAY是映射为Object[]还是List<Object>。如果开启，都为Object[]，false时，则使用List。
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);//使用toString反解析枚举
        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);//该特性决定parser是否允许JSON整数以多0开始(比如，如果000001赋值给json某变量，
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);//允许解析注释
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//是否将允许使用非双引号属性名字
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//是否允许单引号来包住属性名称和字符串值
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);//是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。

        objectMapper.setDateFormat(new SimpleDateFormat(Times.Pattern.DEFAULT_DATE_TIME));
        //驼峰命名法转换为小写加下划线
        //this.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        registerStringNullToEmpty(objectMapper);//string null值反序列化成空字符串
        //registerAllTypeNullToEmpty(objectMapper);//所有类型null值序列化成空字符串
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(Times.Formatter.DEFAULT_DATE_TIME));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(Times.Formatter.DEFAULT_DATE));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(Times.Formatter.DEFAULT_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(Times.Formatter.DEFAULT_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(Times.Formatter.DEFAULT_DATE));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(Times.Formatter.DEFAULT_TIME));
        objectMapper.registerModule(javaTimeModule);

        objectMapper.registerModules(moduleList);
    }

    public static void registerStringNullToEmpty(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer() {
            @Override
            public String getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return "";
            }
        });
        objectMapper.registerModule(module);
    }

    public static void registerAllTypeNullToEmpty(ObjectMapper objectMapper) {
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("");
            }
        });
    }

}
