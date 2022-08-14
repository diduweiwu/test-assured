package com.diduweiwu.annotation;


import cn.hutool.core.util.StrUtil;
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
public @interface Put {
    String value() default StrUtil.EMPTY;

    Class<? extends IProcessor> processor() default MethodProcessor.class;
}
