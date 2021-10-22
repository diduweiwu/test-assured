package com.diduweiwu.processor.param;

import cn.hutool.core.io.FileUtil;
import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class BodyFilePathProcessor implements IProcessor {

    /**
     * 解析参数
     *
     * @param api
     */
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, (value) -> request.body(FileUtil.file(String.valueOf(value))));
    }
}
