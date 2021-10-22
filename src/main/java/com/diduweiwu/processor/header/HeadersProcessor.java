package com.diduweiwu.processor.header;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

public class HeadersProcessor implements IProcessor {

    /**
     * 解析headers参数
     *
     * @param api
     */
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, value -> request.headers((Map<String, Object>) value));
    }
}
