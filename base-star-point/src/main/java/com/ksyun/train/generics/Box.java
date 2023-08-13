package com.ksyun.train.generics;

/**
 * @author: sunjinfu
 * @date: 2023/6/6 17:29
 */
public class Box<T> {

    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return this.t;
    }

    public static void main(String[] args) {
        Box<Integer> box = new Box<>();
//        printBoxData(box);

        // 泛型类的继承关系
        Box<Number> box1 = new ChildBox<>();
        box1.set(10);
        printBoxData(box1);
        Box<Number> box2 = new SecondChildBox<Number, String>();
        box2.set(10);
        printBoxData(box2);
    }

    public static void printBoxData(Box<Number> box) {
        System.out.println(box.get());
    }


}
