package com.ksyun.train.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 18:31
 */
public class GetStreamDemo {

    public static void main(String[] args) {

        // 从集合类获取
        List<String> list = Arrays.asList("a", "b", "c");
        list.stream();
        // 并行处理Stream，适用于有大量计算任务的场景使用， 而非IO密集型任务，底层采用Fork/Join线程池框架
        // 分而治之的思想，parallelStream并不一定比stream处理更快，请勿滥用，IO任务避免使用
        list.parallelStream();

        // 从数组获取
        String[] array = new String[]{"a", "b", "c"};
        Arrays.stream(array);

        // Stream静态方法获取
        String[] array2 = new String[]{"a", "b", "c"};
        Stream.of(array);
        Stream.of("a", "b", "c");
        // 从Supplier函数式接口获取
        Stream.generate(ArrayList::new);
        // 迭代
        Stream.iterate(0, e -> e + 1).limit(10).forEach(System.out::println); //输出 0 ~ 9

        // 从java.util.stream包中提供的流接口获取, LongStream、DoubleStream等
        IntStream.rangeClosed(0, 10).forEach(System.out::println); // 输出了 0 ~ 10

        // Random获取数据流，获取10个随机数字(0 ~ 99)
        new Random().ints(0, 100).limit(10).forEach(System.out::println);
    }
}
