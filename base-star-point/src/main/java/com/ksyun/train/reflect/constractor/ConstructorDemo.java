package com.ksyun.train.reflect.constractor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @author: sunjinfu
 * @date: 2023/6/15 14:05
 */
public class ConstructorDemo {

    public static void main(String[] args) throws Exception {
//        // 得到Student的Class对象
//        // 还未讲泛型 Class<Student> clazz = Student.class;
//        Class clazz = Student.class;
//        // 获取无参构造方法，如果没有无参构造方法，则抛出NoSuchMethodException
//        Constructor<Student> constructor = clazz.getDeclaredConstructor();
//        // 获取指定参数类型的构造方法
//        constructor = clazz.getDeclaredConstructor(String.class, int.class);
//        // 获取公开的无参构造方法
//        constructor = clazz.getConstructor(); // 运行错误， 抛出NoSuchMethodException


//        Class clazz = Student.class;
//        Constructor[] constructors = clazz.getDeclaredConstructors();
//        for (Constructor constructor : constructors) {
//            // 方法修饰符 1-public 2-private 4-protected ,更多值参考java.lang.reflect.Modifier
//            int modifiers = constructor.getModifiers();
//            // 获取构造方法的参数个数
//            int parameterCount = constructor.getParameterCount();
//            // 获取构造方法的参数类型列表
//            Class<?>[] parameterTypes = constructor.getParameterTypes();
//            System.out.println(constructor.toString());
//        }


        // 获取无参构造方法实例化Student对象
        Class<Student> clazz = Student.class;
        Student student = null;
        // 获取无参构造方法
        Constructor<Student> constructor = clazz.getDeclaredConstructor();
        if (Objects.isNull(constructor)) {
            throw new RuntimeException("not found no args constructor");
        }
        // 构造方法是private修饰符，则禁用安全检查开关，否则访问时会抛出IllegalAccessException
        if (!Modifier.isPublic(constructor.getModifiers())) {
            constructor.setAccessible(true);
        }
        // 实例化对象
        student = constructor.newInstance();
        System.out.println(student.toString());

        // 使用有参构造函数构造对象
        constructor = clazz.getDeclaredConstructor(String.class, int.class);
        student = constructor.newInstance("ksyun", 1);
        System.out.println(student.toString()); // name=ksyun,age=1

        // 通过Class的newInstance()方法实例化对象，该方法默认使用public类型的无参构造方法，非public或者没有无参构造方法，会抛异常
//        Class<Student> clazz = Student.class;
//        Student student = clazz.newInstance();
//        System.out.println(student.toString());
    }

}
