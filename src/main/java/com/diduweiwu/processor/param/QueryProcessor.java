package com.diduweiwu.processor.param;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class QueryProcessor implements IProcessor {

    /**
     * 解析参数
     *
     * @param api
     */
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.execute(api, element, clazzAnnotation, (key, value) -> request.queryParam(key, value));
    }
}
