package com.ksyun.train.reflect.method;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author: sunjinfu
 * @date: 2023/6/15 17:56
 */
public class MethodDemo {

    public static void main(String[] args) throws Exception {
//        Class<Student> clazz = Student.class;
//        Method[] methods = clazz.getDeclaredMethods();
//        for (Method method : methods) {
//            String name = method.getName();
//            int modifiers = method.getModifiers();
//            int parameterCount = method.getParameterCount();
//            Class<?>[] parameterTypes = method.getParameterTypes();
//            Class<?> returnType = method.getReturnType();
//            Class<?>[] exceptionTypes = method.getExceptionTypes();
//        }

        // 通过反射调用私有setScore方法给字段score赋值
        Student student = new Student();
        System.out.println("反射前score值: " + student.getScore());
        Class clazz = student.getClass();
        // 通过反射获取字段信息
        Field field = clazz.getDeclaredField("score");
        // 获取字段类型
        Class<?> type = field.getType();
        // 根据参数类型获取setScore方法
        Method setScore = clazz.getDeclaredMethod("setScore", type);
        if (!Modifier.isPublic(setScore.getModifiers())) {
            setScore.setAccessible(true);
        }
        setScore.invoke(student, 100);
        System.out.println("反射前score值: " + student.getScore());

        // 通过反射调用静态方法
        Method printlnMethod = clazz.getDeclaredMethod("println", String.class);
        printlnMethod.invoke(null, "ksyun");
    }
}
