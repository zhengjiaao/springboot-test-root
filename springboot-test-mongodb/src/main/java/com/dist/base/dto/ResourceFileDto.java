package com.dist.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 资源文件
 *
 * @author yinxp@dist.com.cn
 */
@Data
public class ResourceFileDto implements Serializable{

    private String id;      // 资源文件id

    private String fileName;   // 文件名

    private String filePath;    // 文件本地路径

    private String fileSuffix;      // 文件后缀

}
