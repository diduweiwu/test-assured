package com.diduweiwu.annotation.cookie;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.cookie.CookiesProcessor;

import java.lang.annotation.*;

/**
 * 批量设置cookies
 */
@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cookies {
    Class<? extends IProcessor> processor() default CookiesProcessor.class;
}
