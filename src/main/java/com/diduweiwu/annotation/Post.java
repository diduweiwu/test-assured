package com.diduweiwu.annotation;


import com.diduweiwu.processor.MethodProcessor;
import com.diduweiwu.processor.contract.IProcessor;

import java.lang.annotation.*;

/**
 * @author nier
 */
@Inherited
@IAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Post {
    Class<? extends IProcessor> processor() default MethodProcessor.class;
}
