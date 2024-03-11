package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author: zhengja
 * @since: 2024/03/08 16:18
 */
public class OptionalExample {

    // 避免空指针异常
    @Test
    public void test_1() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        optionalValue.ifPresent(value -> System.out.println("Value: " + value));
    }

    // 安全地访问对象的属性
    @Test
    public void test_2() {
        Optional<User> optionalUser = Optional.ofNullable(getUserById(1).get());
        optionalUser.map(User::getName)
                .ifPresent(name -> System.out.println("User name: " + name));
    }

    // 链式操作，避免嵌套的null检查
    @Test
    public void test_3() {
        Optional<Company> optionalCompany = Optional.ofNullable(getCompany());
        String departmentName = optionalCompany.flatMap(Company::getDepartment)
                .flatMap(Department::getName)
                .orElse("Unknown");
        System.out.println("Department name: " + departmentName);
    }

    public Company getCompany() {
        // 假设这是一个获取公司对象的方法
        // 如果公司存在，则返回一个包含公司对象的 Optional
        // 如果公司不存在，则返回一个空的 Optional
        return new Company("Example Company");
    }

    class Company {
        private Department department;

        public Company(String name) {
            this.department = new Department(name + " Department");
        }

        public Optional<Department> getDepartment() {
            return Optional.ofNullable(department);
        }
    }

    class Department {
        private String name;

        public Department(String name) {
            this.name = name;
        }

        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }
    }

    // 过滤、查找满足条件的元素
    @Test
    public void test_4() {
        List<Integer> numbers = Arrays.asList(1, 3, 5, 2, 4, 6);
        Optional<Integer> optionalNumber = numbers.stream()
                .filter(num -> num % 2 == 0)
                .findFirst();
        optionalNumber.ifPresent(num -> System.out.println("Even number: " + num));
    }

    // 聚合操作
    @Test
    public void test_5() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> optionalSum = numbers.stream()
                .reduce(Integer::sum);
        optionalSum.ifPresent(sum -> System.out.println("Sum: " + sum));
    }

    // 处理异常情况
    @Test
    public void test_6() {
        Optional<User> optionalUser = getUserById(1);
        try {
            User user = optionalUser.orElseThrow(UserNotFoundException::new);
            System.out.println(user.getName());
            // 处理用户对象
        } catch (UserNotFoundException e) {
            // 处理异常情况
            e.printStackTrace();
        }
    }

    class UserNotFoundException extends Exception {
        public UserNotFoundException() {
            super("User not found.");
        }
    }

    // 替代默认值
    @Test
    public void test_7() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        String result = optionalValue.orElse("Default Value");
        System.out.println("Result: " + result);
    }

    // 链式操作和转换
    @Test
    public void test_8() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        Optional<Integer> optionalLength = optionalValue.map(String::length);
        optionalLength.ifPresent(length -> System.out.println("Length: " + length));
    }

    // 使用过滤条件获取满足条件的值
    @Test
    public void test_9() {
        Optional<Integer> optionalNumber = Optional.of(10);
        Optional<Integer> filteredNumber = optionalNumber.filter(num -> num % 2 == 0);
        filteredNumber.ifPresent(num -> System.out.println("Filtered number: " + num));
    }

    // 处理集合中的任意一个元素
    @Test
    public void test_10() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Optional<String> optionalName = names.stream()
                .filter(name -> name.startsWith("C"))
                .findAny();
        optionalName.ifPresent(name -> System.out.println("Name starting with 'C': " + name));
    }

    // 自定义处理逻辑
    @Test
    public void test_11() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        if (optionalValue.isPresent()) {
            System.out.println("Value: " + optionalValue.get());
        } else {
            System.out.println("Value is empty.");
        }
    }

    // 使用Optional进行值的转换和异常处理
    @Test
    public void test12() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        try {
            Integer intValue = optionalValue.map(Integer::parseInt)
                    .orElseThrow(NumberFormatException::new);
            System.out.println("Parsed Integer value: " + intValue);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    // 链式操作和组合
    @Test
    public void test_13() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        Optional<String> modifiedValue = optionalValue.map(String::toUpperCase)
                .filter(value -> value.length() > 5);
        modifiedValue.ifPresent(value -> System.out.println("Modified value: " + value));
    }


    // 执行一系列操作直到遇到第一个非空值
    @Test
    public void test_14() {
        Optional<String> optionalValue = Stream.of(getValue1(), getValue2(), getValue3())
                .filter(Objects::nonNull)
                .findFirst();
        optionalValue.ifPresent(value -> System.out.println("First non-null value: " + value));
    }

    public static String getValue1() {
        // 模拟获取第一个值的方法
        return null;
    }

    public static String getValue2() {
        // 模拟获取第二个值的方法
        return "Hello";
    }

    public static String getValue3() {
        // 模拟获取第三个值的方法
        return "World";
    }


    // 使用Optional处理异常情况
    @Test
    public void test_15() {
        Optional<String> optionalValue = Optional.ofNullable(getValue());
        try {
            String result = optionalValue.orElseThrow(() -> new CustomException("Value is empty."));
            System.out.println("Result: " + result);
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static class CustomException extends RuntimeException {
        public CustomException(String msg) {
            super(msg);
        }
    }

    // 使用Optional进行集合操作
    @Test
    public void test_16() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        Optional<String> longestName = names.stream()
                .max(Comparator.comparingInt(String::length));
        longestName.ifPresent(name -> System.out.println("Longest name: " + name));
    }


    // 使用Optional作为方法的返回类型
    @Test
    public void test_17() {
        Optional<User> optionalUser = getUserById(1);
        optionalUser.ifPresent(user -> System.out.println("User: " + user.getName()));
    }

    public Optional<User> getUserById(int id) {
        // 根据ID查询用户
        // 如果找到用户，则返回Optional.of(user)
        // 如果未找到用户，则返回Optional.empty()

        // 模拟从数据库或其他数据源获取User对象
        if (id == 1) {
            User user = new User(1, "John", 20);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    static class User {
        private int id;
        private String name;
        private int age;
        private Address address;
        private List<Role> roles;

        public User(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public List<Role> getRoles() {
            return roles;
        }
    }

    static class Address {
        private String street;
        private String city;
        private String country;

        public Address(String street, String city, String country) {
            this.street = street;
            this.city = city;
            this.country = country;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getFullAddress() {
            return street + ", " + city + ", " + country;
        }
    }

    static class Role {
        private String name;

        public Role(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static String getValue() {
        // 假设这是一个根据某种条件获取值的方法
        boolean condition = true;
        if (condition) {
            return "Hello, World!";
        } else {
            return null;
        }
    }
}
