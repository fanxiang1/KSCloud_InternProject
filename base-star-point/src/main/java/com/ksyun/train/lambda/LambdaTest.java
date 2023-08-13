package com.ksyun.train.lambda;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author: sunjinfu
 * @date: 2023/6/16 16:27
 */
public class LambdaTest {

    public static void main(String[] args) {
//        Consumer<String> consumer = e -> System.out.println("hello," + e);
//        consumer.accept("ksyun");
        Supplier<String> uuidSupplier = () -> UUID.randomUUID().toString();
        System.out.println(uuidSupplier.get());

        Function<Integer, Integer> halfFunc = e -> e/2;
        System.out.println(halfFunc.apply(10));

        Function<Integer, String> echoFunc = e -> "你好，输入的数字为: " + e;
        System.out.println(echoFunc.apply(10));

        Function<Integer, Boolean> f = e -> e > 0;
        Predicate<Integer> p = e -> e > 0;
        System.out.println(f.apply(10));
        System.out.println(p.test(10));

        BiFunction<Integer, Integer, Integer> sumFunc = (a, b) -> a * b;
        IntFunction<Double> doubleFunc = e -> Double.valueOf(e);
        System.out.println(sumFunc.apply(2, 3));
        System.out.println(doubleFunc.apply(2));

        Consumer<String> consumer = e -> LambdaTest.println(e);
    //    Consumer<String> consumer = LambdaTest::println;
        consumer.accept("ksyun");

        Supplier<ArrayList> listSupplier = ArrayList::new;

        ArrayList<String> list = new ArrayList<>();
        Function<String, Boolean> addFunc = list::add;

    }

    public void equals(Integer a) {
        Function<Integer, Boolean> equalsFunc = super::equals;
    }

    public Integer sum(Integer a) {
        return a / 2;
    }

    private static void println(String name) {
        System.out.println("hello, " + name);
    }
}
