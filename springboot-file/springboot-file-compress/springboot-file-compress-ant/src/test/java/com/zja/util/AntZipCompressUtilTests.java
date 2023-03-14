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

}
