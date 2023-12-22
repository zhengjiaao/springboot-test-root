package com.zja.util;

import org.junit.Test;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/12/20 19:20
 */
public class CommonsCompressZipUtilTest {

    @Test
    public void file_test() {
        // 压缩文件
        CommonsCompressZipUtil.zip("D:\\temp\\zip\\测试目录2\\新建文本文档.txt", "D:\\temp\\zip\\测试目录2\\新建文本文档.txt.zip");
        CommonsCompressZipUtil.unzip("D:\\temp\\zip\\测试目录2\\新建文本文档.txt.zip", "D:\\temp\\zip\\测试目录2\\新建文本文档2.txt");
    }

    @Test
    public void directory_test() {
        // 压缩目录
        // CommonsCompressZipUtil.zip("D:\\temp\\zip\\测试目录", "D:\\temp\\zip\\测试目录.zip");
        CommonsCompressZipUtil.unzip("D:\\temp\\zip\\测试目录.zip", "D:\\temp\\zip\\测试目录zip");

        // GBK 无法自动识别编码，解压乱码，会报错
        // String zipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106.zip";
        // String unzipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106";
        // CommonsCompressZipUtil.unzip(zipFilePath, unzipFilePath);
    }

    @Test
    public void test3() throws IOException {
        CommonsCompressZipUtil.zip("D:\\beijing\\gdb\\test\\530102城镇开发边界管理.gdb", "D:\\beijing\\gdb\\test\\530102城镇开发边界管理CommonsCompress4.gdb.zip");
    }
}
