package com.diduweiwu.processor.custom;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;

import java.lang.annotation.*;

@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface My {
    String value();

    Class<? extends IProcessor> processor() default MyProcessor.class;
}
