package com.zja.java.function;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自定义函数 示例
 *
 * @author: zhengja
 * @since: 2024/03/11 11:25
 */
public class FunctionCustomizeExample {


    // 数据验证：自定义函数可以用于对数据进行验证。例如，验证字符串是否为有效的邮箱地址
    // 流程：
    // 1.创建了一个自定义函数emailValidationFunction，它使用正则表达式验证字符串是否为有效的邮箱地址
    // 2.使用apply方法将字符串test@example.com应用于函数，得到验证结果true
    @Test
    public void test_1() {
        // 创建一个函数，验证字符串是否为有效的邮箱地址
        Function<String, Boolean> emailValidationFunction = email -> email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+");

        // 应用函数进行数据验证
        String email = "test@example.com";
        boolean isValidEmail = emailValidationFunction.apply(email);

        System.out.println("邮箱验证结果: " + isValidEmail);
    }

    // 数据处理：自定义函数可以用于对数据进行处理和转换。例如，将字符串中的单词转换为大写
    // 流程：
    // 1.创建了一个自定义函数wordToUpperFunction，它将字符串中的单词转换为大写
    // 2.使用split方法将句子拆分成单词数组，然后将每个单词转换为大写形式，并使用join方法将单词重新组合成句子
    // 3.将字符串"hello world"应用于函数，得到处理结果"HELLO WORLD"
    @Test
    public void test_2() {
        // 创建一个函数，将字符串中的单词转换为大写
        Function<String, String> wordToUpperFunction = sentence -> {
            String[] words = sentence.split(" ");
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].toUpperCase();
            }
            return String.join(" ", words);
        };

        // 应用函数进行数据处理
        String sentence = "hello world";
        String processedSentence = wordToUpperFunction.apply(sentence);

        System.out.println("处理结果: " + processedSentence);
    }

    // 数据映射：自定义函数可以用于对数据进行映射转换。例如，将实体对象映射为DTO（数据传输对象）
    @Test
    public void test_3() {
        // 创建一个函数，将Person对象映射为PersonDTO对象
        Function<Person, PersonDTO> personToDTOFunction = person -> new PersonDTO(person.getName(), person.getAge());

        // 创建一个Person对象
        Person person = new Person("Alice", 25);

        // 应用函数进行数据映射
        PersonDTO personDTO = personToDTOFunction.apply(person);

        System.out.println("映射结果: " + personDTO);
    }

    // 数据过滤：自定义函数可以用于过滤数据集。例如，过滤出年龄大于等于18岁的人
    // 流程：
    // 1.创建了一个自定义函数ageFilterFunction，它根据年龄是否大于等于18进行判断
    // 2.使用stream和filter方法将函数应用于数据集，过滤出年龄大于等于18岁的人
    @Test
    public void test_4() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 17));
        people.add(new Person("Charlie", 30));

        // 创建一个函数，根据年龄是否大于等于18进行判断
        Function<Person, Boolean> ageFilterFunction = person -> person.getAge() >= 18;

        // 使用函数过滤数据
        List<Person> adults = people.stream()
                .filter(ageFilterFunction::apply)
                .collect(Collectors.toList());

        System.out.println("成年人: " + adults);
    }

    // 数据转换：自定义函数可以用于将数据从一种形式转换为另一种形式。例如，将字符串列表转换为整数列表
    // 流程:
    // 1.创建了一个自定义函数stringToIntegerFunction，它将字符串转换为整数
    // 2.使用stream和map方法将函数应用于字符串列表，将每个字符串转换为对应的整数
    @Test
    public void test_5() {
        List<String> stringList = Arrays.asList("1", "2", "3", "4", "5");

        // 创建一个函数，将字符串转换为整数
        Function<String, Integer> stringToIntegerFunction = Integer::parseInt;

        // 使用函数进行数据转换
        List<Integer> integerList = stringList.stream()
                .map(stringToIntegerFunction)
                .collect(Collectors.toList());

        System.out.println("整数列表: " + integerList);
    }

    // 异常处理：自定义函数可以用于处理异常情况。例如，将字符串转换为整数时处理可能的异常情况
    // 流程：
    // 1.创建了一个自定义函数stringToIntegerFunction，它将字符串转换为整数，并在转换过程中处理可能的异常情况
    // 2.使用try-catch块捕获NumberFormatException异常，并在捕获到异常时打印错误消息
    @Test
    public void test_6() {
        String number = "123";

        // 创建一个函数，将字符串转换为整数并处理可能的异常
        Function<String, Integer> stringToIntegerFunction = str -> {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                System.out.println("无效的数字格式");
                return null;
            }
        };

        // 应用函数进行数据转换和异常处理
        Integer result = stringToIntegerFunction.apply(number);

        System.out.println("转换结果: " + result);
    }

    // 数据排序：自定义函数可以用于定义排序规则。例如，根据人的年龄进行排序
    // 流程：
    // 1.创建了一个自定义函数ageExtractorFunction，它从Person对象中提取年龄
    // 2.使用Comparator.comparing方法和函数定义了排序规则，根据年龄将人进行排序
    @Test
    public void test_7() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 17));
        people.add(new Person("Charlie", 30));

        // 创建一个函数，根据年龄进行排序
        Function<Person, Integer> ageExtractorFunction = Person::getAge;

        // 使用函数定义排序规则
        people.sort(Comparator.comparing(ageExtractorFunction));

        System.out.println("排序结果: " + people);
    }

    // 数据聚合：自定义函数可以用于数据聚合操作，例如对数字列表进行求和、平均值等操作。以下是一个求和的示例
    // 流程：
    // 1.创建了一个自定义函数sumFunction，它接收一个整数列表并返回它们的总和
    // 2.使用stream、mapToInt和sum方法将函数应用于数字列表，计算出它们的总和
    @Test
    public void test_8() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 创建一个函数，用于求和
        Function<List<Integer>, Integer> sumFunction = list -> list.stream().mapToInt(Integer::intValue).sum();

        // 使用函数进行数据聚合
        int sum = sumFunction.apply(numbers);

        System.out.println("求和结果: " + sum);
    }

    // 缓存策略：自定义函数可以用于实现缓存策略，以提高数据访问的性能。例如，实现一个缓存函数，将函数的计算结果缓存起来以避免重复计算
    // 流程：
    // 1.创建了一个自定义函数cachedFunction，它将字符串转换为整数并使用缓存来存储计算结果
    // 2.在第一次调用时，函数会执行计算并将结果放入缓存中。在后续的调用中，函数会直接从缓存中获取结果，避免重复计算
    @Test
    public void test_9() {
        Map<String, Integer> cache = new HashMap<>();

        // 创建一个函数，将字符串转换为整数并使用缓存
        Function<String, Integer> cachedFunction = str -> {
            if (cache.containsKey(str)) {
                System.out.println("从缓存中获取结果");
                return cache.get(str);
            } else {
                System.out.println("执行计算");
                int result = Integer.parseInt(str);
                cache.put(str, result);
                return result;
            }
        };

        // 第一次调用，执行计算
        int result1 = cachedFunction.apply("123");
        System.out.println("结果1: " + result1);

        // 第二次调用，从缓存中获取结果
        int result2 = cachedFunction.apply("123");
        System.out.println("结果2: " + result2);
    }

    @Data
    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        // 省略构造函数和getter/setter
    }

    @Data
    static class PersonDTO {
        private String name;
        private int age;

        public PersonDTO(String name, int age) {
            this.name = name;
            this.age = age;
        }
        // 省略构造函数和getter/setter
    }
}

