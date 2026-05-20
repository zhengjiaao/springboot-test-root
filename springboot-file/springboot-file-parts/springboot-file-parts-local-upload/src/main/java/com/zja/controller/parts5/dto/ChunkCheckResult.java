package com.zja.controller.parts5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分片检查结果 - 用于断点续传时查询已上传的分片
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChunkCheckResult {

    /**
     * 文件是否已完整上传（秒传）
     */
    private boolean uploaded;

    /**
     * 已上传的分片编号列表
     */
    private List<Integer> uploadedChunks;

    /**
     * 合并后的文件下载地址（仅当 uploaded=true 时有值）
     */
    private String fileUrl;
}
