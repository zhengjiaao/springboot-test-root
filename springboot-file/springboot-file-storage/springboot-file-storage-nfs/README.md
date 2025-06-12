# springboot-file-storage-nfs

### 1. NFS 简介

**NFS（Network File System）** 是一种分布式文件系统协议，允许客户端计算机像访问本地存储一样访问远程服务器上的文件。核心特点：

- **跨平台**：支持 Linux/Unix/Windows（需额外服务）
- **透明访问**：客户端通过挂载点直接操作远程文件
- **协议版本**：NFSv3（广泛兼容）、NFSv4（增强安全）
- **典型应用**：集群共享存储、容器持久化数据、多服务文件共享

------

### 2. Spring Boot 集成 NFS 完整示例

#### 环境准备

1. **NFS 服务器**（IP: 192.168.1.100）

   ```bash
   # 安装NFS服务
   sudo apt install nfs-kernel-server
   
   # 创建共享目录
   sudo mkdir /data/nfs_share
   sudo chmod 777 /data/nfs_share
   
   # 配置导出目录（/etc/exports）
   /data/nfs_share  *(rw,sync,no_subtree_check,no_root_squash)
   
   # 重启服务
   sudo systemctl restart nfs-kernel-server
   ```

2. **客户端挂载**（Spring Boot服务器）

   ```bash
   # 安装客户端工具
   sudo apt install nfs-common
   
   # 创建本地挂载点
   sudo mkdir /mnt/nfs
   
   # 挂载远程NFS
   sudo mount -t nfs 192.168.1.100:/data/nfs_share /mnt/nfs
   
   # 验证挂载
   df -h | grep nfs
   ```

#### Spring Boot 项目配置

**pom.xml 依赖**

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.11.0</version>
    </dependency>
</dependencies>
```

**application.properties**

```properties
# NFS挂载点路径
nfs.mount-path=/mnt/nfs
```

#### 文件操作服务类

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class NfsStorageService {

    @Value("${nfs.mount-path}")
    private String mountPath;

    // 保存文件到NFS
    public String saveFile(MultipartFile file) throws IOException {
        String filePath = mountPath + "/" + file.getOriginalFilename();
        File targetFile = new File(filePath);
        FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
        return filePath;
    }

    // 从NFS读取文件字节
    public byte[] readFile(String filename) throws IOException {
        Path path = Paths.get(mountPath, filename);
        return Files.readAllBytes(path);
    }

    // 删除NFS文件
    public boolean deleteFile(String filename) {
        File file = new File(mountPath + "/" + filename);
        return file.delete();
    }

    // 检查文件是否存在
    public boolean fileExists(String filename) {
        return new File(mountPath + "/" + filename).exists();
    }
}
```

#### REST 控制器

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private NfsStorageService storageService;

    // 文件上传
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = storageService.saveFile(file);
            return ResponseEntity.ok("File saved to NFS: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    // 文件下载
    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            byte[] data = storageService.readFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

------

### 3. 封装 NFS API 及 Spring Boot 使用

#### 高级封装接口

```java
import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface NfsOperations {
    void storeFile(String fileName, InputStream inputStream) throws IOException;
    byte[] fetchFile(String fileName) throws IOException;
    boolean deleteFile(String fileName);
    List<String> listFiles(String directory);
    boolean createDirectory(String path);
}
```

#### NFS 实现类

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;

@Component
public class NfsClient implements NfsOperations {

    @Value("${nfs.mount-path}")
    private String basePath;

    private Path resolvePath(String relativePath) {
        return Paths.get(basePath, relativePath);
    }

    @Override
    public void storeFile(String fileName, InputStream inputStream) throws IOException {
        Path target = resolvePath(fileName);
        Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public byte[] fetchFile(String fileName) throws IOException {
        return Files.readAllBytes(resolvePath(fileName));
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            return Files.deleteIfExists(resolvePath(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }

    @Override
    public List<String> listFiles(String directory) throws IOException {
        try (Stream<Path> stream = Files.list(resolvePath(directory))) {
            return stream.map(Path::getFileName)
                         .map(Path::toString)
                         .collect(Collectors.toList());
        }
    }

    @Override
    public boolean createDirectory(String path) {
        try {
            Files.createDirectories(resolvePath(path));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Directory creation failed", e);
        }
    }
}
```

#### 业务层使用示例

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;

@Service
public class DocumentService {

    @Autowired
    private NfsClient nfsClient;

    public void processDocument(byte[] document, String userId) {
        String dirPath = "user_docs/" + userId;
        String fileName = "contract.pdf";
        
        // 创建用户专属目录
        nfsClient.createDirectory(dirPath);
        
        // 存储文件
        nfsClient.storeFile(dirPath + "/" + fileName, new ByteArrayInputStream(document));
        
        // 验证存储
        if (nfsClient.fetchFile(dirPath + "/" + fileName).length > 0) {
            System.out.println("Document stored successfully on NFS");
        }
    }
}
```

------

### 关键注意事项

1. **权限管理**

    - 确保NFS导出目录有适当读写权限（`no_root_squash`慎用）
    - Spring Boot应用运行用户需有挂载点操作权限

2. **自动挂载配置**

   ```bash
   # /etc/fstab 添加（实现开机自动挂载）
   192.168.1.100:/data/nfs_share  /mnt/nfs  nfs  defaults  0  0
   ```

3. **错误处理增强**

   ```java
   // 在NfsClient中添加重试机制
   @Retryable(maxAttempts=3, backoff=@Backoff(delay=1000))
   public void storeFileWithRetry(...) {...}
   ```

4. **性能优化**

    - 对大文件使用分块传输
    - 启用NFS缓存（`rsize=8192,wsize=8192`挂载参数）
    - 避免高频小文件操作

5. **安全建议**

    - 使用NFSv4 + Kerberos认证
    - 限制NFS导出IP范围
    - 敏感文件加密存储

> 完整示例可在生产环境扩展：添加文件锁机制、MD5校验、存储监控等功能，满足企业级文件存储需求。