package com.diduweiwu.annotation.cookie;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.cookie.CookieProcessor;

import java.lang.annotation.*;

/**
 * 设置单个cookie
 */
@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cookie {
    // 查询条件-键
    String key() default "";

    // 查询条件-值
    String value() default "";

    Class<? extends IProcessor> processor() default CookieProcessor.class;
}
