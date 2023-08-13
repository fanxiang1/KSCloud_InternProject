package com.ksyun.train.generics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: sunjinfu
 * @date: 2023/6/7 20:05
 */
public abstract class ParameterizedTypeReference<T> {

    private Type type;

    protected ParameterizedTypeReference() {
        System.out.println(this.getClass());
        Type type = this.getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Type must be a parameterized type");
        }
        Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypes.length > 1) {
            throw new IllegalArgumentException("Number of type arguments must be 1");
        }
        this.type = actualTypes[0];
    }

    public Type getType() {
        return type;
    }
}
