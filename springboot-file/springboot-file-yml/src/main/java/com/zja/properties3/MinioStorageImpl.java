/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-05-17 16:09
 * @Since:
 */
package com.zja.properties3;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/05/17 16:09
 */
@Slf4j
public class MinioStorageImpl implements StorageManage {

    private MinioProperties properties;
//    private MinioClient client;

    public MinioStorageImpl(MinioProperties properties) {
        this.properties = properties;
//        this.client = MinioClient.builder()
//                .endpoint(properties.getEndpoint())
//                .credentials(properties.getAccessKey(), properties.getSecretKey())
//                .build();
    }

    @Override
    public void putObject(String objectName, InputStream stream) throws Exception {
//        client.putObject(PutObjectArgs.builder()
//                .bucket(properties.getBucketName())
//                .object(objectName)
//                .stream(stream, stream.available(), -1)
//                .build());
    }

    @Override
    public InputStream getInputStream(String objectName) {
       /* try {
            return client.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucketName()).object(objectName).build());
        } catch (Exception e) {
            throw new BusinessException("根据[objectName=" + objectName + "]未找到文件. " + e.getMessage());
        }*/
        return null;
    }

}
