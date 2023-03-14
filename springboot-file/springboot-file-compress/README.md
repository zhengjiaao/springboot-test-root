# springboot-file-compress

**针对：文件或目录 压缩和解压缩**

工具：
- Apache ant **已弃用**，已迁移：Ant -> Avalon-Excalibur -> Commons-IO -> Commons-Compress
- Apache Commons-Compress 最好的JAVA生态压缩库，**最推荐**
- zip4j 功能多，**推荐**
- jdk 功能少，**不推荐**


## Apache Commons-Compress 最推荐

> 最好的JAVA生态压缩库

- [commons-compress | 官网](https://commons.apache.org/proper/commons-compress/)
- [commons-compress | github](https://github.com/apache/commons-compress)

特点：
- 负责处理压缩的底层库，它能处理的压缩格式非常多，包括ar, cpio, Unix dump, tar, zip, gzip, XZ, Pack200, bzip2, 7z, arj, lzma, snappy, DEFLATE, lz4, Brotli, Zstandard, DEFLATE64和Z格式.
- 推荐Commons Compress的一个重要原因：用它基本就可以满足你们项目中所有压缩格式的需要了。
- 支持自动识别加压缩编码格式。

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-compress</artifactId>
    <version>1.22</version>
</dependency>
```

学习推荐：
- [commons-compress | Zip格式用法介绍](https://zhuanlan.zhihu.com/p/141166836)
- [commons-compress | 7zip格式用法介绍](https://zhuanlan.zhihu.com/p/146341677)
- [commons-compress | Tar格式用法介绍](https://zhuanlan.zhihu.com/p/190146621)
- [commons-compress | 不同格式通用用法介绍](https://zhuanlan.zhihu.com/p/222905091)


## zip4j 推荐

- [zip4j | 官方](https://github.com/srikanth-lingala/zip4j)
- [zip4j | github](https://github.com/srikanth-lingala/zip4j)

特征： 
- 从Zip文件创建，添加，提取，更新，删除文件
- 读/写密码保护的Zip文件
- 支持AES 128/256加密
- 支持标准邮​​编加密
- 支持Zip64格式
- 支持存储（无压缩）和Deflate压缩方法
- 从Split Zip文件创建或提取文件（例如：z01，z02，... zip）
- 支持Unicode文件名
- 进度监视器

## 依赖引入

```xml
<dependency>
    <groupId>net.lingala.zip4j</groupId>
    <artifactId>zip4j</artifactId>
    <version>2.11.5</version>
</dependency>
```

学习推荐：

- [zip4j | springboot-file-compress-zip4j](./springboot-file-compress-zip4j)
