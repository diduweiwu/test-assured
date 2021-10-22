package com.diduweiwu.processor.param;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * body 上传文件
 */
public class BodyFileProcessor implements IProcessor {

    /**
     * 解析参数
     *
     * @param api
     */
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, value -> request.body((File) value));
    }
}
