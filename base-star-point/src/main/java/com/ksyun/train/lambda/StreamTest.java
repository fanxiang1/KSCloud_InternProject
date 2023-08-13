package com.ksyun.train.lambda;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: sunjinfu
 * @date: 2023/6/17 14:10
 */
public class StreamTest {

    public static void main(String[] args) {
//        List<String> list = Arrays.asList("a", "b", "c");
//        list.parallelStream();
//
//        String[] array = new String[]{"a", "b", "c"};
//        Arrays.stream(array);
//        Stream.of(array);
//        Stream.of("a", "b", "c");
//
//        IntStream.rangeClosed(0, 10).forEach(System.out::println);
//        Stream.iterate(0, e -> e + 1).limit(10).forEach(System.out::println);
//        Stream.generate(ArrayList::new);
//        new Random().ints(0, 100).limit(10).forEach(System.out::println);

        List<Student> list = Student.mockStudents();
//        list.stream()
//                .filter(e -> e.getScore() >= 80 && e.getHobby() != null && e.getHobby().size() > 0)
//                .forEach(System.out::println);
//
//        list.stream()
//                .filter(e -> e.getScore() >= 80)
//                .filter(e -> e.getHobby() != null && e.getHobby().size() > 0)
//                .forEach(System.out::println);
//
//        List<Student> boys = new ArrayList<>();
//        for (Student student : list) {
//            if (Objects.equals(student.getGender(), "男")) {
//                boys.add(student);
//            }
//        }
//        Collections.sort(boys, new Comparator<Student>() {
//            @Override
//            public int compare(Student o1, Student o2) {
//                return o1.getScore().compareTo(o2.getScore());
//            }
//        }.reversed());
//
//        List<String> boyNames = new ArrayList<>();
//        for (Student student : boys) {
//            boyNames.add(student.getName());
//        }
//        System.out.println(boyNames);

//        List<String> boyNames = list.stream()
//                .filter(e -> Objects.equals(e.gender, "男"))
//                .sorted(Comparator.comparing(Student::getScore).reversed())
//                .map(Student::getName)
//                .collect(Collectors.toList());
//        System.out.println(boyNames);
//        list.stream().map(Student::getScore).map(e -> e > 60).forEach(System.out::println);
//        list.stream()
//                .filter(e -> Objects.nonNull(e.getHobby()))
//                .flatMap(e -> e.getHobby().stream())
//                .forEach(System.out::println);
//        list.stream()
//                .sorted(Comparator.comparing(Student::getScore))
//                .forEach(System.out::println);
//        list.stream()
//                .sorted(Comparator.comparing(Student::getScore).reversed())
//                .forEach(System.out::println);
//        list.stream()
//                .map(Student::getScore).sorted()
//                .forEach(System.out::println);
//        list.stream()
//                .map(Student::getScore).sorted(Comparator.reverseOrder())
//                .forEach(System.out::println);
//        list.stream()
//                .filter(e -> Objects.nonNull(e.getHobby()))
//                .flatMap(e -> e.getHobby().stream())
//                .distinct()
//                .forEach(System.out::println);
//        list.stream().skip(1).forEach(System.out::println);
//        list.stream().limit(2).forEach(System.out::println);
//        list.stream()
//                .filter(e -> e.getScore() > 80)
//                .peek(System.out::println)
//                .map(Student::getName)
//                .peek(System.out::println)
//                .forEach(System.out::println);
//        long totalScore = list.stream()
//                .filter(e -> e.getScore() != null)
//                .mapToInt(Student::getScore)
//                .sum();
//        System.out.println(totalScore);
        // key=name, value=score
//        Map<String, Integer> nameScoreMap = list.stream()
//                .collect(Collectors.toMap(Student::getName, Student::getScore));
//        // key=name, value=student object
//        Map<String, Student> studentMap = list.stream()
//                .collect(Collectors.toMap(Student::getName, e -> e));
        // key=name, value=student object
//        Map<String, Student> studentMap = list.stream()
//                .collect(Collectors.toMap(Student::getName, Function.identity(),
//                        (oldValue, newValue) -> newValue, LinkedHashMap::new));

//        Map<String, List<Student>> genderGroup = list.stream().collect(Collectors.groupingBy(Student::getGender));
//        System.out.println(genderGroup.toString());
//        Map<String, Set<Student>> genderGroup = list.stream()
//                .collect(Collectors.groupingBy(Student::getGender, Collectors.toSet()));
//        Map<String, Long> genderCountGroup = list.stream()
//                .collect(Collectors.groupingBy(Student::getGender, Collectors.counting()));
//        System.out.println(genderCountGroup.toString());
//        Map<String, Integer> genderCountGroup = list.stream()
//               .collect(Collectors.groupingBy(Student::getGender, Collectors.summingInt(Student::getScore)));
//        System.out.println(genderCountGroup.toString());
//        Map<Boolean, List<Student>> partitionMap = list.stream()
//                .collect(Collectors.partitioningBy(e -> e.getScore() >= 60));
//        System.out.println(partitionMap.toString());
//        Map<Boolean, List<Student>> partitionMap = list.stream()
//                .collect(Collectors.partitioningBy(e -> e.getScore() >= 0));
//        System.out.println(partitionMap.toString());
//        int totalHobby = list.stream().map(Student::getHobby)
//                .filter(Objects::nonNull)
//                .mapToInt(Collection::size)
//                .sum();
//        System.out.println(totalHobby);
//        int minScore = list.stream().mapToInt(Student::getScore).min().orElse(-1);
//        double avgScore = list.stream().mapToInt(Student::getScore).average().orElse(0);
//        int maxScore = list.stream().mapToInt(Student::getScore).max().orElse(-1);
//        long sumScore = list.stream().mapToInt(Student::getScore).sum();
//        System.out.println("min: " + minScore + ",max: " + maxScore + ",avg: " + avgScore + ",sum: " + sumScore);

//        long count = list.stream().map(Student::getHobby).filter(Objects::nonNull).count();
//        System.out.println(count);

//        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        int sum = 10; // 初始值 10， 从10开始累加
//        for (int i : numbers) {
//            sum += i;
//        }
//        System.out.println("sum : " + sum); // 55
//        int result = Arrays.stream(numbers).reduce(10, (a, b) -> a + b);
//        System.out.println(result);
//        result = Arrays.stream(numbers).reduce(10, (a, b) -> a + a * (b - 1));
//        System.out.println(result);
//        List<String> nameList = list.stream().map(Student::getName).collect(Collectors.toList());
//        List<String> nameList = list.stream()
//                .map(Student::getName)
//                .collect(Collectors.toCollection(LinkedList::new));
//        System.out.println(nameList);
//        Set<String> nameSet = list.stream().map(Student::getName).collect(Collectors.toSet());
//        String[] nameArray = list.stream().map(Student::getName).toArray(String[]::new);
//        Map<Integer, String> idNameMap = list.stream().collect(Collectors.toMap(Student::getNo, Student::getName));
//        Map<String, Optional<Student>> maxScoreMap = list.stream()
//                .collect(Collectors.groupingBy(Student::getGender,
//                        Collectors.maxBy(Comparator.comparing(Student::getScore))));
//        maxScoreMap.forEach((k, v) -> System.out.println(k + "=" + v.orElse(null)));
//        Map<String, Student> studentMap = list.stream()
//                .filter(e -> e.getHobby() != null)
//                .collect(Collectors.groupingBy(Student::getGender,
//                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(e -> e.getHobby().size())), Optional::get)));
//        System.out.println(studentMap);
//        boolean existNamed = list.stream().anyMatch(e -> Objects.equals("张三", e.getName()));
//        Student s = list.stream().filter(e -> Objects.equals(e.name, "张三")).findFirst().orElse(null);
//        s = list.stream().filter(e -> Objects.equals(e.name, "张三")).findAny().orElse(null);
        list.stream().filter(e -> e.getScore() > 60).map(Student::getName).collect(Collectors.toList());
        list.parallelStream();
    }
}
