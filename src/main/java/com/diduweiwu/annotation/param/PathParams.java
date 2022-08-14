package com.diduweiwu.annotation.param;

import cn.hutool.core.util.StrUtil;
import com.diduweiwu.annotation.IAnnotation;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.param.PathParamsProcessor;

import java.lang.annotation.*;

@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParams {
    // 查询条件-键
    String value() default StrUtil.EMPTY;

    Class<? extends IProcessor> processor() default PathParamsProcessor.class;
}
