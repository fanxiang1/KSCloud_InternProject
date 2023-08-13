package com.ksyun.train.reflect.method;

/**
 * @author: sunjinfu
 * @date: 2023/6/30 17:02
 */
public class Student {

    private int score;

    public int getScore() {
        return this.score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    public static void println(String txt) {
        System.out.println("This is one static method, txt: " + txt);
    }
}
