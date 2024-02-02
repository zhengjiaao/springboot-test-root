/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-13 17:43
 * @Since:
 */
package com.zja.hadoop.hdfs.config;

import com.zja.hadoop.hdfs.file.FileProperties;
import com.zja.hadoop.hdfs.file.FileService;
import com.zja.hadoop.hdfs.file.HdfsFileServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: zhengja
 * @since: 2023/02/13 17:43
 */
@Component
@Configuration
@EnableConfigurationProperties({FileProperties.class})
public class FileConfig {

    /**
     * 文件存储服务配置
     */
    @Bean
    FileService fileService(FileProperties properties) throws Exception {

        /*if (FileProperties.FileDBType.minio.equals(properties.getDbType())) {
            return new MinioFileServiceImpl(properties.getMinio());
        }

        if (FileProperties.FileDBType.mongodb.equals(properties.getDbType())) {
            return new MogondbFileServiceImpl(properties.getMongodb());
        }*/

        if (FileProperties.FileDBType.hdfs.equals(properties.getDbType())) {
            return new HdfsFileServiceImpl(properties.getHdfs());
        }

        throw new RuntimeException("注册 FileService bean 失败，未正确配置 test.file.dbType=" + properties.getDbType());
    }

}
