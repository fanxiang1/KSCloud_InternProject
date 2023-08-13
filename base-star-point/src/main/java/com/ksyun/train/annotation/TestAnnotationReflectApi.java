package com.ksyun.train.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 15:42
 */
public class TestAnnotationReflectApi {

    public static void main(String[] args) throws NoSuchMethodException {
        // 通过.class获取Class对象
        Class clazz = TaskSchedule.class;
        // 获取autoCleanResource方法信息
        Method method = clazz.getDeclaredMethod("autoCleanResource");
        System.out.println("====getDeclaredAnnotations 打印开始");
        Annotation[] annotations = method.getDeclaredAnnotations();
        print(annotations);
        System.out.println("====getDeclaredAnnotations 打印结束");
        System.out.println("====getAnnotation 开始打印");
        Schedule schedule = method.getAnnotation(Schedule.class);
        if (Objects.nonNull(schedule)) {
            System.out.println(schedule.toString());
        }
        System.out.println("====getAnnotation 打印结束");
        annotations = method.getAnnotationsByType(Schedule.class);
        System.out.println("====getAnnotationsByType 打印开始");
        print(annotations);
        System.out.println("====getAnnotationsByType 打印结束");
    }

    private static void print(Annotation[] annotations) {
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                System.out.println(annotation.toString());
            }
        }
    }
}
