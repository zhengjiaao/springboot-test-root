package com.dist.image;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 引入jar：commons-codec
 *
 * @program: springbootdemo
 * @Date: 2019/1/25 10:35
 * @Author: Mr.Zheng
 * @Description:
 */
public class ImageTest {

    /**
     * MD5是单向加密的，不可逆，可以加密，不能解密
     * md5hex是可逆的，可以加密，可以解密
     * 根据md5hex值判断图片是否是同一张，每个图片都有md5hex的唯一值
     */
    @Test
    public void testMD5(){
        try {
            String md5Hex = DigestUtils.md5Hex(new FileInputStream("D:\\qr-code-vcard.png"));
            System.out.println("md5Hex===="+md5Hex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
