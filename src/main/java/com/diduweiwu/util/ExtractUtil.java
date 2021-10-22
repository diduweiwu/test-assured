package com.diduweiwu.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 处理器公用方法
 */
@NoArgsConstructor
public class ExtractUtil {

    /**
     * 解析对象字段里面的属性
     *
     * @param element
     */
    public static void extractFieldValue(Object api, AnnotatedElement element, BiConsumer<String, Object> fieldValueConsumer) {
        String fieldName = StrUtil.EMPTY;
        Object fieldValue = null;
        if (element instanceof Field) {
            Field field = (Field) element;
            fieldName = field.getName();
            fieldValue = ReflectUtil.getFieldValue(api, fieldName);
        }
        // 回调传回fieldName与fieldValue用于业务逻辑处理
        fieldValueConsumer.accept(fieldName, fieldValue);
    }

    /**
     * 解析对象方法里面的属性
     *
     * @param element
     */
    public static void extractMethodValue(Object api, AnnotatedElement element, BiConsumer<String, Object> methodValueConsumer) {
        String methodName = StrUtil.EMPTY;
        Object methodValue = null;
        // 对象字段里面的属性
        if (element instanceof Method) {
            Method method = (Method) element;
            methodValue = ReflectUtil.invoke(api, method);
        }
        // 回调传回fieldName与fieldValue用于业务逻辑处理
        methodValueConsumer.accept(methodName, methodValue);
    }

    /**
     * 解析注解里面的属性
     *
     * @param element
     * @param annotationType
     * @param annotationValueConsumer
     */
    public static void extractAnnotationFieldValue(AnnotatedElement element, Class<? extends Annotation> annotationType, String fieldName, BiConsumer<String, Object> annotationValueConsumer) {
        Map<String, Object> annotationValueMap = AnnotationUtil.getAnnotationValueMap(element, annotationType);
        Object annotationValue = annotationValueMap.get(fieldName);
        annotationValueConsumer.accept(fieldName, annotationValue);
    }
}
