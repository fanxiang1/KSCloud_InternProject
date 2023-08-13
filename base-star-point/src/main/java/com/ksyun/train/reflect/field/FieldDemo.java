package com.ksyun.train.reflect.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author: sunjinfu
 * @date: 2023/6/15 16:07
 */
public class FieldDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        Class clazz = Student.class;
//        // 获取类直接声明的所有字段
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            // 字段名称
//            String name = field.getName();
//            // 访问修饰符 1-public 2-private 4-protected
//            int modifiers = field.getModifiers();
//            // 字段类型
//            Class<?> type = field.getType();
//            // 输出: name: score, modifier: 2, type: int
//            System.out.println("name: " + name+ ", modifier: " + modifiers + ", type: " + type.toString());
//        }
//
//        // 获取类所有的public类型字段，包括从父类继承的字段
//        fields = clazz.getFields();
//        for (Field field : fields) {
//            String name = field.getName();
//            int modifiers = field.getModifiers();
//            Class<?> type = field.getType();
//            // 输出: name: name, modifier: 1, type: class java.lang.String
//            System.out.println("name: " + name + ", modifier: " + modifiers + ", type: " + type.toString());
//        }


        // 通过反射绕过检查，强制给Student对象的private字段score赋值
        Student student = new Student();
        System.out.println("反射前score属性值: " + student.getScore()); // 输出默认值0
        // student.score = 100; // 编译错误，private字段无法直接访问赋值
        // 通过反射获取score字段，不存在抛出NoSuchFieldException
        Field score = student.getClass().getDeclaredField("score");
        // private字段，直接访问会抛出IllegalAccessException
        if (!Modifier.isPublic(score.getModifiers())) {
            // 强制访问
            score.setAccessible(true);
        }
        // 通过Field对象的set方法给student对象中的score字段赋值100
        score.set(student, 100);
        System.out.println("反射后score属性值: " + student.getScore()); // 输出100
    }
}
