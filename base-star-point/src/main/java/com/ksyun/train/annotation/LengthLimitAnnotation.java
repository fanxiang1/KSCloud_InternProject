package com.ksyun.train.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 16:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface LengthLimitAnnotation {
    int max() default 1000;
    int min() default 0;
}
