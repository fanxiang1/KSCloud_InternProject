package com.ksyun.train.reflect;

import static com.ksyun.train.reflect.GetClassDemo.Type.A;

/**
 * @author: sunjinfu
 * @date: 2023/6/15 9:53
 */
public class GetClassDemo {

    public static void main(String[] args) {

        // Object.getClass()方式
        String s = new String();
        Class stringClazz = s.getClass();
        System.out.println(stringClazz);

        System.out.println(A.getClass());

        byte[] bytes = new byte[]{};
        System.out.println(bytes.getClass().toString());

        // .class 方式
        Class c = java.lang.Integer.class;
        System.out.println(c);

//        boolean b;
//        Class c = b.getClass(); // 编译错误
        Class clazz = boolean.class;
        clazz = java.lang.Integer.class;

        // Class.forName方式
        try {
            clazz = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public enum Type {
        A, B;
    }
}
