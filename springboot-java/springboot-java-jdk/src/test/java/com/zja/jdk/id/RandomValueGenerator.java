package com.zja.jdk.id;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Java 生成随机值
 * 一般用途：使用 java.util.Random。
 * 安全性要求高：使用 java.security.SecureRandom。
 * 简单快速：使用 Math.random()。
 *
 * @Author: zhengja
 * @Date: 2024-09-14 15:17
 */
public class RandomValueGenerator {

    @Test
    public void Random_test() {
        Random random = new Random();

        // 生成一个 [0, 100) 范围内的随机整数
        int randomInt = random.nextInt(100);
        System.out.println("随机整数: " + randomInt);

        // 生成一个 [0.0, 1.0) 范围内的随机浮点数
        double randomDouble = random.nextDouble();
        System.out.println("随机浮点数: " + randomDouble);
    }

    @Test
    public void Math_test() {
        // 生成一个 [0.0, 1.0) 范围内的随机浮点数
        double randomDouble = Math.random();
        System.out.println("随机浮点数: " + randomDouble);

        // 生成一个 [0, 100) 范围内的随机整数
        int randomInt = (int) (Math.random() * 100);
        System.out.println("随机整数: " + randomInt);
    }

    @Test
    public void SecureRandom_test() {
        SecureRandom secureRandom = new SecureRandom();

        // 生成一个 [0, 100) 范围内的随机整数
        int randomInt = secureRandom.nextInt(100);
        System.out.println("随机整数: " + randomInt);

        // 生成一个 [0.0, 1.0) 范围内的随机浮点数
        double randomDouble = secureRandom.nextDouble();
        System.out.println("随机浮点数: " + randomDouble);
    }

}
