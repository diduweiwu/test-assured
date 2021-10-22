package com.diduweiwu.annotation.param;

import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.param.BodyFileProcessor;

import java.lang.annotation.*;

/**
 * 文件流上传
 */
@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BodyFile {
    Class<? extends IProcessor> processor() default BodyFileProcessor.class;
}
