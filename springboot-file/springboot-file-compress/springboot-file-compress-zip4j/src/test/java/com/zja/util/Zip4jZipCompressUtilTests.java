/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 10:33
 * @Since:
 */
package com.zja.util;

import net.lingala.zip4j.exception.ZipException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Zip4jZipCompressUtilTests {

    // 解压缩文件
    @Test
    public void test() throws ZipException {
        // 解压文件
        Zip4jZipCompressUtil.zip("D:\\temp\\zip\\存储目录\\测试文件.zip", "D:\\temp\\zip\\测试文件.txt");
        Zip4jZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\测试文件.zip", "D:\\temp\\zip\\存储目录");
    }

    // 解压缩文件夹
    @Test
    public void test2() throws ZipException {
        // 解压文件夹
        // Zip4jZipCompressUtil.zip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试目录");
        // Zip4jZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        // GBK 无法自动识别编码，解压乱码，不会报错
        String zipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106.zip";
        String unzipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106";
        Zip4jZipCompressUtil.unzip(zipFilePath, unzipFilePath);
    }

    @Test
    public void test3() throws ZipException {
        Zip4jZipCompressUtil.zip("D:\\beijing\\gdb\\test\\530102城镇开发边界管理.gdb", "D:\\beijing\\gdb\\test\\530102城镇开发边界管理Zip4j.gdb.zip");
    }

}
