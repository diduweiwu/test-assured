package com.diduweiwu.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.diduweiwu.annotation.*;
import com.diduweiwu.processor.contract.IPostCheck;
import com.diduweiwu.processor.contract.IProcessor;
import com.diduweiwu.processor.contract.ISetUp;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RequestUtil {

    /**
     * 发送请求,无前置和后置检查
     *
     * @return
     */
    public static Response send(Object api) {
        return sendWithSetUps(api, Collections.emptyList());
    }

    /**
     * 发送请求,附带一个前置检查
     *
     * @param api
     * @param iSetUp
     * @return
     */
    public static Response send(Object api, ISetUp iSetUp) {
        return sendWithSetUps(api, ListUtil.of(iSetUp));
    }

    /**
     * 发送请求,附带一个后置检查
     *
     * @param api
     * @param iPostCheck
     * @return
     */
    public static Response send(Object api, IPostCheck iPostCheck) {
        return send(api, ListUtil.empty(), ListUtil.of(iPostCheck));
    }

    /**
     * 发送请求,附带一系列后置检查
     *
     * @param api
     * @param iPostChecks
     * @return
     */
    public static Response sendWithChecks(Object api, List<IPostCheck> iPostChecks) {
        return send(api, ListUtil.empty(), iPostChecks);
    }

    /**
     * 发送请求,附带一个前置检查和后置检查
     *
     * @param api
     * @param iSetUp
     * @return
     */
    public static Response send(Object api, ISetUp iSetUp, IPostCheck iPostCheck) {
        return send(api, ListUtil.of(iSetUp), ListUtil.of(iPostCheck));
    }

    /**
     * 发送请求,附带一系列前置检查和一系列后置检查
     *
     * @param api
     * @return
     */
    public static Response send(Object api, List<ISetUp> iSetUps, List<IPostCheck> iPostChecks) {
        Response response = sendWithSetUps(api, iSetUps);
        if (CollectionUtil.isNotEmpty(iPostChecks)) {
            iPostChecks.forEach(postCheck -> postCheck.execute(response));
        }

        return response;
    }

    /**
     * 发送请求,附带一系列前置检查
     *
     * @param api
     * @param iSetUps
     * @return
     */
    public static Response sendWithSetUps(Object api, List<ISetUp> iSetUps) {
        Class<?> clazz = api.getClass();
        if (AnnotationUtil.hasAnnotation(clazz, Get.class)) {
            return get(api, iSetUps);
        }
        if (AnnotationUtil.hasAnnotation(clazz, Post.class)) {
            return post(api, iSetUps);
        }
        if (AnnotationUtil.hasAnnotation(clazz, Put.class)) {
            return put(api, iSetUps);
        }
        if (AnnotationUtil.hasAnnotation(clazz, Delete.class)) {
            return delete(api, iSetUps);
        }
        if (AnnotationUtil.hasAnnotation(clazz, Options.class)) {
            return options(api, iSetUps);
        }

        throw new RuntimeException("不支持的请求类型");
    }

    /**
     * 初始化请求,附带一系列前置准备
     *
     * @param api
     * @param iSetUps
     * @return
     */
    public static RequestSpecification composeRequest(Object api, List<ISetUp> iSetUps) {
        Assert.notNull(api, "接口对象不能为null");
        RequestSpecification request = given();
        // 分析class注解
        analyzeClazz(api, request);
        // 分析field注解
        analyzeFields(api, request);

        // 分析method注解
        analyzeMethods(api, request);

        if (CollectionUtil.isNotEmpty(iSetUps)) {
            iSetUps.forEach(setUp -> setUp.execute(request));
        }

        return request;
    }

    public static Response get(Object api, List<ISetUp> iSetUps) {
        return composeRequest(api, iSetUps).get();
    }

    public static Response post(Object api, List<ISetUp> iSetUps) {
        return composeRequest(api, iSetUps).post();
    }

    public static Response put(Object api, List<ISetUp> iSetUps) {
        return composeRequest(api, iSetUps).put();
    }

    public static Response delete(Object api, List<ISetUp> iSetUps) {
        return composeRequest(api, iSetUps).delete();
    }

    public static Response options(Object api, List<ISetUp> iSetUps) {
        return composeRequest(api, iSetUps).options();
    }

    public static Response get(Object api) {
        return composeRequest(api, Collections.emptyList()).get();
    }

    public static Response post(Object api) {
        return composeRequest(api, Collections.emptyList()).post();
    }

    public static Response put(Object api) {
        return composeRequest(api, Collections.emptyList()).put();
    }

    public static Response delete(Object api) {
        return composeRequest(api, Collections.emptyList()).delete();
    }

    public static Response options(Object api) {
        return composeRequest(api, Collections.emptyList()).options();
    }

    /**
     * 分析clazz对象注解属性
     *
     * @param o
     */
    private static void analyzeClazz(Object o, RequestSpecification request) {
        analyzeAnnotation(o, o.getClass(), request);
    }

    /**
     * 分析注解元素上面的注解信息
     *
     * @param element
     */
    private static void analyzeAnnotation(Object o, AnnotatedElement element, RequestSpecification request) {
        Annotation[] clazzAnnotations = AnnotationUtil.getAnnotations(element, true);
        // 获取对象所有的注解
        for (Annotation clazzAnnotation : clazzAnnotations) {
            // 解析被IAnnotation注解过的注解
            if (AnnotationUtil.hasAnnotation(clazzAnnotation.annotationType(), IAnnotation.class)) {
                // 获取注解的值
                Map<String, Object> annotationValueMap = AnnotationUtil.getAnnotationValueMap(element, clazzAnnotation.annotationType());
                Class<IProcessor> processorClazz = (Class<IProcessor>) annotationValueMap.get("processor");
                Assert.notNull(processorClazz, String.format("%s中未找到Processor配置", clazzAnnotation.annotationType().getName()));

                // 实例化处理器并调用处理函数
                ReflectUtil.newInstance(processorClazz).execute(o, element, clazzAnnotation.annotationType(), request);
            }
        }
    }

    /**
     * 分析属性对应的注解
     *
     * @param o
     */
    private static void analyzeFields(Object o, RequestSpecification request) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            analyzeAnnotation(o, field, request);
        }
    }

    /**
     * 分析方法对应的注解
     *
     * @param o
     */
    private static void analyzeMethods(Object o, RequestSpecification request) {
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            analyzeAnnotation(o, method, request);
        }
    }
}
