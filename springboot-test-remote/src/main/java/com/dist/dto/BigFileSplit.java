package com.dist.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/6 14:34
 */
@Data
public class BigFileSplit implements Serializable {

    // 文件路径
    private String filePath;

    // 文件大小
    private long fileLength;

    // 文件名称
    private String fileName;

    // 以多大来切割文件
    private long blockSize;

    // 文件块数
    private int size;

    // 切割后的文件的存放路径
    private String destPath;

    // 切割后的每块的文件名
    private List<String> destFileNames;
}
