/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 13:42
 * @Since:
 */
package com.zja.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author: zhengja
 * @since: 2023/10/16 13:42
 */
@Slf4j
public class HttpUtilTest {
    @Test
    public void test_get() {
        // 最简单的HTTP请求，可以自动通过header等信息判断编码，不区分HTTP和HTTPS
        String result1 = HttpUtil.get("https://www.baidu.com");

        // 当无法识别页面编码的时候，可以自定义请求页面的编码
        String result2 = HttpUtil.get("https://www.baidu.com", CharsetUtil.CHARSET_UTF_8);

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("city", "北京");

        String result3 = HttpUtil.get("https://www.baidu.com", paramMap);
    }

    @Test
    public void test_post() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("city", "北京");

        String result = HttpUtil.post("https://www.baidu.com", paramMap);
    }


    @Test
    public void test_downloadFile() {
        String fileUrl = "https://gdal.org/gdal.pdf";

        //将文件下载后保存在E盘，返回结果为下载文件大小
        long size = HttpUtil.downloadFile(fileUrl, FileUtil.file("d:/"));
        System.out.println("Download size: " + size);
    }

    @Test
    public void test_downloadFileV2() {
        String fileUrl = "https://gdal.org/gdal.pdf";

        //带进度显示的文件下载
        HttpUtil.downloadFile(fileUrl, FileUtil.file("d:/"), new StreamProgress() {
            @Override
            public void start() {
                Console.log("开始下载。。。。");
            }

            //下载进度
            @Override
            public void progress(long l, long l1) {
                log.info("文件大小={},当前已下载大小={}", l, l1);
            }

            @Override
            public void finish() {
                Console.log("下载完成！");
            }
        });
    }
}
