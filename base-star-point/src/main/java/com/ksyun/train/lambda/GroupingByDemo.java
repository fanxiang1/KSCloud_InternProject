package com.ksyun.train.lambda;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 19:01
 */
public class GroupingByDemo {

    public static void main(String[] args) {
        List<Student> list = Student.mockStudents();

        // [1] 按性别进行分组
        Map<String, List<Student>> genderGroup = list.stream().collect(Collectors.groupingBy(Student::getGender));
        System.out.println(genderGroup.toString()); // 女=[小月月], 男=[张三, 李四, 李四]

        // [2] 按性别分组统计男女学生人数
        Map<String, Long> genderCountGroup = list.stream()
                .collect(Collectors.groupingBy(Student::getGender, Collectors.counting()));
        System.out.println(genderCountGroup.toString()); // 女=1, 男=3

        // [3] 按性别分组统计男女学生分数总和
        Map<String, Integer> sumScoreMap = list.stream()
                .collect(Collectors.groupingBy(Student::getGender, Collectors.summingInt(Student::getScore)));
        System.out.println(genderCountGroup.toString()); // 女=88, 男=225

        // [4] 按性别分组查找分数最高的学生
        Map<String, Optional<Student>> maxScoreMap = list.stream()
                .collect(Collectors.groupingBy(Student::getGender,
                        Collectors.maxBy(Comparator.comparing(Student::getScore))));
        maxScoreMap.forEach((k, v) -> System.out.println(k + "=" + v.orElse(null))); // // 女=小月月 男=李四

        // [5] 按性别分组，查找爱好最多的学生
        Map<String, Student> studentMap = list.stream()
                .filter(e -> e.getHobby() != null)
                .collect(Collectors.groupingBy(Student::getGender,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(e -> e.getHobby().size())), Optional::get)));
        System.out.println(studentMap);

        // [6] 按条件分割数据partitioningBy
        // partitioningBy只会产生两条数据，因为其分割数据的参数是一个Predicate函数式接口
        Map<Boolean, List<Student>> partitionMap = list.stream()
                .collect(Collectors.partitioningBy(e -> e.getScore() >= 60));
        System.out.println(partitionMap.toString()); // false=[王五], true=[张三, 李四, 小月月]

        partitionMap = list.stream()
                .collect(Collectors.partitioningBy(e -> e.getScore() >= 0));
        System.out.println(partitionMap.toString()); // false=[], true=[张三, 李四, 小月月, 王五]
    }
}
