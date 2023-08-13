package com.ksyun.train.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 17:21
 */
public class GenericsCompareDemo {

    public static void main(String[] args) {
        // 类型转换， 编译器Unchecked警告
        List list = new ArrayList();
        list.add("hello");
        String s = (String) list.get(0);

        // 使用泛型后
        List<String> list2 = new ArrayList<>();
        list2.add("hello");
        String s2 = list2.get(0);   // 无须类型转换
    }

}
