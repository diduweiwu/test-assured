package com.diduweiwu.annotation.param;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.param.BodyProcessor;

import java.lang.annotation.*;

/**
 * body 传参
 */
@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Body {
    String value() default "";

    Class<? extends IProcessor> processor() default BodyProcessor.class;
}
