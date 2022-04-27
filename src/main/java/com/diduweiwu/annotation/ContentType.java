package com.diduweiwu.annotation;


import com.diduweiwu.processor.ContentTypeProcessor;
import com.diduweiwu.processor.contract.IProcessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.*;

/**
 * @author nier
 */
@Inherited
@IAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType {
    String value();

    Class<? extends IProcessor> processor() default ContentTypeProcessor.class;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class Type {
        public static final String APPLICATION_JSON = "application/json";
    }
}
