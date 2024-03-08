package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: zhengja
 * @since: 2024/03/08 17:36
 */
public class OptionalListUserExample {

    // 从可能为空的List<User>中获取满足条件的第一个用户
    @Test
    public void test_1() {
        List<User> userList = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30),
                new User(3, "Charlie", 35)
        );

        Optional<User> optionalUser = userList.stream()
                .filter(user -> user.getName().startsWith("B"))
                .findFirst();
        optionalUser.ifPresent(user -> System.out.println("User starting with 'B': " + user.getName()));
    }

    // 使用Optional获取列表中年龄最大的用户
    @Test
    public void test_2() {
        List<User> userList = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30),
                new User(3, "Charlie", 35)
        );

        Optional<User> optionalMaxAgeUser = userList.stream()
                .max(Comparator.comparingInt(User::getAge));
        optionalMaxAgeUser.ifPresent(user -> System.out.println("User with max age: " + user.getName()));
    }

    // 使用Optional结合flatMap处理嵌套的List<User>结构
    @Test
    public void test_3() {
        List<List<User>> nestedList = Arrays.asList(
                Arrays.asList(
                        new User(1, "Alice", 25),
                        new User(2, "Bob", 30)
                ),
                Arrays.asList(
                        new User(3, "Charlie", 35),
                        new User(4, "David", 40)
                )
        );

        Optional<User> optionalUser = nestedList.stream()
                .flatMap(List::stream)
                .filter(user -> user.getName().startsWith("D"))
                .findFirst();
        optionalUser.ifPresent(user -> System.out.println("User starting with 'D': " + user.getName()));
    }

    // 从可能为空的List<User>中获取所有年龄大于等于30的用户
    @Test
    public void test_4() {
        List<User> userList = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30),
                new User(3, "Charlie", 35)
        );

        List<User> filteredUsers = userList.stream()
                .filter(user -> user.getAge() >= 30)
                .collect(Collectors.toList());

        filteredUsers.forEach(user -> System.out.println("User with age >= 30: " + user.getName()));
    }

    // 使用Optional获取列表中的第三个用户
    @Test
    public void test_5() {
        List<User> userList = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30),
                new User(3, "Charlie", 35)
        );

        Optional<User> optionalThirdUser = userList.stream()
                .skip(2)
                .findFirst();
        optionalThirdUser.ifPresent(user -> System.out.println("Third user: " + user.getName()));
    }

    // 使用Optional结合flatMap处理嵌套的List<User>结构并获取所有用户的名称
    @Test
    public void test_6() {
        List<List<User>> nestedList = Arrays.asList(
                Arrays.asList(
                        new User(1, "Alice", 25),
                        new User(2, "Bob", 30)
                ),
                Arrays.asList(
                        new User(3, "Charlie", 35),
                        new User(4, "David", 40)
                )
        );

        List<String> userNames = nestedList.stream()
                .flatMap(List::stream)
                .map(User::getName)
                .collect(Collectors.toList());

        userNames.forEach(name -> System.out.println("User name: " + name));
    }

    // 使用Optional结合flatMap处理嵌套的对象列表
    @Test
    public void test_7() {
        List<User> userList = Arrays.asList(
                new User(1, "Alice", 25),
                new User(2, "Bob", 30),
                new User(3, "Charlie", 35)
        );

        Optional<String> optionalUserName = userList.stream()
                .filter(user -> user.getId() == 2)
                .findFirst()
                .map(User::getName);
        optionalUserName.ifPresent(name -> System.out.println("User name: " + name));
    }

    static class User {
        private int id;
        private String name;
        private int age;

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
    }
}
