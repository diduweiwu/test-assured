package com.diduweiwu.annotation.authorization;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.authorization.BearTokenProcessor;
import com.diduweiwu.processor.contract.IProcessor;

import java.lang.annotation.*;


@Inherited
@IAnnotation
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BearToken {
    String value() default "";

    Class<? extends IProcessor> processor() default BearTokenProcessor.class;
}
