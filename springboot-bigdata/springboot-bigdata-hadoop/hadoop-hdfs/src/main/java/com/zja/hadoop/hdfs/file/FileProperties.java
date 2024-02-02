/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-22 11:03
 * @Since:
 */
package com.zja.hadoop.hdfs.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author: zhengja
 * @since: 2023/02/22 11:03
 */
@Getter
@Setter
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {

    public static final String PREFIX = "test.file";

    /**
     * 数据库类型 默 minio
     */
    @NestedConfigurationProperty
    private FileDBType dbType = FileDBType.minio;

    /**
     * Minio 属性配置
     */
    @NestedConfigurationProperty
    private MinioProperties minio;

    /**
     * Mongodb 属性配置
     */
    @NestedConfigurationProperty
    private MongodbProperties mongodb;

    /**
     * Mongodb 属性配置
     */
    @NestedConfigurationProperty
    private HdfsProperties hdfs;

    /**
     * 选择文件存储的数据库类型
     */
    public enum FileDBType {

        /**
         * minio 存储文件
         */
        minio,

        /**
         * mongodb  存储文件
         */
        mongodb,

        /**
         * hdfs 存储文件
         */
        hdfs
    }


    @Getter
    @Setter
    public static class MinioProperties {

        /**
         * Minio server endpoint 示例 http://ip:port
         */
        private String endpoint;

        /**
         * Minio server accessKey 服务端访问密钥
         */
        private String accessKey;

        /**
         * Minio server secretKey 服务端密钥
         */
        private String secretKey;

        /**
         * Minio server bucketName 需服务端手动创建
         */
        private String bucketName;

    }


    @Getter
    @Setter
    public static class MongodbProperties {

        /**
         * Mongo server host. 示例 ip
         */
        private String host;

        /**
         * Mongo server port.
         */
        private int port;

        /**
         * Login user of the mongo server.
         */
        private String username;

        /**
         * Login password of the mongo server.
         */
        private char[] password;

        /**
         * Database name. 需服务端手动创建
         */
        private String database;

        /**
         * Authentication database name.
         */
        private String authenticationDatabase;
    }

    @Getter
    @Setter
    public static class HdfsProperties {

        /**
         * Hdfs server uri. 示例 hdfs://hadoop1:9000
         */
        private String uri;

        /**
         * Login user of the Hdfs server.
         */
        private String username;

        /**
         * Hdfs server path. 示例 path=/dir1
         */
        private String path;
    }
}
