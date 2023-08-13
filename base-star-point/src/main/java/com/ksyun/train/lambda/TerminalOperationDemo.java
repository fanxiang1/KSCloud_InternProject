package com.ksyun.train.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 18:47
 */
public class TerminalOperationDemo {

    public static void main(String[] args) {
        List<Student> list = Student.mockStudents();

        // [1] 求最大max、最小min、平均avg值、总和
        // 最低分数值
        int minScore = list.stream().mapToInt(Student::getScore).min().orElse(-1);
        // 平均分数值
        double avgScore = list.stream().mapToInt(Student::getScore).average().orElse(0);
        // 最大分数值
        int maxScore = list.stream().mapToInt(Student::getScore).max().orElse(-1);
        // 分数总和
        long sumScore = list.stream().mapToInt(Student::getScore).sum();
        // min: 55,max: 90,avg: 78.25,sum: 313
        System.out.println("min: " + minScore + ",max: " + maxScore + ",avg: " + avgScore + ",sum: " + sumScore);

        // [2] 统计数量count
        long count = list.stream().map(Student::getHobby).filter(Objects::nonNull).count(); // 3
        System.out.println(count);

        // [3] 归纳reduce
        // Java7
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int sum = 10; // 初始值 10， 从10开始累加
        for (int i : numbers) {
            sum += i;
        }
        System.out.println("sum : " + sum); // 65

        // Java8实现
        sum = Arrays.stream(numbers).reduce(10, (a, b) -> a + b); // 65
        System.out.println(sum);
        // reduce的操作参数是一个二元函数式接口，非常适用于一些二元算术计算
        sum = Arrays.stream(numbers).reduce(1, (a, b) -> a + a * (b - 1));
        System.out.println(sum);

        // 收集操作collect
        // 转为List
        List<String> nameList = list.stream().map(Student::getName).collect(Collectors.toList());
        // 转为Set
        Set<String> nameSet = list.stream().map(Student::getName).collect(Collectors.toSet());
        // 转为Array
        String[] nameArray = list.stream().map(Student::getName).toArray(String[]::new);
        // 转为Map
        Map<Integer, String> idNameMap = list.stream().collect(Collectors.toMap(Student::getNo, Student::getName));

        // 短路操作findAny、findFirst、anyMatch
        // 查找是否有叫张三的同学
        boolean existNamed = list.stream().anyMatch(e -> Objects.equals("张三", e.getName()));
        // 按顺序查找，第一个叫张三的同学
        Student s = list.stream().filter(e -> Objects.equals(e.getName(), "张三")).findFirst().orElse(null);
        // 相比FindFirst，FindAny更适用于并行流计算中
        s = list.stream().filter(e -> Objects.equals(e.getName(), "张三")).findAny().orElse(null);
    }
}
