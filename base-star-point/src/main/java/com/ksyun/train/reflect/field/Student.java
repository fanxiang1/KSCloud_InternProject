package com.ksyun.train.reflect.field;

/**
 * @author: sunjinfu
 * @date: 2023/6/30 16:51
 */
public class Student extends Person {
    // 私有类型字段
    private int score;
    // 只提供了get方法
    public int getScore() {
        return this.score;
    }
}
