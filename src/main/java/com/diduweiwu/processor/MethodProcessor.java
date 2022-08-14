package com.diduweiwu.processor;

import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;

/**
 * method标记处理器,置空即可
 */
@Slf4j
public class MethodProcessor implements IProcessor {
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> annotationType, RequestSpecification request) {
        this.executeKeyValueMap(api, element, annotationType, map -> {
            Object value = map.get("value");
            if (Objects.nonNull(value)) {
                request.basePath(String.valueOf(value));
            }
        });
    }
}
