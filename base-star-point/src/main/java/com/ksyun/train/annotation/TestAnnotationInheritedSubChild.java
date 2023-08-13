package com.ksyun.train.annotation;

import java.lang.annotation.Annotation;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 14:51
 */
public class TestAnnotationInheritedSubChild extends TestAnnotationInheritedChild {

    public static void main(String[] args) {
        // 通过.class语法获取Class对象
        Class clazz = TestAnnotationInheritedSubChild.class;
        // 获取注解，包括从父类继承的注解
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                System.out.println(annotation.toString());
            }
        }
    }
}
