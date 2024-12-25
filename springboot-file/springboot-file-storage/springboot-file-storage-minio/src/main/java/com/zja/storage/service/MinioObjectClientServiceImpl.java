package com.zja.storage.service;

import com.zja.storage.minio.MinioObjectClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2024-12-25 13:37
 */
@Service
public class MinioObjectClientServiceImpl implements MinioObjectClientService {

    @Autowired
    MinioObjectClient client;

}
