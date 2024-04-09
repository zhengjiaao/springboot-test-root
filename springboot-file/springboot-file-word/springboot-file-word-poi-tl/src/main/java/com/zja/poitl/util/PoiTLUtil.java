package com.zja.poitl.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 根据word模版生成word文档
 *
 * @author: zhengja
 * @since: 2024/04/02 10:13
 */
public class PoiTLUtil {

    private PoiTLUtil() {
    }

    /**
     * 根据word模版生成word文档
     *
     * @param wordPath word生成后的存路径
     * @param data     数据
     * @param template 模版路径，在resources下面的模版
     * @throws IOException
     */
    public static void generateWord(String wordPath, Map<String, Object> data, String template) throws IOException {
        File file = new File(wordPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(false);

        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(getResourceAsStream(template),builder.build()).render(data);
        xwpfTemplate.writeAndClose(Files.newOutputStream(Paths.get(wordPath)));
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

}
