package com.zja.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/2 18:34
 */
@Document(collection = "fileInfo")
@Data
public class FileInfo {
    private String id;
    private String fileName;
    private String contentType;
    private String fileId;
}
