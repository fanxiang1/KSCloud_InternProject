package com.ksyun.train.lambda;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 18:56
 */
public class ListConvertToMap {

    public static void main(String[] args) {
        List<Student> list = Student.mockStudents();
        // key=name, value=score
        Map<String, Integer> nameScoreMap = list.stream()
                .collect(Collectors.toMap(Student::getName, Student::getScore));
        // key=name, value=student object
        Map<String, Student> studentMap = list.stream()
                .collect(Collectors.toMap(Student::getName, e -> e));

        // Function函数式接口提供了一个静态方法identity，始终返回它的输入值
        // key=name, value=student object
        studentMap = list.stream()
                .collect(Collectors.toMap(Student::getName, Function.identity()));

        // 使用Collectors.toMap时需要注意的是，当key已经存在时，会直接报错 Duplicate key，
        // 根据场景，给它一个指定的merge策略，如后者覆盖前者数据
        // key=name, value=student object
        studentMap = list.stream()
                .collect(Collectors.toMap(Student::getName, Function.identity(),
                        (oldValue, newValue) -> newValue));

        // Collectors.toMap默认生成的是HashMap，当我们需要有固定顺序的LinkedHashMap时可以提供一个Supplier作为参数
        studentMap = list.stream()
                .collect(Collectors.toMap(Student::getName, Function.identity(),
                        (oldValue, newValue) -> newValue, LinkedHashMap::new));

    }
}
