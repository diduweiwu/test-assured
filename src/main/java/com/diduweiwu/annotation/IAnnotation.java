package com.diduweiwu.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IAnnotation {
}
