package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author: zhengja
 * @since: 2024/03/08 16:17
 */
public class OptionalUserExample {

    @Test
    public void test() {
        // Optional<User> optionalUser = getUserById(1);
        Optional<User> optionalUser = Optional.empty();

        optionalUser.ifPresent(user -> {
            String name = user.getName();
            System.out.println("User name: " + name);

            Optional<String> optionalEmail = user.getEmail();
            optionalEmail.ifPresent(email -> System.out.println("User email: " + email));

            Optional<Address> optionalAddress = user.getAddress();
            optionalAddress.ifPresent(address -> System.out.println("User address: " + address.getFullAddress()));
        });
    }

    // 检查用户是否存在并执行相应操作
    @Test
    public void test_1() {
        Optional<User> optionalUser = getUserById(1);
        optionalUser.ifPresent(user -> {
            // 执行操作
            System.out.println("User exists!");
        });
    }

    // 获取用户的名称，如果不存在则使用默认值
    @Test
    public void test_2() {
        Optional<User> optionalUser = getUserById(1);
        String username = optionalUser.map(User::getName)
                .orElse("Unknown");
        System.out.println("Username: " + username);
    }

    // 获取用户的邮箱，如果不存在则抛出异常
    @Test
    public void test_3() {
        Optional<User> optionalUser = getUserById(1);
        String email = optionalUser.flatMap(User::getEmail)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        System.out.println("Email: " + email);
    }

    // 检查用户是否有地址并打印完整地址
    @Test
    public void test_4() {
        Optional<User> optionalUser = getUserById(1);
        optionalUser.flatMap(User::getAddress)
                .ifPresent(address -> System.out.println("Full address: " + address.getFullAddress()));

        // or 处理可能为空的用户地址

        Optional<User> optionalUser2 = getUserById(2);
        Optional<Address> optionalAddress = optionalUser2.flatMap(User::getAddress);
        optionalAddress.ifPresent(address -> System.out.println("User address: " + address.getFullAddress()));
    }

    // 获取用户的角色列表，如果不存在则返回空列表
    @Test
    public void test_5() {
        Optional<User> optionalUser = getUserById(1);
        List<Role> roles = optionalUser.map(User::getRoles)
                .orElse(Collections.emptyList());
        roles.forEach(role -> System.out.println("Role: " + role.getName()));

        // or 处理可能为空的用户角色列表

        Optional<User> optionalUser2 = getUserById(1);
        Optional<List<Role>> optionalRoles = optionalUser2.map(User::getRoles);
        optionalRoles.ifPresent(roles2 -> roles2.forEach(role -> System.out.println("User role: " + role.getName())));
    }

    // 链式操作，处理多级属性访问
    @Test
    public void test_6() {
        Optional<User> optionalUser = getUserById(1);
        Optional<String> optionalCity = optionalUser.flatMap(User::getAddress)
                .map(Address::getCity);
        optionalCity.ifPresent(city -> System.out.println("User city: " + city));
    }

    // 判断用户是否满足特定条件
    @Test
    public void test_7() {
        Optional<User> optionalUser = getUserById(1);
        boolean isAdult = optionalUser.filter(user -> user.getAge() >= 18)
                .isPresent();
        System.out.println("Is adult: " + isAdult);
    }

    // 获取用户的地址的国家，如果不存在则返回默认国家
    @Test
    public void test_8() {
        Optional<User> optionalUser = getUserById(1);
        String country = optionalUser.flatMap(User::getAddress)
                .map(Address::getCountry)
                .orElse("Default Country");
        System.out.println("Country: " + country);
    }

    // 使用用户的姓名创建一个新的字符串，如果不存在则返回空字符串
    @Test
    public void test_9() {
        Optional<User> optionalUser = getUserById(1);
        String fullName = optionalUser.map(User::getName)
                .map(name -> "Mr/Ms " + name)
                .orElse("");
        System.out.println("Full name: " + fullName);
    }

    // 对用户进行转换操作，将用户对象转换为DTO对象
    @Test
    public void test_10() {
        Optional<User> optionalUser = getUserById(1);
        Optional<UserDTO> optionalUserDTO = optionalUser.map(user -> new UserDTO(user.getId(), user.getName()));
        optionalUserDTO.ifPresent(userDTO -> System.out.println("User DTO: " + userDTO));
    }

    // 使用Optional处理用户的一组电话号码，如果电话号码为空，则跳过
    @Test
    public void test_11() {
        Optional<User> optionalUser = getUserById(2);
        optionalUser.flatMap(User::getPhoneNumbers)
                .ifPresent(phoneNumbers -> phoneNumbers.forEach(System.out::println));
    }

    // 检查用户是否有特定角色
    @Test
    public void test_12() {
        Optional<User> optionalUser = getUserById(1);
        boolean hasAdminRole = optionalUser.map(User::getRoles)
                .map(roles -> roles.stream().anyMatch(role -> role.equals("Admin")))
                .orElse(false);
        System.out.println("Has admin role: " + hasAdminRole);
    }


    public static Optional<User> getUserById(int userId) {
        // 模拟从数据库或其他数据源获取User对象
        if (userId == 1) {
            Address address = new Address("123 Main St", "City", "Country");
            User user = new User(1, "John", 20, "john@example.com", address);
            return Optional.of(user);
        }

        // 模拟从数据库或其他数据源获取User对象
        if (userId == 2) {
            List<String> phoneNumbers = Arrays.asList("1234567890", "9876543210");
            User user = new User(2, "John Doe", phoneNumbers);
            return Optional.of(user);
        }

        // 模拟从数据库或其他数据源获取User对象
        if (userId == 3) {
            Role role = new Role("李四");
            Role roleAdmin = new Role("Admin");
            List<Role> roles = Arrays.asList(role, roleAdmin);
            User user = new User(3, roles);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    static class User {
        private int id;
        private String name;
        private int age;
        private String email;
        private Address address;
        private List<Role> roles;
        private List<String> phoneNumbers;

        public User(int id, String name, int age, String email, Address address) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.email = email;
            this.address = address;
        }

        public User(int id, String name, List<String> phoneNumbers) {
            this.id = id;
            this.name = name;
            this.phoneNumbers = phoneNumbers;
        }

        public User(int id, List<Role> roles) {
            this.id = id;
            this.roles = roles;
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

        public Optional<String> getEmail() {
            return Optional.ofNullable(email);
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public List<Role> getRoles() {
            return roles;
        }

        public Optional<List<String>> getPhoneNumbers() {
            return Optional.ofNullable(phoneNumbers);
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

    static class UserDTO {
        private int id;
        private String name;

        public UserDTO(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
