package com.zja.report.service;

import com.zja.report.model.dto.StorageDTO;
import com.zja.report.model.dto.StorageNodeDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 存储服务功能接口
 * @Author: zhengja
 * @Date: 2024-09-04 9:43
 */
public interface StorageFileService {

    /**
     * 获取存储类型
     * @return String
     */
    String getStorageType();

    /**
     * 创建节点：暂时不允许创建多层级的节点(文件夹)
     * @param spaceId 空间id，必须的，自定义[业务id，例如：项目id、流程id等]
     * @param nodeName 节点名称，必须的
     * @return StorageNodeDTO
     */
    StorageNodeDTO createNode(String spaceId, String nodeName);

    /**
     * 获取节点信息
     * @param nodeId 节点id
     * @return StorageNodeDTO
     */
    StorageNodeDTO getNode(String nodeId);

    /**
     * 获取所属空间的根节点列表
     * @param spaceId 空间id，必须的，自定义[业务id，例如：项目id、流程id等]
     * @return StorageNodeDTO
     */
    List<StorageNodeDTO> getRootNodes(String spaceId);

    /**
     * 删除节点：会级联删除节点下所有文件
     * @param nodeId 节点id
     * @return boolean
     */
    boolean deleteNode(String nodeId);

    /**
     * 上传文件
     * @param fileName 文件名称，必须的
     * @param is 文件流，必须的
     * @param spaceId 空间id，必须的，可自定义为[业务id，例如：项目id、流程id等]
     * @param nodeId 节点id，可选的，需要创建节点(类似目录)，注，国图存储服务中相同节点（含null节点）下的文件名称会重命名，例如：a.jpg -> a(1).jpg
     * @return 文件信息
     * @throws IOException IO异常
     */
    StorageDTO uploadFile(String fileName, InputStream is, String spaceId, String nodeId) throws IOException;

    /**
     * 上传文件
     * @param fileName 文件名称，必须的
     * @param is 文件流，必须的
     * @param nodeId 节点id，可选的，需要创建节点(类似目录)，注，国图存储服务中相同节点（含null节点）下的文件名称会重命名，例如：a.jpg -> a(1).jpg
     * @return 文件信息
     * @throws IOException IO异常
     */
    StorageDTO uploadFile(String fileName, InputStream is, String nodeId) throws IOException;

    /**
     * 上传文件: 默认使用随机生成的空间(spaceId)，并且节点为空(nodeId)
     * @param fileName 文件名称，必须的
     * @param is 文件流，必须的
     * @return 文件信息
     * @throws IOException IO异常
     */
    StorageDTO uploadFile(String fileName, InputStream is) throws IOException;

    /**
     * 上传文件: 默认使用随机生成的空间(spaceId)，并且节点为空(nodeId)
     * @param file 文件，必须的
     * @return 文件信息
     * @throws IOException IO异常
     */
    StorageDTO uploadFile(MultipartFile file) throws IOException;

    /**
     * 上传文件: 默认使用随机生成的空间(spaceId)，并且节点为空(nodeId)
     * @param filePath 文件路径，必须的
     * @return 文件信息
     * @throws IOException IO异常
     */
    StorageDTO uploadFile(String filePath) throws IOException;

    /**
     * 上传文件: 默认使用随机生成的空间(spaceId)，并且节点为空(nodeId)
     * @param filePath 文件路径，必须的
     * @param nodeId 节点id，可选的，需要创建节点(类似目录)，注，国图存储服务中相同节点（含null节点）下的文件名称会重命名，例如：a.jpg -> a(1).jpg
     * @return 文件信息
     * @throws IOException IO异常
     */
    StorageDTO uploadFile(String filePath, String nodeId) throws IOException;

    /**
     * 获取文件信息
     * @param fileId 文件id
     * @return StorageDTO
     */
    StorageDTO getFileMetaData(String fileId);

    /**
     * 获取空间下的文件列表
     * @param spaceId 空间id
     * @return List<StorageDTO>
     */
    List<StorageDTO> getSpaceFiles(String spaceId);

    /**
     * 获取节点下的文件列表
     * @param nodeId 节点id
     * @return List<StorageDTO>
     */
    List<StorageDTO> getNodeFiles(String nodeId);

    /**
     * 获取文件名称
     * @param fileId 文件id
     * @return String
     */
    String getFileName(String fileId);

    /**
     * 获取文件URL
     * @param fileId 文件id
     * @return String
     */
    String getFileUrl(String fileId);

    /**
     * 获取文件预览URL todo 仅实现了国图存储预览功能，但，MongoDB 存储未实现预览功能
     * @param fileId 文件id
     * @return String
     */
    String getFilePreviewUrl(String fileId);

    /**
     * 下载文件
     * @param fileId 文件id
     * @return InputStream
     * @throws IOException IO异常
     */
    InputStream downloadFile(String fileId) throws IOException;

    /**
     * 下载文件
     * @param fileId 文件id
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    void downloadFile(String fileId, HttpServletResponse response) throws IOException;

    /**
     * 下载文件
     * @param fileId 文件id
     * @param fileName 文件名称
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    void downloadFile(String fileId, String fileName, HttpServletResponse response) throws IOException;

    /**
     * 下载文件
     * @param fileId 文件id
     * @param filePath 文件保存路径
     * @throws IOException IO异常
     */
    void downloadFile(String fileId, String filePath) throws IOException;

    /**
     * 存在文件
     * @return boolean
     */
    boolean exists(String fileId);

    /**
     * 删除文件
     * @param fileId 文件id
     * @return boolean
     */
    boolean deleteFile(String fileId);

    /**
     * 删除文件，并延迟一定时间
     * @param fileId 文件路径
     * @param delayInMillis 延迟时间，单位：毫秒  注意，延迟校验时间默认为1分钟，因此推荐设置 1分钟以上
     */
    void deleteFileByDelayed(String fileId, long delayInMillis);
}
