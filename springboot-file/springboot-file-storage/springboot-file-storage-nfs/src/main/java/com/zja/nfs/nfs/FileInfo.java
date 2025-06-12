package com.zja.nfs.nfs;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2025-06-12 15:07
 */
@Data
public class FileInfo implements Serializable {
    private String name;
    private boolean isDirectory; // true 表示是目录（即“桶”）
    private long size;            // 文件大小，目录为 0
    private String type;          // 类型：dir / file

    public FileInfo(String name, boolean isDirectory, long size) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.type = isDirectory ? "dir" : "file";
    }
}