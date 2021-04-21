package com.gongbo.common.validate.custom.annotation;


import com.gongbo.common.validate.custom.validator.RangeEndCheckValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RangeEndCheckValidator.class})
public @interface RangeEnd {

    boolean required() default true;

    /**
     * 分组标识
     */
    String rangeGroup();

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
 
    
