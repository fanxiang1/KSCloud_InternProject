package com.ksyun.train.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 18:15
 */
public class Student {
    // 学号
    private Integer no;
    // 姓名
    private String name;
    // 分数
    private Integer score;
    // 性别  男  女
    private String gender;
    // 爱好
    private List<String> hobby;

    public Student(Integer no, String name, Integer score, String gender, List<String> hobby) {
        this.no = no;
        this.name = name;
        this.score = score;
        this.gender = gender;
        this.hobby = hobby;
    }

    // 方便测试，只打印名字
    public String toString() {
        return this.name;
    }

    public static List<Student> mockStudents() {
        Student zhangSan = new Student(1, "张三", 80, "男", Arrays.asList("football", "basketball"));
        Student liSi = new Student(2, "李四", 90, "男", null);
        Student xiaoYue = new Student(3, "小月月", 88, "女", Arrays.asList("dancer", "sing"));
        Student wangWu = new Student(4, "王五", 55, "男", Arrays.asList("game"));
        List<Student> list = new ArrayList<>(Arrays.asList(zhangSan, liSi, xiaoYue, wangWu));
        return list;
    }

    public Integer getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getHobby() {
        return hobby;
    }
}
