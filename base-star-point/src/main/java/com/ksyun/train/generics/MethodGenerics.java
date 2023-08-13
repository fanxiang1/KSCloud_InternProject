package com.ksyun.train.generics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: sunjinfu
 * @date: 2023/6/6 16:40
 */
public class MethodGenerics {

    private Box<? extends Number> ok;
    // 实例方法
    public <K, V> V getData(K key, V value) {
        return value;
    }

    // 构造方法
    public <T extends Number> MethodGenerics(T data) {

    }

    public static double getBoxData(Box<? extends Number> box) {
        return box.get().doubleValue();
    }


    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }

    public static <E> void append(List<E> list, Class<E> cls) throws Exception {
        E elem = cls.newInstance();   // OK
        list.add(elem);
    }

    public static void main(String[] args) throws Exception {
        List<String> data = new ArrayList<>();
        data.add("a");
        Class clz = data.getClass();
        Method addMethod = clz.getMethod("add", Object.class);
        addMethod.invoke(data, 10);
        for (Object e : data) {
            System.out.println(e);
        }
    }

}
