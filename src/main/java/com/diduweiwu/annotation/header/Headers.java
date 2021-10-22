package com.diduweiwu.annotation.header;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.header.HeadersProcessor;

import java.lang.annotation.*;

/**
 * 批量设置header
 */
@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Headers {
    Class<? extends IProcessor> processor() default HeadersProcessor.class;
}
