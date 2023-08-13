package com.ksyun.train.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 16:39
 */
public class TestLengthLimitAnnotation {

    public static void main(String[] args) throws IllegalAccessException {
        Container container = new Container();
        container.setImage("http://hub.kce.ksyun.com/ksyun/nginx:v1.0");
        container.setName("2345436523456543456754323456543234565434565434");
        // 获取class对象
        Class clazz = container.getClass();
        // 获取对象所有字段
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return;
        }
        // 检查字段长度是否符合范围
        for (Field f : fields) {
            checkFieldLength(f, container);
        }
    }

    private static void checkFieldLength(Field field, Object obj) throws IllegalAccessException {
        if (Objects.isNull(field)) {
            return;
        }
        // 获取字段上标记的注解LengthLimitAnnotation
        LengthLimitAnnotation limitAnnotation = field.getDeclaredAnnotation(LengthLimitAnnotation.class);
        // 没有注解，表示该字段无须进行内容长度检查
        if (Objects.isNull(limitAnnotation)) {
            return;
        }
        // 如果私有属性你，强制访问
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
        // 获取字段值
        String value = (String) field.get(obj);
        int valueLen = 0;
        if (Objects.nonNull(value) && value.length() > 0) {
            valueLen = value.length();
        }
        // 获取字段上注解约束的范围长度
        int max = limitAnnotation.max();
        int min = limitAnnotation.min();
        String name = field.getName();
        // 不满足要求，抛出运行时异常
        if (valueLen > max) {
            throw new RuntimeException(name + " length can't exceed " + max);
        }
        if (valueLen < min) {
            throw new RuntimeException(name + " length can't less than " + min);
        }
    }
}
