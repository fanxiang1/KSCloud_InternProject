package com.ksyun.train.reflect.constractor;

/**
 * @author: sunjinfu
 * @date: 2023/6/30 16:57
 */
public class Student {

    private String name;

    private int age;

    // 私有无参构造方法
    private Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return "name=" + name + ",age=" + age;
    }
}
