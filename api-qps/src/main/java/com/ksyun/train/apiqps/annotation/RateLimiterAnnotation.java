package com.ksyun.train.apiqps.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiterAnnotation {
    // unit is second
    long ttl() default 10L;

    /**
     *  允许访问次数
     */
    int maxCount() default 5;
}
