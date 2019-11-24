package com.dist.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 文件属性
 * @author yinxp@dist.com.cn
 * @date 2019/6/25
 */
public abstract class FileAttributeUtil {

    private final static Charset CHARSET = Charset.forName("UTF-8");


    /**
     * 向文件写入自定义属性（用户自定义）
     * @param localFilePath
     * @param attrs
     */
    public static void saveUserAttributes(String localFilePath, Map<String, String> attrs) throws IOException {
        if (!new File(localFilePath).exists()) {
            throw new RuntimeException("file is not exist");
        }
        Path path = Paths.get(localFilePath);
        UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            view.write(entry.getKey(), CHARSET.encode(entry.getValue()));
        }
    }


    /**
     * 从文件读取自定义属性（用户自定义）
     * @param localFilePath
     * @return
     * @throws IOException
     */
    public static Map<String, String> getUserAttributes(String localFilePath) throws IOException {
        if (!new File(localFilePath).exists()) {
            throw new RuntimeException("file is not exist");
        }
        Map<String, String> map = new HashMap<>();
        Path path = Paths.get(localFilePath);
        UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        List<String> metaKeys = view.list();
        for (String metaKey : metaKeys) {
            ByteBuffer buff = ByteBuffer.allocate(view.size(metaKey));
            view.read(metaKey, buff);
            buff.flip();
            map.put(metaKey, CHARSET.decode(buff).toString());
        }
        return map;
    }


    /**
     * 从文件读取基本元数据（文件固有）
     * @param localFilePath
     * @return
     * @throws IOException
     */
    public Map<String, String> getBasicAttributes(String localFilePath) throws IOException {
        if (!new File(localFilePath).exists()) {
            throw new RuntimeException("file is not exist");
        }
        Map<String, String> map = new HashMap<>();
        Path path = Paths.get(localFilePath);
        Set<String> keys = Files.readAttributes(path, "*").keySet();
        for (String attr : keys) {
            map.put(attr, Files.getAttribute(path, attr).toString());
        }
        return map;
    }


    /**
     * 查看系统支持的AttributeViews
     * @param localFilePath
     */
    public static void supportedWhichViews(String localFilePath) {
        Path path = Paths.get(localFilePath);
        FileSystem fileSystem = path.getFileSystem();
        Set<String> supportedViews = fileSystem.supportedFileAttributeViews();
        for (String view : supportedViews) {
            System.out.println(view);
        }
    }
}
