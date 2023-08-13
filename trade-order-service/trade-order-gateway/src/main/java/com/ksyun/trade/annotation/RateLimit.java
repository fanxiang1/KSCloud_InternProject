package com.ksyun.trade.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    String key() default "";

    @AliasFor("key")
    String value() default "";

    long limitCount();

    long seconds();
}
