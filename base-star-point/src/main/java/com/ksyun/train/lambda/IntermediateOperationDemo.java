package com.ksyun.train.lambda;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 18:35
 */
public class IntermediateOperationDemo {

    public static void main(String[] args) {
        List<Student> list = Student.mockStudents();
        // [1] 过滤filter
        // 查找分数大于等于80分并且爱好不为空的学生
        list.stream()
                .filter(e -> e.getScore() >= 80 && e.getHobby() != null && e.getHobby().size() > 0)
                .forEach(System.out::println);
        // 也可以多个filter，逻辑简单的多个过滤条件尽量写在一个filter中
        list.stream()
                .filter(e -> e.getScore() >= 80)
                .filter(e -> e.getHobby() != null && e.getHobby().size() > 0)
                .forEach(System.out::println);

        // [2] 一对一数据转换map
        list.stream().map(Student::getScore).map(e -> e > 60).forEach(System.out::println);

        // [3] 一对多数据转换flatMap
        // 输出所有学生的爱好，flatMap用于将一个元素转换成拥有多个数据的流
        list.stream()
                .filter(e -> Objects.nonNull(e.getHobby()))
                .flatMap(e -> e.getHobby().stream())
                .forEach(System.out::println);

        // [4] 排序sort
        // 指定Comparator排序，输出 王五 张三 小月月 李四
        list.stream()
                .sorted(Comparator.comparing(Student::getScore))
                .forEach(System.out::println);
        // 指定Comparator倒序排序， 输出 李四 小月月 张三 王五
        list.stream()
                .sorted(Comparator.comparing(Student::getScore).reversed())
                .forEach(System.out::println);
        // 根据Integer实现的排序规则进行排序，输出 55 80 88 90
        list.stream()
                .map(Student::getScore).sorted()
                .forEach(System.out::println);
        // 根据Integer实现的排序规则进行倒序排序，输出 90 88 80 55
        list.stream()
                .map(Student::getScore).sorted(Comparator.reverseOrder())
                .forEach(System.out::println);

        // [5] 去重distinct
        // 学生爱好可能重叠，打印去重后所有爱好，distinct判断逻辑取决于数据类型的equals方法
        list.stream()
                .filter(e -> Objects.nonNull(e.getHobby()))
                .flatMap(e -> e.getHobby().stream())
                .distinct()
                .forEach(System.out::println);

        // [6] 跳过skip, 如果跳过的数据数量超过数据元素总数，则不会有任何输出，用于取后n个数据
        list.stream().skip(3).forEach(System.out::println); // 输出 王五

        // [7] 限制数据数量limit, 如果限制数大于等于数据元素总数，则无任何作用，用于取前n个数据
        list.stream().limit(2).forEach(System.out::println);

        // [8] 消费数据peek, 参数实际是一个Consumer函数式接口，通过peek可以额外操作一次数据，经常用于打印调试
        list.stream()
                .filter(e -> e.getScore() > 80)
                .peek(System.out::println)
                .map(Student::getName)
                .peek(System.out::println)
                .forEach(System.out::println);

        // [9] - 转换成整型数据流mapToInt, 将Stream转换成IntStream后，可以做一些统计操作，如sum、count等
        // 学生的分数总和
        long totalScore = list.stream()
                .filter(e -> e.getScore() != null)
                .mapToInt(Student::getScore)
                .sum();
        System.out.println(totalScore);

    }
}
