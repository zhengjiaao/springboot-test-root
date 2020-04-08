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
 * @Description: 根据md5hex值判断图片是否是同一张，每个文件都有md5hex的唯一值
 */
public class ImageTest {

    /**
     * MD5是单向加密的，不可逆，可以加密，不能解密
     * md5hex是可逆的，可以加密，可以解密
     * 根据md5hex值判断图片是否是同一张，每个图片都有md5hex的唯一值
     * 如果两个文件都没有改动则文件是相同的。
     */
    @Test
    public void testMD5(){
        try {
            String md5Hex = DigestUtils.md5Hex(new FileInputStream("D:\\aa.txt"));
            System.out.println("md5Hex===="+md5Hex);
            //md5Hex====47bce5c74f589f4867dbd57e9ca9f808
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
