/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 15:14
 * @Since:
 */
package com.zja.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 命令行执行工具
 *
 * @author: zhengja
 * @since: 2023/11/03 15:14
 */
@Slf4j
public class PaddleOCRCommandUtil {

    private PaddleOCRCommandUtil() {

    }

    /**
     * 执行命令
     */
    public static StringBuilder command(String command) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec(command);
        // 读取命令输出
        StringBuilder stringBuilder = readCommandOutput(process.getInputStream());

        // 等待命令执行完成
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            // 命令执行失败
            String errorMessage = "命令：" + command + "\n" +
                    "命令执行错误：" +
                    getErrorMessage(process.getErrorStream());

            if (!errorMessage.contains("'NoneType' object is not iterable")){
                throw new RuntimeException(errorMessage);
            }
        }

        return stringBuilder;
    }

    // 读取命令输出
    private static StringBuilder readCommandOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"))) {

            String line;
            boolean first = false;
            StringBuilder result = new StringBuilder();
            result.append("[");
            while ((line = reader.readLine()) != null) {
                log.info(line);
                String data = extractData(line);
                if (Boolean.FALSE == first && isNotEmpty(data)) {
                    first = true;
                    continue;
                }
                if (Boolean.TRUE == first && isNotEmpty(data)) {
                    result.append(data).append(",");
                }
            }
            if (result.length() > 2) {
                result.deleteCharAt(result.length() - 1);
            }
            result.append("]");

            return result;
        }
    }

    private static String extractData(String content) {
        if (isEmpty(content)) {
            return null;
        }

        int startIndex = content.indexOf("ppocr INFO: ");
        if (startIndex != -1) {
            String data = content.substring(startIndex + "ppocr INFO: ".length());
            return toJsonData(data);
        }

        return null;
    }

    private static String toJsonData(String data) {
        String rep = StringUtils.replace(data, ", (", ", [");
        String rep2 = StringUtils.replace(rep, ", ['", ", [\"");
        String rep3 = StringUtils.replace(rep2, "',", "\",");
        return StringUtils.replace(rep3, ")]", "]]");
    }

    private static StringBuilder getErrorMessage(InputStream errorStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {

            String line;
            StringBuilder errorMessage = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                errorMessage.append(line).append("\n");
            }

            return errorMessage;
        }
    }

    private static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    private static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

}
