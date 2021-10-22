package com.diduweiwu.annotation.param;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.param.BodyFilePathProcessor;

import java.lang.annotation.*;

/**
 * 文件上传
 */
@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BodyFilePath {
    // 查询条件-值
    String value() default "";

    Class<? extends IProcessor> processor() default BodyFilePathProcessor.class;
}
