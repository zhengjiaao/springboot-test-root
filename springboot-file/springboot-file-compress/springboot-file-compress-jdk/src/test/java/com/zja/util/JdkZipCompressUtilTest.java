package com.zja.util;

import org.junit.Test;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/12/20 18:55
 */
public class JdkZipCompressUtilTest {

    @Test
    public void test() throws IOException {
        // 压缩文件
        // JdkZipCompressUtil.zipFile("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试文件.txt");
        // JdkZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        // 压缩文件夹
//        JdkZipCompressUtil.zipFolder("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试目录");
//        JdkZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        // GBK 无法自动识别编码，解压乱码，会报错
        // String zipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106.zip";
        // String unzipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106";
        // JdkZipCompressUtil.unzip(zipFilePath, unzipFilePath);

        JdkZipCompressUtil.zip("D:\\beijing\\gdb\\test\\530102城镇开发边界管理.gdb", "D:\\beijing\\gdb\\test\\530102城镇开发边界管理jdk.gdb.zip");
    }
}
