package com.zja.hanbian.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-09-20 17:39
 */
@Service
public class UserManagementService {
    private Map<String, String> usernamePasswordMap;

    public UserManagementService() {
        usernamePasswordMap = new HashMap<>();
    }

    public void addUser(String username, String password) {
        usernamePasswordMap.put(username, password);
        System.out.println("User added successfully: " + username);
    }

    public void deleteUser(String username) {
        usernamePasswordMap.remove(username);
        System.out.println("User deleted successfully: " + username);
    }

    public boolean validateUser(String username, String password) {
        if (usernamePasswordMap.containsKey(username) && usernamePasswordMap.get(username).equals(password)) {
            System.out.println("User validated successfully: " + username);
            return true;
        } else {
            System.out.println("User validation failed: " + username);
            return false;
        }
    }

    public static void main(String[] args) {
        UserManagementService service = new UserManagementService();
        service.addUser("JohnDoe", "password123");
        service.addUser("JaneSmith", "securepass");

        service.validateUser("JohnDoe", "password123");
        service.validateUser("JaneSmith", "wrongpassword");

        service.deleteUser("JohnDoe");
    }
}
