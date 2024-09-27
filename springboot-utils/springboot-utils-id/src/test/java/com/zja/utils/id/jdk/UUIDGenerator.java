package com.zja.utils.id.jdk;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @Author: zhengja
 * @Date: 2024-09-14 15:21
 */
public class UUIDGenerator {

    @Test
    public void randomUUID_test() {
        // 生成随机的 UUID 对象
        UUID uuid = UUID.randomUUID();
        System.out.println("生成的随机 UUID 为：" + uuid.toString());
        // d304e8b0-350c-434c-919c-e7cc4420b87b
        // f4c3445b-b8cd-456a-9661-66112dd9558a

//        String uuidString = "9912d36c-1fbc-ef62-8bcf-ec92a1a26313";
//        UUID uuid = UUID.fromString(uuidString);

        // 截取前面部分
        String frontPart = uuid.toString().substring(0, 8);
        System.out.println("前面部分: " + frontPart);

        // 截取后面部分,相对于前面的部分，唯一性更高。
        String backPart = uuid.toString().substring(25);
        System.out.println("后面部分: " + backPart);
    }

    @Test
    public void uuid_test() {
        // 通过给定的字符串名称生成 UUID 对象
        UUID uuid = UUID.nameUUIDFromBytes("example_name".getBytes());
        System.out.println("生成的 UUID 为：" + uuid.toString());
        // dc05e22d-59eb-37f3-b01d-9739c8ec7e5a
        // dc05e22d-59eb-37f3-b01d-9739c8ec7e5a
    }

    @Test
    public void SecureRandom_uuid_test() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        // 将生成的随机字节数组转换为 UUID
        UUID uuid = toUUID(randomBytes);
        System.out.println("生成的 UUID 为：" + uuid.toString());
        // 8aec7004-d333-0087-4e1f-934bc32eacb2
        // 9912d36c-1fbc-ef62-8bcf-ec92a1a26313
    }

    private static UUID toUUID(byte[] bytes) {
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (bytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }

}
