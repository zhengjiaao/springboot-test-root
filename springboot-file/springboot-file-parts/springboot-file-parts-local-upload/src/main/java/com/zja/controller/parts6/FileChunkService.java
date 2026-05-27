package com.zja.controller.parts6;

import com.zja.controller.parts6.request.ChunkUploadStatusRequest;
import com.zja.controller.parts6.request.FileChunkMergeRequest;
import com.zja.controller.parts6.request.FileChunkUploadRequest;
import com.zja.controller.parts6.request.FileQueryRequest;
import com.zja.controller.parts6.vo.ChunkUploadStatusVO;
import com.zja.controller.parts6.vo.FileChunkMergeResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 通用文件分片上传服务层
 *
 * @author: zhengja
 * @since: 2026/04/23 10:00:00
 */
public interface FileChunkService {

    /**
     * 上传文件分片
     *
     * @param request 分片上传请求参数
     * @param file 分片文件
     */
    void uploadChunk(FileChunkUploadRequest request, MultipartFile file);

    /**
     * 合并文件分片
     *
     * @param request 分片合并请求参数
     * @return 合并结果
     */
    FileChunkMergeResultVO mergeChunks(FileChunkMergeRequest request);

    /**
     * 获取已合并文件的信息
     *
     * @param request 文件查询请求参数
     * @return 文件信息（包含是否存在、路径、大小等）
     */
    FileChunkMergeResultVO getFileInfo(FileQueryRequest request);

    /**
     * 查询分片上传状态（支持断点续传）
     *
     * @param request 分片上传状态查询请求参数
     * @return 分片上传状态
     */
    ChunkUploadStatusVO getUploadStatus(ChunkUploadStatusRequest request);

    /**
     * 清理过期的临时文件
     * 每天凌晨4点执行
     */
    void cleanupExpiredFiles();

    /**
     * 下载已合并的文件
     *
     * @param request  文件查询请求参数
     * @param response HTTP响应
     */
    void downloadFile(FileQueryRequest request, HttpServletResponse response);
}
