package com.diduweiwu.annotation.param;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.param.QueryProcessor;

import java.lang.annotation.*;

@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
    // 查询条件-键
    String key() default "";

    // 查询条件-值
    String value() default "";

    Class<? extends IProcessor> processor() default QueryProcessor.class;
}
