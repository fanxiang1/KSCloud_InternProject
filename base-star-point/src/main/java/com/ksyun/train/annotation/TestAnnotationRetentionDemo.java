package com.ksyun.train.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author: sunjinfu
 * @date: 2023/6/30 19:19
 */
public class TestAnnotationRetentionDemo {

    @RetentionSourceAnnotation
    @RetentionClassAnnotation
    @RetentionRuntimeAnnotation
    private String name;

    public static void main(String[] args) throws NoSuchFieldException {
        // 通过反射获取字段信息
        Field name = TestAnnotationRetentionDemo.class.getDeclaredField("name");
        // 获取作用于name字段上的全部注解信息
        Annotation[] annotations = name.getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                System.out.println(annotation.toString());
            }
        }

    }
}
