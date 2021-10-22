package com.diduweiwu.processor.cookie;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

public class CookiesProcessor implements IProcessor {

    /**
     * 解析cokkies参数
     *
     * @param api
     */
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, value -> request.cookies((Map<String, Object>) value));
    }
}
