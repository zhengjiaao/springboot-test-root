package com.zja.mvc.web.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * ContextPath 操作
 *
 * @Author: zhengja
 * @Date: 2024-11-07 9:54
 */
@Slf4j
public class ContextPathUtil {

    private ContextPathUtil() {
    }

    /**
     * 获取上下文根路径
     *
     * @param request HttpServletRequest
     * @return String 示例：http://localhost:8080/context-path
     */
    public static String getContextPathURL(HttpServletRequest request) {
        if (null == request) {
            throw new IllegalArgumentException("请求的【HttpServletRequest】为空.");
        }

        // 获取端口，并忽略默认端口80和443
        String port = (80 == request.getServerPort() || 443 == request.getServerPort()) ? "" : (":" + request.getServerPort());

        // 获取https，被nginx代理情况（如nginx配置了X-Forwarded-Scheme或者X-Forwarded-Proto）
        String scheme = getValidHeader(request, "X-Forwarded-Scheme", "X-Forwarded-Proto");
        if (StringUtils.isEmpty(scheme)) {
            scheme = request.getScheme();
        }

        return scheme + "://" + request.getServerName() + port + request.getContextPath();
    }

    private static String getValidHeader(HttpServletRequest request, String... headerNames) {
        for (String headerName : headerNames) {
            String headerValue = request.getHeader(headerName);
            if (StringUtils.hasText(headerValue) && (headerValue.equalsIgnoreCase("http") || headerValue.equalsIgnoreCase("https"))) {
                return headerValue;
            }
        }
        return null;
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取上下文根路径
     *
     * @return String 示例：http://localhost:8080/context-path
     */
    public static String getContextPathURL() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            log.warn("未获取到HttpServletRequest对象，请检查是否在SpringMVC环境下调用该方法，推荐启动应用.");
            return "http://localhost:8080";
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return getContextPathURL(request);
    }

    /**
     * 获取api路径：拼接上下文根路径
     *
     * @param path 路径
     * @return String 示例：http://localhost:8080/context-path/path
     */
    public static String getPathURL(String path) {
        String contextPathURL = getContextPathURL();

        if (StringUtils.isEmpty(contextPathURL)) {
            throw new IllegalStateException("Context path URL is not set.");
        }

        if (StringUtils.isEmpty(path)) {
            return contextPathURL;
        }

        // 清理路径，防止注入攻击
        path = cleanPath(path);

        // 拼接api路径
        String apiPath = contextPathURL + path;

        return apiPath;
    }

    private static String cleanPath(String path) {
        // 替换多个斜杠为单个斜杠
        path = path.replaceAll("[/\\\\]+", "/");

        // 确保路径以斜杠开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // 确保路径不以斜杠结尾
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    /**
     * 获取临时目录路径（用完后请及时清理目录）
     *
     * @return String 示例，/context-path/temp/temp-dir
     */
    public static String getTempDirectory() throws IOException {
        // 固定的临时目录名称
        String fixedDirName = "temp";

        // 获取当前项目根目录
        Path baseDir = getBaseDirectory();
        Path tempDir = Files.createDirectories(baseDir.resolve(fixedDirName));
        // 随机生成子目录名称，避免目录冲突
        String subDirName = UUID.randomUUID().toString();
        Path subTempDir = Files.createDirectory(tempDir.resolve(subDirName));
        return subTempDir.toString();
    }

    private static Path getBaseDirectory() throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String contextPath = request.getServletContext().getRealPath("");
            if (contextPath == null) {
                throw new IOException("Failed to get the real path of the context.");
            }
            return Paths.get(contextPath);
        } else {
            // 方便本地未启动服务时进行测试
            String userDir = System.getProperty("user.dir");
            if (userDir == null) {
                throw new IOException("Failed to get the user directory.");
            }
            return Paths.get(userDir);
        }
    }

    /**
     * 获取临时文件路径（用完后请及时清理文件）
     *
     * @param fileName 文件名称
     * @return String 示例，/context-path/temp/temp-dir/temp-file-name.txt
     */
    public static String getTempFilePath(String fileName) throws IOException {
        String tempDirectory = getTempDirectory();
        return Paths.get(tempDirectory, fileName).toString();
    }

    /**
     * 删除临时文件
     *
     * @param filePath 文件路径
     */
    public static void deleteTempFilePath(String filePath) {
        try {
            Path path = Paths.get(filePath);

            if (Files.isDirectory(path)) {
                try (Stream<Path> stream = Files.walk(path)) {
                    stream.sorted(java.util.Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            log.warn("Failed to delete file: {}", file.getAbsolutePath());
                        }
                    });
                }
            }

            if (Files.isRegularFile(path)) {
                Files.deleteIfExists(path);
            }

            Path parent = path.getParent();
            if (parent != null) {
                try (Stream<Path> stream = Files.list(parent)) {
                    long count = stream.count();
                    if (count == 0) {
                        Files.deleteIfExists(parent);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println(getContextPathURL());
        System.out.println(getPathURL("/api/v1/report"));
        System.out.println(getPathURL("api/v1/report"));
        System.out.println(getPathURL("api/v1/report/"));
        System.out.println(getPathURL("\\api\\v1\\report\\"));

        String tempDirectory = getTempDirectory();
        String tempFilePath = getTempFilePath("test.txt");
        System.out.println(tempDirectory);
        System.out.println(tempFilePath);

        deleteTempFilePath(tempDirectory);
        deleteTempFilePath(tempFilePath);

        System.out.println(System.getProperty("user.home")); // 用户主目录，示例 C:\Users\\username
        System.out.println(System.getProperty("user.dir")); // 当前项目根目录 示例 D:\workspace\springboot-test-root
        System.out.println(System.getProperty("java.io.tmpdir")); // 临时文件目录 示例 C:\Users\\username\AppData\Local\Temp\
    }
}
