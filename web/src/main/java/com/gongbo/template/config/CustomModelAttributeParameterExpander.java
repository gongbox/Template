
package com.gongbo.template.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;
import com.gongbo.common.constant.ParamNames;
import com.gongbo.common.params.*;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Component
@Primary
@ConditionalOnProperty(name = "config.swagger-enable", havingValue = "true")
public class CustomModelAttributeParameterExpander extends ModelAttributeParameterExpander {

    @Autowired
    public CustomModelAttributeParameterExpander(FieldProvider fields, AccessorsProvider accessors, EnumTypeDeterminer enumTypeDeterminer) {
        super(fields, accessors, enumTypeDeterminer);
    }

    protected boolean support(ResolvedType resolvedType) {
        return resolvedType.isInstanceOf(BaseParam.class);
    }

    @Override
    public List<Parameter> expand(ExpansionContext context) {
        List<Parameter> parameters = super.expand(context);

        if (!support(context.getParamType())) {
            return parameters;
        }

        parameters = newArrayList(parameters);

        //请求类型
        String parameterType = determineScalarParameterType(
                context.getOperationContext().consumes(),
                context.getOperationContext().httpMethod());

        //添加分页参数
        if (context.getParamType().isInstanceOf(PageParam.class)) {
            ResolvedPrimitiveType fieldType = ResolvedPrimitiveType.all().get(4);

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.PAGE_SIZE)
                    .description("每页条数")
                    .defaultValue("20")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("int"))
                    .parameterType(parameterType)
                    .order(1000)
                    .build());

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.PAGE)
                    .description("当前页数")
                    .defaultValue("1")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("int"))
                    .parameterType(parameterType)
                    .order(1001)
                    .build());
        }

        //添加日期参数
        if (context.getParamType().isInstanceOf(DateParam.class)) {
            ResolvedObjectType fieldType = ResolvedObjectType.create(LocalDate.class, null, null, null);

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.DATE)
                    .description("日期")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("string"))
                    .parameterType(parameterType)
                    .order(1002)
                    .build());
        }

        //添加日期范围参数
        if (context.getParamType().isInstanceOf(DateRangeParam.class)) {
            ResolvedObjectType fieldType = ResolvedObjectType.create(LocalDate.class, null, null, null);

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.START_DATE)
                    .description("开始日期")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("string"))
                    .parameterType(parameterType)
                    .order(1010)
                    .build());

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.END_DATE)
                    .description("结束日期")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("string"))
                    .parameterType(parameterType)
                    .order(1011)
                    .build());
        }

        //添加时间范围参数
        if (context.getParamType().isInstanceOf(DateTimeRangeParam.class)) {
            ResolvedObjectType fieldType = ResolvedObjectType.create(LocalDateTime.class, null, null, null);

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.START_DATE_TIME)
                    .description("开始时间")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("string"))
                    .parameterType(parameterType)
                    .order(1030)
                    .build());

            parameters.add(new ParameterBuilder()
                    .name(ParamNames.END_DATE_TIME)
                    .description("结束时间")
                    .required(Boolean.FALSE)
                    .allowMultiple(false)
                    .type(fieldType)
                    .modelRef(new ModelRef("string"))
                    .parameterType(parameterType)
                    .order(1031)
                    .build());
        }
        return ImmutableList.copyOf(parameters);
    }

    public static String determineScalarParameterType(Set<? extends MediaType> consumes, HttpMethod method) {
        String parameterType = "query";

        if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED)
                && method == HttpMethod.POST) {
            parameterType = "form";
        } else if (consumes.contains(MediaType.MULTIPART_FORM_DATA)
                && method == HttpMethod.POST) {
            parameterType = "formData";
        }

        return parameterType;
    }
}
