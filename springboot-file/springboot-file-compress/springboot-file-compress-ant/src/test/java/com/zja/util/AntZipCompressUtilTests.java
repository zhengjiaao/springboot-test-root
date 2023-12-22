/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 10:32
 * @Since:
 */
package com.zja.util;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AntZipCompressUtilTests {

    //压缩
    @Test
    public void test1() {
        AntZipCompressUtil.zip("D:\\temp\\zip\\测试目录", "D:\\temp\\zip\\存储目录\\a.zip");
    }

    //解压
    @Test
    public void test2() {
        AntZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\a.zip", "D:\\temp\\zip\\存储目录\\a");
    }

    @Test
    public void test3() {
        // GBK 无法自动识别编码，解压乱码，不会报错
        String zipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106.zip";
        String unzipFilePath = "D:\\zhejiang\\331083玉环市城镇开发边界管理成果20231106";
        AntZipCompressUtil.unzip(zipFilePath, unzipFilePath);
    }

    @Test
    public void test4() {
        AntZipCompressUtil.zip("D:\\beijing\\gdb\\test\\530102城镇开发边界管理.gdb", "D:\\beijing\\gdb\\test\\530102城镇开发边界管理Ant.gdb.zip");
    }


}
