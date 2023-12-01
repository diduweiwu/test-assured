package com.diduweiwu.processor.authorization;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * bear token 配置
 */
public class BearTokenProcessor implements IProcessor {
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, value -> request.header("Authorization", "Bearer " + value));
    }
}
