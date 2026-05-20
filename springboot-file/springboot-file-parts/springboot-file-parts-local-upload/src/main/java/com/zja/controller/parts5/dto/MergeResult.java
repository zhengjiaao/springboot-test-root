package com.zja.controller.parts5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 合并结果
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeResult {

    /**
     * 是否合并成功
     */
    private boolean success;

    /**
     * 合并后的文件名
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    private long fileSize;

    /**
     * 文件下载地址
     */
    private String fileUrl;

    /**
     * 消息
     */
    private String message;
}
