package com.ksyun.train.generics;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 17:35
 */
public class D <T extends A & B & C> {
    private T data;

    public T getData() {
        return this.data;
    }
}
