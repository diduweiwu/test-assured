package com.diduweiwu.processor.param;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.diduweiwu.processor.contract.IProcessor;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

public class PathParamsProcessor implements IProcessor {

    /**
     * 解析参数
     *
     * @param api
     */
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation,
                (value) -> {
                    Map<String, ?> pathParams = null;
                    if (value instanceof String) {
                        pathParams = JSONUtil.toBean(String.valueOf(value), new TypeReference<Map<String, ?>>() {
                        }, true);
                    }

                    Assert.isTrue(value instanceof Map, "PathParams 必须为Json Object字符串或者Map对象");
                    pathParams = (Map<String, ?>) value;
                    request.pathParams(pathParams);
                });
    }
}
