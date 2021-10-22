package com.diduweiwu.annotation;

import com.diduweiwu.processor.HostProcessor;
import com.diduweiwu.processor.contract.IProcessor;

import java.lang.annotation.*;

@Inherited
@IAnnotation
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Host {
    String value() default "";

    /**
     * 标注处理器类
     *
     * @return
     */
    Class<? extends IProcessor> processor() default HostProcessor.class;
}
