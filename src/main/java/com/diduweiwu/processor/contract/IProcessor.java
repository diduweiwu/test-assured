package com.diduweiwu.processor.contract;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.diduweiwu.util.ExtractUtil;
import io.restassured.specification.RequestSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * 处理器
 */
public interface IProcessor {
    /**
     * 解析处理value
     *
     * @param api
     * @param element
     * @param annotationType
     * @param valueConsumer
     */
    default void executeSingValue(Object api, AnnotatedElement element, Class<? extends Annotation> annotationType, ValueConsumer valueConsumer) {
        valueConsumer.execute(executeValue(api, element, annotationType));
    }

    /**
     * 解析value
     *
     * @param api
     * @param element
     * @param annotationType
     * @return
     */
    default Object executeValue(Object api, AnnotatedElement element, Class<? extends Annotation> annotationType) {
        // 引用类型来保存value值
        final AtomicReference<Object> atmoicValue = new AtomicReference<>();
        // 对象字段里面的属性
        ExtractUtil.extractFieldValue(api, element, (fieldKey, fieldValue) -> {
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                atmoicValue.set(fieldValue);
            }
        });

        // 对象方法里面的属性
        ExtractUtil.extractMethodValue(api, element, (methodKey, methodValue) -> {
            if (ObjectUtil.isNotEmpty(methodValue)) {
                atmoicValue.set(methodValue);
            }
        });

        // 注解里面配置的属性有最高优先级,所以注解里面配置了对应属性,则以注解属性为准
        ExtractUtil.extractAnnotationFieldValue(element, annotationType, "value", (annotationKey, annotationValue) -> {
            if (ObjectUtil.isNotEmpty(annotationValue)) {
                atmoicValue.set(annotationValue);
            }
        });

        Object value = atmoicValue.get();
        String annotationName = annotationType.getName();
        Assert.notNull(value, "{} 的value配置错误,请检查", annotationName);
        return value;
    }

    /**
     * 解析value
     *
     * @param api
     * @param element
     * @param annotationType
     * @return
     */
    default Object executeKey(Object api, AnnotatedElement element, Class<? extends Annotation> annotationType) {
        // 引用类型来保存value值
        final AtomicReference<Object> atmoicKey = new AtomicReference<>();
        // 对象字段里面的属性
        ExtractUtil.extractFieldValue(api, element, (fieldKey, fieldValue) -> {
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                atmoicKey.set(fieldKey);
            }
        });

        // 对象方法里面的属性
        ExtractUtil.extractMethodValue(api, element, (methodKey, methodValue) -> {
            if (ObjectUtil.isNotEmpty(methodValue)) {
                atmoicKey.set(methodKey);
            }
        });

        // 注解里面配置的属性有最高优先级,所以注解里面配置了对应属性,则以注解属性为准
        ExtractUtil.extractAnnotationFieldValue(element, annotationType, "key", (annotationKey, annotationValue) -> {
            // 如果注解key字段指定的值非空,则将指定的值设置为键值
            if (ObjectUtil.isNotEmpty(annotationValue)) {
                atmoicKey.set(annotationValue);
            }
        });

        Object key = atmoicKey.get();
        String annotationName = annotationType.getName();
        Assert.notNull(key, "{} 的Key配置错误,请检查", annotationName);
        return key;
    }

    /**
     * 解析key和value
     *
     * @param api
     * @param element
     * @param annotationType
     * @param keyValueConsumer
     */
    default void execute(Object api, AnnotatedElement element, Class<? extends Annotation> annotationType, BiConsumer<String, Object> keyValueConsumer) {
        Object key = executeKey(api, element, annotationType);
        Object value = executeValue(api, element, annotationType);

        // 调用具体配置逻辑设置restassured的配置
        keyValueConsumer.accept(String.valueOf(key), value);
    }

    /**
     * 默认不做任何操作
     *
     * @param api
     * @param element
     * @param clazzAnnotation
     * @param request
     */
    default void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
    }
}
