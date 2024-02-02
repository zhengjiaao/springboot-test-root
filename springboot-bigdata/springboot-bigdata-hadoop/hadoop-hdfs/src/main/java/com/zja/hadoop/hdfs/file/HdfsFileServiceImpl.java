/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-04-17 9:52
 * @Since:
 */
package com.zja.hadoop.hdfs.file;

import com.zja.hadoop.hdfs.file.FileProperties;
import com.zja.hadoop.hdfs.file.FileService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

/**
 * @author: zhengja
 * @since: 2023/04/17 9:52
 */
@Slf4j
public class HdfsFileServiceImpl implements FileService {

    private final FileSystem fileSystem;

    private final String path;

    public HdfsFileServiceImpl(FileProperties.HdfsProperties properties) throws Exception {
        checkConnect(properties);

        this.fileSystem = FileSystem.get(new URI(properties.getUri()), new Configuration(), properties.getUsername());
        this.path = properties.getPath();
    }

    @Override
    public FileSystem fileSystem() {
        return this.fileSystem;
    }

    @Override
    public void uploadObject(String objectName, MultipartFile file) throws Exception {
        try (FSDataOutputStream outputStream = fileSystem.create(new Path(path + "/" + objectName))) {
            IOUtils.copy(file.getInputStream(), outputStream);
        }
    }

    @Override
    public void uploadObject(String objectName, String filename) throws Exception {
        Path localPath = new Path(filename);
        Path hdfsPath = new Path(path + "/" + objectName);
        fileSystem.copyFromLocalFile(localPath, hdfsPath);
    }

    @Override
    public String uploadObjectAsync(String objectName, String filename) {
        CompletableFuture.runAsync(() -> {
            try {
                uploadObject(objectName, filename);
            } catch (Exception e) {
                log.error("上传文件到 HDFS 失败: " + e.getMessage());
            }
        });
        return "上传文件到 HDFS 中...";
    }

    @Override
    public void downloadObject(String objectName, String filename) throws Exception {
        try (FSDataInputStream inputStream = fileSystem.open(new Path(path + "/" + objectName))) {
            IOUtils.copy(inputStream, Files.newOutputStream(Paths.get(filename)));
        }
    }

    @Override
    public InputStream getObjectInputStream(String objectName) throws Exception {
        Path hdfsPath = new Path(path + "/" + objectName);
        return fileSystem.open(hdfsPath);
    }

    @Override
    public boolean existObject(String objectName) throws Exception {
        Path hdfsPath = new Path(path + "/" + objectName);
        return fileSystem.exists(hdfsPath);
    }

    @Override
    public void deleteObject(String objectName) throws Exception {
        Path hdfsPath = new Path(path + "/" + objectName);
        fileSystem.delete(hdfsPath, false);
    }

    private void checkConnect(FileProperties.HdfsProperties properties) throws Exception {
        try (val fs = FileSystem.get(new URI(properties.getUri()), new Configuration(), properties.getUsername())) {
            boolean exists = fs.exists(new Path(properties.getPath()));
            if (!exists) {
                fileSystem.mkdirs(new Path(path));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("获取hdfs FileSystem发生系统错误,错误诊断: " + e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("获取hdfs url解析错误, 请检查! 错误诊断: " + e);
        }
    }
}
