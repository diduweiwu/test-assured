package com.diduweiwu.processor.custom;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class MyProcessor implements IProcessor {
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, value -> request.cookie("My", value));
    }
}
