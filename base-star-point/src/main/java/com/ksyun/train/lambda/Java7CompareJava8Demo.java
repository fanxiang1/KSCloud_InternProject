package com.ksyun.train.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 18:19
 */
public class Java7CompareJava8Demo {

    public static void main(String[] args) {
        List<Student> list = Student.mockStudents();

        // Java7 实现
        List<Student> boys = new ArrayList<>();
        // 先根据性别过滤
        for (Student student : list) {
            if (Objects.equals(student.getGender(), "男")) {
                boys.add(student);
            }
        }
        // 根据分数倒序排序
        Collections.sort(boys, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getScore().compareTo(o2.getScore());
            }
        }.reversed());

        // 获取名称
        List<String> boyNames = new ArrayList<>();
        for (Student student : boys) {
            boyNames.add(student.getName());
        }
        System.out.println(boyNames);

        // Java8实现
         boyNames = list.stream()
                .filter(e -> Objects.equals(e.getGender(), "男")) // 根据性别过滤
                .sorted(Comparator.comparing(Student::getScore).reversed()) // 按分数倒序排序
                .map(Student::getName) // 获取姓名
                .collect(Collectors.toList());
        System.out.println(boyNames);
    }
}
